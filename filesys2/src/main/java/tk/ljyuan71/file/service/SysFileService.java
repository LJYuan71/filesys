package tk.ljyuan71.file.service;

import java.util.List;
import java.util.Map;

import tk.ljyuan71.file.model.SysFile;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */
public interface SysFileService {
	//添加文件
	int insert(SysFile sysFile);
	//更新文件
	int update(SysFile sysFile);
	//删除文件
	int deleteById(String id);
	//根据id查询文件
	SysFile queryById(String id);
	//查找真实文件
	List<SysFile> queryRealFile(Map<String, Object> map);
	//查找真实文件
	SysFile querySingleRealFile(Map<String, Object> map);
	//根据组id查询文件
	List<SysFile> queryFileByGroupId(String groupId);
	//根据组查询文件
	List<SysFile> queryFileByGroup(Map<String, Object> map);
}
