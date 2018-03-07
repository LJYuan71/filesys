package tk.ljyuan71.file.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.ljyuan71.file.dao.SysFileDao;
import tk.ljyuan71.file.model.SysFile;
import tk.ljyuan71.file.service.SysFileService;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */

@Service("sysFileService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)//注解形式的事务管理
public class SysFileServiceImpl implements SysFileService{
	
	@Autowired
	private SysFileDao sysFileDao;

	/* (non-Javadoc)
	 * @see tk.ljyuan71.file.service.SysFileService#insert(tk.ljyuan71.file.model.SysFile)
	 */
	@Override
	public int insert(SysFile sysFile) {
		return sysFileDao.insert(sysFile);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.file.service.SysFileService#update(tk.ljyuan71.file.model.SysFile)
	 */
	@Override
	public int update(SysFile sysFile) {
		return sysFileDao.update(sysFile);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.file.service.SysFileService#deleteById(java.lang.String)
	 */
	@Override
	public int deleteById(String id) {
		return sysFileDao.deleteById(id);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.file.service.SysFileService#queryById(java.lang.String)
	 */
	@Override
	public SysFile queryById(String id) {
		return sysFileDao.queryById(id);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.file.service.SysFileService#queryRealFile(java.util.Map)
	 */
	@Override
	public List<SysFile> queryRealFile(Map<String, Object> map) {
		return sysFileDao.queryRealFile(map);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.file.service.SysFileService#querySingleRealFile(java.util.Map)
	 */
	@Override
	public SysFile querySingleRealFile(Map<String, Object> map) {
		List<SysFile> realFile = sysFileDao.queryRealFile(map);
		if (realFile != null && realFile.size() > 0) {
			return realFile.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.file.service.SysFileService#queryFileByGroupId(java.lang.String)
	 */
	@Override
	public List<SysFile> queryFileByGroupId(String groupId) {
		if(StringUtils.isBlank(groupId)) return new ArrayList<SysFile>();
		return sysFileDao.queryFileByGroupId(groupId);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.file.service.SysFileService#queryFileByGroupPid(java.lang.String)
	 */
	@Override
	public List<SysFile> queryFileByGroupPid(String groupPid) {
		if(StringUtils.isBlank(groupPid)) return new ArrayList<SysFile>();
		return sysFileDao.queryFileByGroupPid(groupPid);
	}

	/* (non-Javadoc)
	 * @see tk.ljyuan71.file.service.SysFileService#queryFileByGroup(java.lang.Map)
	 */
	@Override
	public List<SysFile> queryFileByGroup(Map<String, Object> map) {
		if (map == null || map.isEmpty()) return new ArrayList<SysFile>();
		return sysFileDao.queryFileByGroup(map);
	}
	

}
