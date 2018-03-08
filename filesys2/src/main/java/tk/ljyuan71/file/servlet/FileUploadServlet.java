package tk.ljyuan71.file.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tk.ljyuan71.file.model.SysFile;
import tk.ljyuan71.file.service.SysFileService;
import tk.ljyuan71.file.utils.FileConfig;
import tk.ljyuan71.file.utils.FileSysUtils;

/**
 * Servlet user to accept file upload
 */
public class FileUploadServlet extends HttpServlet {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.创建DiskFileItemFactory对象，配置缓存用
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

		// 2. 创建 ServletFileUpload对象
		ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

		// 3. 设置文件名称编码
		servletFileUpload.setHeaderEncoding("utf-8");

		// 4. 开始解析文件
		// 文件md5获取的字符串
		String fileMd5 = null;
		// 文件的索引
		String chunk = null;
		String size = null;
		String retMsg = null;//返回信息
		String userId = null;
		String groupId = null;
		String sysId = "defsys";//当sys为空时，默认保存进此系统路径
		String fileId  = UUID.randomUUID().toString();
		try {
			List<FileItem> items = servletFileUpload.parseRequest(request);
			for (FileItem fileItem : items) {
				boolean isAdd = false;
				if (fileItem.isFormField()) { 
					// >> 普通数据,能够获取非文件的参数值，其中有webupload上传组件name="fileMd5";name="fileMd5";name="name";name="type";
					//name="lastModifiedDate";name="size";name="chunks";name="chunk"等以及自定义参数的值
					String fieldName = fileItem.getFieldName();
					if ("fileMd5".equals(fieldName)) {
						fileMd5 = fileItem.getString("utf-8");
					}
					if ("chunk".equals(fieldName)) {
						chunk = fileItem.getString("utf-8");
					}
					if ("size".equals(fieldName)) {
						size = fileItem.getString("utf-8");
					}
					if ("userId".equals(fieldName)) {
						userId = fileItem.getString("utf-8");
					}
					if ("groupId".equals(fieldName)) {
						groupId = fileItem.getString("utf-8");
					}
					if ("sysId".equals(fieldName)) {
						sysId = fileItem.getString("utf-8");
					}
				} else { 
					File catalog = null;//文件目录
					File saveFile = null;//保存的文件
					String fileName = fileItem.getName();
					if (StringUtils.isBlank(chunk)) {//不使用分片上传或者使用分片上传时文件大小小于chunkSize的值
						String catalogPath = FileSysUtils.parsePath(FileConfig.get("filePath")+"/"+sysId,fileId);
						catalog = new File(catalogPath);
						saveFile = new File(catalogPath,fileId+fileName.substring(fileName.lastIndexOf(".")));//文件全限定名
						isAdd = true;
					} else {//使用分片上传
						// 如果文件夹没有创建文件夹
						catalog = new File(FileConfig.get("tempPath") + "/" + fileMd5);
						if (!catalog.exists()) {
							catalog.mkdirs();
						}
						// 保存分片文件
						saveFile = new File(FileConfig.get("tempPath") + "/" + fileMd5 + "/" + chunk);
					}
					if (!catalog.exists()) {
						catalog.mkdirs();
					}
					fileItem.write(saveFile);
					if (isAdd) {
						SysFile f = new SysFile(fileName, sysId, isAdd);
						f.setMd5(FileSysUtils.getFileMD5(saveFile));
						f.setFileId(fileId);
						f.setLength(Long.parseLong(size));
						f.setUserId(userId);
						f.setUploadIP(FileSysUtils.getRequestIp(request));
						if (StringUtils.isNotBlank(groupId)) {
							f.setGroupId(groupId);
						}
						sysFileService.insert(f);
						retMsg = "{\"status\":1,\"isChunk\":1,\"fileName\":\""+fileName+"\",\"fileId\":\""+f.getFileId()
								+"\",\"fileMd5\":\""+f.getMd5()+"\",\"uploadTime\":\""+f.getUploadTime()+"\"}";
					} else {
						retMsg = "{\"status:1\",\"isChunk\":1\"}";
					}
				}
			}
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write(retMsg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
