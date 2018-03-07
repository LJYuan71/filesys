package tk.ljyuan71.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import tk.ljyuan71.file.utils.FileSysUtils;
import tk.ljyuan71.file.utils.Pager;
import tk.ljyuan71.file.utils.QueryFilter;
import tk.ljyuan71.filegroup.service.FileGroupService;
import tk.ljyuan71.user.model.SysUser;
import tk.ljyuan71.user.service.SysUserService;

/**
 * @author ljyuan 2018年1月18日
 * @Description:  
 */
//允许跨域
@Controller
@RequestMapping("/sysUser")
public class SysUserController {
	
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private FileGroupService fileGroupService;
	
	@RequestMapping("/queryAllJson.do")
	@ResponseBody
	public Map<String, Object> queryAllJson(HttpServletRequest request, HttpServletResponse response){
		QueryFilter queryFilter=new QueryFilter(request, true);
		Pager<SysUser> sysUser = sysUserService.queryAll(queryFilter);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", sysUser.getDataList()); //这里的 rows 和total 的key 是固定的 
		map.put("count", sysUser.getCount());
		map.put("code", 0);
		map.put("msg", "成功");
		return map;
	}
	@RequestMapping("/save")
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response, @RequestBody SysUser sysUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int count = 0;
			String groupPid = request.getParameter("groupPid");
			if (StringUtils.isBlank(sysUser.getId())) {
				if(StringUtils.isBlank(groupPid)){
					sysUser.setId(UUID.randomUUID().toString());
				}else{
					sysUser.setId(groupPid);
				}
				count = sysUserService.insert(sysUser);
			} else {
				count = sysUserService.update(sysUser);
			}
			map.put("data", count);
			map.put("code", 200);
			map.put("msg", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 500);
			map.put("msg", "失败");
		}
		return map;
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public ModelAndView edit(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		SysUser sysUser = null;
		if (StringUtils.isNotBlank(id)) {
			sysUser = (SysUser) sysUserService.queryById(id);
		}
		if (sysUser == null) {
			sysUser = new SysUser();
		}
		HttpSession session = request.getSession();
		session.setAttribute("curUser", sysUser);
		ModelAndView mv = new ModelAndView("/edit");
		Map<String, String> fileUrl = FileSysUtils.getFileUrl();
		mv.addObject("sysUser", sysUser).addObject("fileUrl", fileUrl);
		return mv;
	}
	
	@RequestMapping("/get")
	@ResponseBody
	public ModelAndView get(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		SysUser sysUser = (SysUser) sysUserService.queryById(id);
		ModelAndView mv = new ModelAndView("/get");
		Map<String, String> fileUrl = FileSysUtils.getFileUrl();
		mv.addObject("sysUser", sysUser).addObject("fileUrl", fileUrl);
		return mv;
	}
	/**
	 * 批量删除
	 */
	@RequestMapping("/del")
	@ResponseBody
	public String del(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String flag = "fail";
		try {
			String sysUserId = request.getParameter("ids");
			if (StringUtils.isNotBlank(sysUserId)) {
				String[] aryId = sysUserId.split(",");
				Map<String, Object> paramMap = new HashMap<String, Object>();
				if (aryId.length == 1){
					sysUserService.deleteById(aryId[0]);
					//删除附件
					paramMap.put("groupPid", aryId[0]);
					paramMap.put("newStatus", 0);
					fileGroupService.updateStatus(paramMap);
				} else {
					sysUserService.deleteByIds(aryId);
					for (String userId : aryId) {
						paramMap.clear();
						paramMap.put("groupPid", userId);
						paramMap.put("newStatus", 0);
						fileGroupService.updateStatus(paramMap);
					}
				}
				flag = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
