package tk.ljyuan71.file.servlet;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
 * 合并上传文件
 */
public class UploadActionServlet extends HttpServlet {
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
		String action = request.getParameter("action");
		String userId = request.getParameter("userId");
		String groupId = request.getParameter("groupId");
		String groupPid = request.getParameter("groupPid");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw = response.getWriter();
		if ("mergeChunks".equals(action)) {//合并指定临时文件夹下的指定文件(如果存在的话)
			// 获得需要合并的目录
			String fileMd5 = request.getParameter("fileMd5");
			String fileName = request.getParameter("fileName");

			// 读取目录所有文件
			File f = new File(FileConfig.get("tempPath") + "/" + fileMd5);
			File[] fileArray = f.listFiles(new FileFilter() {

				// 排除目录，只要文件
				@Override
				public boolean accept(File pathname) {
					if (pathname.isDirectory()) {
						return false;
					}
					return true;
				}
			});

			// 转成集合，便于排序
			if (fileArray != null && fileArray.length > 0) {
				List<File> fileList = new ArrayList<File>(
						Arrays.asList(fileArray));
				// 从小到大排序
				Collections.sort(fileList, new Comparator<File>() {

					@Override
					public int compare(File o1, File o2) {
						if (Integer.parseInt(o1.getName()) < Integer
								.parseInt(o2.getName())) {
							return -1;
						}
						return 1;
					}

				});

				// 新建保存文件
				String suffix = fileName.substring(fileName.lastIndexOf("."));
				String fileId = fileMd5;
				if (StringUtils.isBlank(fileId)) {
					fileId = UUID.randomUUID().toString();
				}
				String catalogPath = FileSysUtils.parsePath(FileConfig.get("filePath"), fileId);
				File catalog = new File(catalogPath);
				if (!catalog.exists()) {
					catalog.mkdirs();
				}
				File saveFile = new File(catalogPath, fileId + suffix);// 文件全限定名
				// 创建文件
				saveFile.createNewFile();

				// 输出流
				FileOutputStream fileOutputStream = new FileOutputStream(
						saveFile);
				FileChannel outChannel = fileOutputStream.getChannel();

				// 合并
				FileChannel inChannel;
				boolean isFull = true;
				for (int i = 0; i < fileList.size(); i++) {
					File chunkFile = fileList.get(i);
					if (chunkFile.getName().equals(i+"")) {
						inChannel = new FileInputStream(fileList.get(i)).getChannel();
						inChannel.transferTo(0, inChannel.size(), outChannel);
						inChannel.close();
						// 删除分片
						chunkFile.delete();
					} else {//缺少分片
						isFull = false;
						break;
					}
				}

				// 关闭流
				if (isFull) {
					fileOutputStream.close();
					outChannel.close();
					
					// 清除临时文件夹
					File tempFile = new File(FileConfig.get("tempPath") + "/" + fileMd5);
					if (tempFile.isDirectory() && tempFile.exists()) {
						tempFile.delete();
					};
					SysFile addFile = new SysFile(fileName, true);
					addFile.setMd5(fileMd5);
					addFile.setLength(saveFile.length());
					addFile.setUserId(userId);
					addFile.setUploadIP(FileSysUtils.getRequestIp(request));
					if (StringUtils.isNotBlank(groupId)) {
						addFile.setGroupId(groupId);
					}
					if (StringUtils.isNotBlank(groupPid)) {
						addFile.setGroupPid(groupPid);
					}
					sysFileService.insert(addFile);
					pw.write("{\"status\":1,\"fileName\":\""+fileName+"\",\"fileId\":\""+addFile.getFileId()+"\",\"fileMd5\":\""
							+fileMd5+"\",\"uploadTime\":"+addFile.getUploadTime()+"}");
				} else {
					pw.write("{\"status\":0,\"msg\":\"分片不完整,请重新上传\"}");
				}
			}
		} else if ("checkChunk".equals(action)) {
			// 校验文件是否已经上传并返回结果给前端

			// 文件唯一表示								
			String fileMd5 = request.getParameter("fileMd5");
			// 当前分块下标
			String chunk = request.getParameter("chunk");
			// 当前分块大小
			String chunkSize = request.getParameter("chunkSize");
			// 找到分块文件
			File checkFile = new File(FileConfig.get("tempPath") + "/" + fileMd5 + "/" + chunk);

			// 检查文件是否存在，且大小一致
			
			if (checkFile.exists() && checkFile.length() == Integer.parseInt((chunkSize))) {
				pw.write("{\"ifExist\":1}");
			} else {
				pw.write("{\"ifExist\":0}");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
