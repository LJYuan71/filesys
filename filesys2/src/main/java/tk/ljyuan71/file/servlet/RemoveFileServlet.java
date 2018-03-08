package tk.ljyuan71.file.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tk.ljyuan71.file.model.SysFile;
import tk.ljyuan71.file.service.SysFileService;
import tk.ljyuan71.file.utils.FileConfig;
import tk.ljyuan71.file.utils.FileSysUtils;
/**
 * @author ljyuan 2018年1月18日
 * @Description:  
 */

public class RemoveFileServlet extends HttpServlet{

	private static final long serialVersionUID = -4296821746675744540L;
	SysFileService sysFileService;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		WebApplicationContext app = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		sysFileService = (SysFileService)app.getBean("sysFileService");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ip = FileSysUtils.getRequestIp(request);
		String userId = request.getParameter("userId");
		//跨域
		String jsonpCallback = request.getParameter("jsonpCallback");
		String fileId = request.getParameter("fileId");
		String isLs = request.getParameter("isLs");//是否历史库，历史库时真实删除文件
		Boolean ls = new Boolean(isLs);
		boolean isExistFile = false;
		response.setContentType("text/html;charset=utf-8");
		//下载排斥
		String path;
		try {
			if (ls) {
				path = FileConfig.get("lsFilePath");
			} else {
				path = FileConfig.get("filePath");
			}
			if (StringUtils.isNotBlank(fileId)) {
				SysFile sysFile = sysFileService.queryById(fileId);
				if (sysFile != null && userId.equals(sysFile.getUserId()) && sysFile.getCanDel() == 1 && sysFile.getStatus() == 1) {
					isExistFile = true;
					path = FileSysUtils.parsePath(path+"/"+sysFile.getSysId(), sysFile.getFileId(), sysFile.getUploadTime());
					File file = FileSysUtils.getFile(new File(path), sysFile.getFileId()+""+sysFile.getExt(),
							sysFile.getLength().longValue());//文件
					if (ls) {
						file.delete();
						sysFileService.deleteById(fileId);
					} else {
						//判断历史文件目录是否已存在相同已删除文件(这里历史文件使用Md5判重)
						path = FileSysUtils.parseLsPath(sysFile.getMd5());
						File lsFile = FileSysUtils.getFile(new File(path),
								sysFile.getMd5()+""+sysFile.getExt(), sysFile.getLength().longValue());
						if (!lsFile.exists() || !lsFile.isFile()) {
							//复制文件到历史文件目录
							FileUtils.copyFileToDirectory(file, 
									new File(FileSysUtils.parseLsPath(sysFile.getMd5())));   
						}
						sysFile.setStatus((short)3);
						sysFile.setUploadIP(ip);
						sysFileService.update(sysFile);
					}
				}
			}
			if (isExistFile) {
				response.getWriter().write(jsonpCallback+"({\"status\":true,\"code\":"+200+",\"message\":\"删除成功\"});");
			} else {
				response.getWriter().write(jsonpCallback+"({\"status\":true,\"code\":"+500+",\"message\":\"文件不存在或者没有权限操作文件\"});");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(jsonpCallback+"({\"status\":false,\"code\":"+500+",\"message\":\"程序错误\"});");
			throw new RuntimeException("删除文件失败", e);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
