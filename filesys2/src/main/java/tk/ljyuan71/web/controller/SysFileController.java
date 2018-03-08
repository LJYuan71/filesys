package tk.ljyuan71.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tk.ljyuan71.file.model.SysFile;
import tk.ljyuan71.file.service.SysFileService;

import com.alibaba.fastjson.JSON;

/**
 * @author ljyuan 2018年1月18日
 * @Description:  
 */
//允许跨域
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/sysFile")
public class SysFileController {
	
	@Autowired
	private SysFileService sysFileService;
	
	@RequestMapping("/queryFilesByGroup")
	@ResponseBody
	public Map<String, Object> queryFilesByGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String groupId = request.getParameter("groupId");
		String groupPid = request.getParameter("groupPid");
		String callback = request.getParameter("jsonpCallback");
		List<SysFile> fileList = null;
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (StringUtils.isBlank(groupId) && StringUtils.isBlank(groupPid)) {
			retMap.put("code", 500);
			retMap.put("message", "确实必要参数");
			return retMap;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("groupId", groupId);
		paramMap.put("groupPid", groupPid);
		fileList = sysFileService.queryFileByGroup(paramMap);
		if (StringUtils.isNotBlank(callback)) {//跨域
			response.setContentType("text/html");
		    response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			retMap.put("code", 200);
			retMap.put("message", "成功");
			retMap.put("data", fileList);
			String jsonStr = JSON.toJSONString(retMap);
			out.print(callback + "(" + jsonStr + ")");
			return null;
		} else {
			retMap.put("code", 200);
			retMap.put("message", "成功");
			retMap.put("data", fileList);
			return retMap;
		}
	}
}
