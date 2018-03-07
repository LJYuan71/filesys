package tk.ljyuan71.user.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.ljyuan71.file.utils.Pager;
import tk.ljyuan71.file.utils.QueryFilter;
import tk.ljyuan71.user.dao.SysUserDao;
import tk.ljyuan71.user.model.SysUser;
import tk.ljyuan71.user.service.SysUserService;

import com.github.pagehelper.PageHelper;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */
@Service("sysUserService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)//注解形式的事务管理
public class SysUserServiceImpl implements SysUserService{
	
	@Autowired
	private SysUserDao sysUserDao;

	/* (non-Javadoc)
	 * @see tk.ljyuan71.user.service.SysUserService#insert(tk.ljyuan71.user.model.SysUser)
	 */
	@Override
	public int insert(SysUser sysUser) {
		return sysUserDao.insert(sysUser);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.user.service.SysUserService#update(tk.ljyuan71.user.model.SysUser)
	 */
	@Override
	public int update(SysUser sysFile) {
		return sysUserDao.update(sysFile);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.user.service.SysUserService#deleteById(java.lang.String)
	 */
	@Override
	public int deleteById(String id) {
		return sysUserDao.deleteById(id);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.user.service.SysUserService#queryById(java.lang.String)
	 */
	@Override
	public SysUser queryById(String id) {
		return sysUserDao.queryById(id);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.user.service.SysUserService#queryAll(java.util.Map)
	 */
	@Override
	public Pager<SysUser> queryAll(QueryFilter queryFilter) {
		Map<String, Object> paramMap = queryFilter.getFilters();
		Pager<SysUser> returnPager=new Pager<SysUser>();
		if (queryFilter.isNeedPage()) {
			//分页
			Pager<SysUser> pager = queryFilter.getPager();
			PageHelper.offsetPage(pager.getBeginRum(), pager.getLimit());
			List<SysUser> userList = sysUserDao.queryAll(paramMap);
			Integer total = sysUserDao.queryTotal(paramMap);
			returnPager.setDataList(userList);
			returnPager.setCount(total);
		} else {
			//不分页
			List<SysUser> userList = sysUserDao.queryAll(paramMap);
			returnPager.setDataList(userList);
		}
		return returnPager;
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.user.service.SysUserService#deleteByIds(java.lang.String[])
	 */
	@Override
	public int deleteByIds(String[] ids) {
		return sysUserDao.deleteByIds(ids);
	}

	

}
