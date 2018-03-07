package tk.ljyuan71.filegroup.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.ljyuan71.file.utils.Pager;
import tk.ljyuan71.file.utils.QueryFilter;
import tk.ljyuan71.filegroup.dao.FileGroupDao;
import tk.ljyuan71.filegroup.model.FileGroup;
import tk.ljyuan71.filegroup.service.FileGroupService;
import tk.ljyuan71.user.model.SysUser;

import com.github.pagehelper.PageHelper;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */

@Service("fileGroupService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)//注解形式的事务管理
public class FileGroupServiceImpl implements FileGroupService{
	
	@Autowired
	private FileGroupDao fileGroupDao;

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#insert(tk.ljyuan71.filegroup.model.FileGroup)
	 */
	@Override
	public int insert(FileGroup fileGroup) {
		return fileGroupDao.insert(fileGroup);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#updateStatus(java.util.Map)
	 */
	@Override
	public int updateStatus(Map<String, Object> map) {
		return fileGroupDao.updateStatus(map);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#deleteById(java.lang.String)
	 */
	@Override
	public int deleteById(String id) {
		return fileGroupDao.deleteById(id);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#queryById(java.lang.String)
	 */
	@Override
	public FileGroup queryById(String id) {
		return fileGroupDao.queryById(id);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#queryAll(java.util.Map)
	 */
	@Override
	public Pager<FileGroup> queryAll(QueryFilter filter) {
		Map<String, Object> paramMap = filter.getFilters();
		Pager<FileGroup> returnPager=new Pager<FileGroup>();
		if (filter.isNeedPage()) {
			//分页
			Pager<FileGroup> pager = filter.getPager();
			PageHelper.offsetPage(pager.getBeginRum(), pager.getLimit());
			List<FileGroup> userList = fileGroupDao.queryAll(paramMap);
			Integer total = fileGroupDao.queryTotal(paramMap);
			returnPager.setDataList(userList);
			returnPager.setCount(total);
		} else {
			//不分页
			List<FileGroup> userList = fileGroupDao.queryAll(paramMap);
			returnPager.setDataList(userList);
		}
		return returnPager;
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#queryTotal(java.util.Map)
	 */
	@Override
	public int queryTotal(Map<String, Object> filter) {
		return fileGroupDao.queryTotal(filter);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#queryFileByGroup(java.util.Map)
	 */
	@Override
	public List<FileGroup> queryFileByGroup(Map<String, Object> paramMap) {
		return fileGroupDao.queryFileByGroup(paramMap);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#delById(java.lang.String)
	 */
	@Override
	public int delById(String id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("newStatus", 0);
		paramMap.put("fileId", id);
		return fileGroupDao.updateStatus(paramMap);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#delByGroupId(java.lang.String)
	 */
	@Override
	public int delByGroupId(String groupId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("newStatus", 0);
		paramMap.put("groupId", groupId);
		return fileGroupDao.updateStatus(paramMap);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#delByGroupPid(java.lang.String)
	 */
	@Override
	public int delByGroupPid(String groupPid) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("newStatus", 0);
		paramMap.put("groupPid", groupPid);
		return fileGroupDao.updateStatus(paramMap);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.filegroup.service.FileGroupService#inserts(java.util.List)
	 */
	@Override
	public int inserts(List<FileGroup> fileGroups, SysUser sysUser) {
		int total = 0;
		for (FileGroup fileGroup : fileGroups) {
			fileGroup.setCreatorId(sysUser.getId());
			fileGroup.setCreatorName(sysUser.getUsername());
			fileGroup.setUploadIP(sysUser.getLoginIp());
			fileGroup.setStatus((short)1);
			String ext = null;
			String title = fileGroup.getTitle();
			if (StringUtils.isNotBlank(title) && title.indexOf(".") != -1) {
				ext = title.substring(title.lastIndexOf(".")+1);
			}
			fileGroup.setExt(ext);
			if (fileGroup.getCanDel() == null) {
				fileGroup.setCanDel((short)1);
			}
			if (fileGroup.getUploadTime() == null) {
				fileGroup.setUploadTime(System.currentTimeMillis());
			}
			total += fileGroupDao.insert(fileGroup);
		}
		return total;
	}

}
