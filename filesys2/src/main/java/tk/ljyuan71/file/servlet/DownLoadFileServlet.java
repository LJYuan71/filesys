package tk.ljyuan71.file.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tk.ljyuan71.file.model.SysFile;
import tk.ljyuan71.file.service.SysFileService;
import tk.ljyuan71.file.utils.FileConfig;
import tk.ljyuan71.file.utils.FileSysUtils;
import tk.ljyuan71.file.utils.ZipUtils;

/**
 * @author ljyuan 2017年12月19日
 * @Description:  文件下载Servlet
 */
public class DownLoadFileServlet extends HttpServlet{

	private static final long serialVersionUID = 6736990780537508726L;
	
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
		String groupId = request.getParameter("groupId");//组中所有文件zip下载
		String groupIds = request.getParameter("groupIds");//组中和组上zip下载
		String fileId = request.getParameter("fileId");//单文件下载
		String fileIds = request.getParameter("fileIds");//多文件下载
		String isLs = request.getParameter("isLs");//是否历史库
		Boolean ls = new Boolean(isLs);
		//下载排斥
		String path;
		if (ls) {
			path = FileConfig.get("lsFilePath");
		} else {
			path = FileConfig.get("filePath");
		}
		FileInputStream fis = null;
		ZipOutputStream zipOut = null;
		boolean isExistFile = false;
		try {
			File file = new File(path);
			zipOut = new ZipOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			if (StringUtils.isNotBlank(fileId)) {
				SysFile sysFile = sysFileService.queryById(fileId);
				if (sysFile != null) {
					path = FileSysUtils.parsePath(path+"/"+sysFile.getSysId(), sysFile.getFileId(), sysFile.getUploadTime());
					file = FileSysUtils.getFile(new File(path), sysFile.getFileId()+""+sysFile.getExt(), sysFile.getLength().longValue());//文件
					response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(sysFile.getTitle(), "UTF-8"));
					WritableByteChannel writer = Channels.newChannel(response.getOutputStream());
					fis = new FileInputStream(file);
					fis.getChannel().transferTo(0,file.length(), writer);
					isExistFile = true;
				}
			} else if (StringUtils.isNotBlank(groupId)) {
				List<SysFile> fileList = sysFileService.queryFileByGroupId(groupId);
				response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode("打包下载"
				+ new SimpleDateFormat("yyyyMMdd").format(new Date())+".zip", "UTF-8"));
				for (SysFile sysFile : fileList) {
					String tmpPath = FileSysUtils.parsePath(path+"/"+sysFile.getSysId(), sysFile.getFileId(), sysFile.getUploadTime());
					file = FileSysUtils.getFile(new File(tmpPath), sysFile.getFileId()+""+sysFile.getExt(), sysFile.getLength().longValue());//文件
					ZipUtils.putAndReadFile(file,zipOut,sysFile.getTitle());
					isExistFile = true;
				}
				
			} else if (StringUtils.isNotBlank(groupIds)) {
				String[] files = fileIds.split(",");
				Map<String, Object> map = new HashMap<String, Object>(16);
				map.put("groupIds", files);
				List<SysFile> fileList = sysFileService.queryFileByGroup(map);
				response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode("打包下载"
				+ new SimpleDateFormat("yyyyMMdd").format(new Date())+".zip", "UTF-8"));
				for (SysFile sysFile : fileList) {
					String tmpPath = FileSysUtils.parsePath(path+"/"+sysFile.getSysId(), sysFile.getFileId(), sysFile.getUploadTime());
					file = FileSysUtils.getFile(new File(tmpPath), sysFile.getFileId()+""+sysFile.getExt(), sysFile.getLength().longValue());//文件
					ZipUtils.putAndReadFile(file,zipOut,sysFile.getTitle());
					isExistFile = true;
				}
			} else if (StringUtils.isNotBlank(fileIds)) {
				String[] files = fileIds.split(",");
				Map<String, Object> map = new HashMap<String, Object>(16);
				map.put("fileIds", files);
				List<SysFile> fileList = sysFileService.queryFileByGroup(map);
				response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode("打包下载"
						+ new SimpleDateFormat("yyyyMMdd").format(new Date())+".zip", "UTF-8"));
				for (SysFile sysFile : fileList) {
					String tmpPath = FileSysUtils.parsePath(path+"/"+sysFile.getSysId(), sysFile.getFileId(), sysFile.getUploadTime());
					file = FileSysUtils.getFile(new File(tmpPath), sysFile.getFileId()+""+sysFile.getExt(), sysFile.getLength().longValue());//文件
					ZipUtils.putAndReadFile(file,zipOut,sysFile.getTitle());
					isExistFile = true;
				}
			}
			if (!isExistFile) {//文件不存在
				response.reset();
				response.setContentType("text/html; charset=utf-8");
				PrintWriter pw = response.getWriter();
			    pw .println("<script>alert('不存在对应文件!');window.history.back(-1);</script>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.reset();
			response.setContentType("text/html; charset=utf-8");
			PrintWriter pw = response.getWriter();
		    pw .println("<script>alert('下载文件出错!');window.history.back(-1);</script>");
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (zipOut != null) {
					zipOut.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
