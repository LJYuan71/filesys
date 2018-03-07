package tk.ljyuan71.user.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import tk.ljyuan71.user.model.SysUser;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */
@Repository
public interface SysUserDao {
	//插入用户
	int insert(SysUser sysUser);
	//更新用户
	int update(SysUser sysUser);
	//删除用户
	int deleteById(String id);
	//根据id查询用户
	SysUser queryById(String id);
	//查找用户
	List<SysUser> queryAll(Map<String, Object> map);
	//删除用户
	int deleteByIds(String[] ids);
	//获取总记录条数
	int queryTotal(Map<String, Object> filter);
}
