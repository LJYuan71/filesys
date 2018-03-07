package tk.ljyuan71.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tk.ljyuan71.file.utils.FileSysConfig;
import tk.ljyuan71.file.utils.FileSysUtils;
import tk.ljyuan71.file.utils.Pager;
import tk.ljyuan71.file.utils.QueryFilter;
import tk.ljyuan71.filegroup.model.FileGroup;
import tk.ljyuan71.filegroup.service.FileGroupService;
import tk.ljyuan71.user.model.SysUser;
import tk.ljyuan71.user.service.SysUserService;

/**
 * @author ljyuan 2018年1月18日
 * @Description:  
 */
@Controller
@RequestMapping("/filegroup")
public class FileGroupController {
	
	@Autowired
	private FileGroupService fileGroupService;
	@Autowired
	private SysUserService sysUserService;
	
	
	@RequestMapping("/queryAllJson.do")
	@ResponseBody
	public Map<String, Object> queryAllJson(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String page = request.getParameter("page");
			QueryFilter queryFilter = null;
			if (StringUtils.isBlank(page)) {
				queryFilter = new QueryFilter(request, false);
			} else {
				queryFilter = new QueryFilter(request, true);
			}
			Pager<FileGroup> fileGroupPager = fileGroupService.queryAll(queryFilter);
			map.put("data", fileGroupPager.getDataList()); //这里的 rows 和total 的key 是固定的 
			map.put("count", fileGroupPager.getCount());
			map.put("code", 200);
			map.put("msg", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 500);
			map.put("msg", "失败");
		}
		return map;
	}
	
	@RequestMapping("/saveList")
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response, @RequestBody List<FileGroup> fileGroups) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = request.getSession();
			SysUser sysUser = (SysUser) session.getAttribute("curUser");
			sysUser.setLoginIp(FileSysUtils.getRequestIp(request));
			int total = fileGroupService.inserts(fileGroups,sysUser);
			map.put("data", total);
			map.put("code", 200);
			map.put("msg", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 500);
			map.put("msg", "失败");
		}
		return map;
	}
	
	@RequestMapping("/get")
	@ResponseBody
	public Map<String, Object> get(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");
			FileGroup fileGroup = fileGroupService.queryById(id);
			map.put("data", fileGroup);
			map.put("code", 200);
			map.put("msg", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 500);
			map.put("msg", "失败");
		}
		return map;
	}
	/**
	 * 删除
	 */
	@RequestMapping("/del")
	@ResponseBody
	public Map<String, Object> del(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String fileId = request.getParameter("fileId");
			String groupId = request.getParameter("groupId");
			String groupPid = request.getParameter("groupPid");
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("fileId", fileId);
			paramMap.put("groupId", groupId);
			paramMap.put("groupPid", groupPid);
			paramMap.put("newStatus", 0);
			int delCount = fileGroupService.updateStatus(paramMap);
			map.put("data", delCount);
			map.put("code", 200);
			map.put("msg", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 500);
			map.put("msg", "失败");
		}
		return map;
	}
	/**
	 * 文件下载
	 */
	@RequestMapping("/downloadFiles")
	@ResponseBody
	public void downloadFiles(HttpServletRequest request,HttpServletResponse response) {
		OutputStream  os = null;
		HttpClient client = null;
		try {
			//获取url参数
			String groupId = request.getParameter("groupId");//组中所有文件zip下载
			String groupPid = request.getParameter("groupPid");//组中和组上zip下载
			String fileId = request.getParameter("fileId");//单文件下载
			String fileIds = request.getParameter("fileIds");//多文件下载
			//获取远程访问秘钥
			String filesys = FileSysConfig.getSystemConfig("filesys");
			String filePrivateKey = FileSysConfig.getSystemConfig("filePrivateKey");
			String pKey = FileSysUtils.getMD5(filePrivateKey, null);
			String fileDownLoadUrl = FileSysConfig.getUrlConfig("fileDownLoadUrl");
			HttpSession session = request.getSession();
			SysUser sysUser = (SysUser) session.getAttribute("curUser");
			String url = fileDownLoadUrl+"?userId="+sysUser.getId()+"&filesys="+filesys+"&filePrivateKey="+pKey;
			if (StringUtils.isNotBlank(fileId)) {
				url = url + "&fileId="+fileId;
			} else if (StringUtils.isNotBlank(fileId)) {
				url = url + "&fileIds="+fileIds;
			} else if (StringUtils.isNotBlank(fileId)) {
				url = url + "&groupId="+groupId;
			} else if (StringUtils.isNotBlank(fileId)) {
				url = url + "&groupPid="+groupPid;
			} else {
				response.reset();
				response.setContentType("text/html; charset=utf-8");
				PrintWriter pw = response.getWriter();
			    pw .println("<script>alert('不存在对应文件!');window.history.back(-1);</script>");
			    return;
			}
			client = new DefaultHttpClient();  
            HttpGet httpget = new HttpGet(url);  
            HttpResponse res = client.execute(httpget); //执行远程http请求
  
            HttpEntity entity = res.getEntity();  
            InputStream in = entity.getContent();  
            
			String fileName = getFileName(res);
			if (StringUtils.isBlank(fileName)) {
				fileName = "文件下载";
			}
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
			os = response.getOutputStream();
			//循环写入输出流
	        byte[] b = new byte[2048];
	        int length;
	        while ((length = in.read(b)) > 0) {
	            os.write(b, 0, length);
	        }
	        in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				// 这里主要关闭。
		        try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
	}
	
	/** 
     * 获取response header中Content-Disposition中的filename值 
     * @param response 
     * @return 
     */  
    public static String getFileName(HttpResponse response) {  
        Header contentHeader = response.getFirstHeader("Content-Disposition");  
        String filename = null;  
        if (contentHeader != null) {  
            HeaderElement[] values = contentHeader.getElements();  
            if (values.length == 1) {  
                NameValuePair param = values[0].getParameterByName("filename");  
                if (param != null) {  
                    try {  
                        filename=URLDecoder.decode(param.getValue(),"utf-8");  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
        return filename;  
    }  
	
}
