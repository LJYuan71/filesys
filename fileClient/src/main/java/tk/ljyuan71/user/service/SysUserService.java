package tk.ljyuan71.user.service;

import tk.ljyuan71.file.utils.Pager;
import tk.ljyuan71.file.utils.QueryFilter;
import tk.ljyuan71.user.model.SysUser;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */
public interface SysUserService {
	//插入用户
	int insert(SysUser sysFile);
	//更新用户
	int update(SysUser sysFile);
	//删除用户
	int deleteById(String id);
	//根据id查询用户
	SysUser queryById(String id);
	//查找用户
	Pager<SysUser> queryAll(QueryFilter queryFilter);
	//删除用户
	int deleteByIds(String[] ids);
}
