package tk.ljyuan71.file.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tk.ljyuan71.file.model.SysFile;
import tk.ljyuan71.file.service.SysFileService;
import tk.ljyuan71.file.utils.FileConfig;
import tk.ljyuan71.file.utils.FileSysUtils;

/**
 * Servlet implementation class CheckFileExistServlet
 */
public class CheckFileExistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
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
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String fileMd5 = request.getParameter("fileMd5");
		String fileName = request.getParameter("fileName");
		Long fileSize = Long.parseLong(request.getParameter("fileSize"));
		String userId = request.getParameter("userId");
		String groupId = request.getParameter("groupId");
		String groupPid = request.getParameter("groupPid");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("md5", fileMd5);
		param.put("length", fileSize);
		SysFile sysFile = sysFileService.querySingleRealFile(param);
		response.setContentType("text/json; charset=utf-8");
		PrintWriter pw = response.getWriter();
		if (sysFile != null && StringUtils.isNotBlank(sysFile.getMd5())) {
			File file = new File(FileSysUtils.parsePath(FileConfig.get("filePath"), sysFile.getMd5(), sysFile.getRealTime()),fileMd5+sysFile.getExt());
			if (file.exists() && file.isFile()) {//文件真实存在
				if ("checkAndSave".equalsIgnoreCase(action)) {//检查是否存在时保存
					//重新赋值sysFile
					SysFile f = new SysFile(fileName, false);
					f.setUserId(userId);
					f.setMd5(fileMd5);
					f.setGroupId(groupId);
					f.setExt(sysFile.getExt());
					f.setGroupId(groupId);
					f.setGroupPid(groupPid);
					f.setUploadIP(FileSysUtils.getRequestIp(request));
					if (StringUtils.isBlank(f.getGroupId())) {
						f.setGroupId(UUID.randomUUID().toString());
					}
					if (StringUtils.isBlank(f.getGroupPid())) {
						f.setGroupPid(UUID.randomUUID().toString());
					}
					f.setLength(fileSize);
					f.setRealTime(sysFile.getRealTime());
					sysFileService.insert(f);
					
					pw.write("{\"ifExist\":1,\"ifSave\":1,\"fileName\":\""+fileName+"\",\"fileId\":\""+f.getFileId()
							+"\",\"fileMd5\":\""+fileMd5+"\",\"uploadTime\":\""+f.getUploadTime()+"\"}");
				} else {//只检查不保存
					pw.write("{\"ifExist\":1,\"ifSave\":0}");
				}
			} else {
				System.out.println("真实文件不存在,但却存在上传记录,请管理员解决问题");//记录系统BUG日志
				pw.write("{\"ifExist\":0}");
			}
		} else {//文件不存在
			pw.write("{\"ifExist\":0}");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
