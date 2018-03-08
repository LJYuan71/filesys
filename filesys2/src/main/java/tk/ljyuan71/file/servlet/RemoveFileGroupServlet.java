package tk.ljyuan71.file.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tk.ljyuan71.file.model.SysFile;
import tk.ljyuan71.file.service.SysFileService;
/**
 * @author ljyuan 2018年1月18日
 * @Description:  
 */
public class RemoveFileGroupServlet extends HttpServlet{
	
	private static final long serialVersionUID = -7163223998341616812L;
	
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
		//跨域
		String jsonpCallback = request.getParameter("jsonpCallback");
		String userId = request.getParameter("userId");
		//支持3种批量删除方式
		String fileIds = request.getParameter("fileIds");
		String groupId = request.getParameter("groupId");
		String groupIds = request.getParameter("groupIds");
		response.setContentType("text/html;charset=utf-8");
		List<SysFile> fileList = null;
		
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(fileIds)) {
				paramMap.put("fileIds", fileIds.split(","));
			}
			paramMap.put("groupId", groupId);
			paramMap.put("groupIds", groupIds);
			fileList = sysFileService.queryFileByGroup(paramMap);
			for (SysFile sysFile : fileList) {
				//先行过滤
				if (sysFile.getCanDel() == 1 && sysFile.getStatus() == 1 && userId.equals(sysFile.getUserId()) ) {
					request.setAttribute("fileId", sysFile.getFileId());
					request.getRequestDispatcher("/removeFile").forward(request, response);
				}
			}
			response.getWriter().write(jsonpCallback+"({\"status\":true,\"code\":"+200+",\"message\":\"批量删除成功\"})");
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(jsonpCallback+"({\"status\":false,\"code\":"+500+"})");
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
