package tk.ljyuan71.file.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import tk.ljyuan71.file.model.SysFile;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */
@Repository
public interface SysFileDao {
	//插入文件
	int insert(SysFile sysFile);
	//更新文件
	int update(SysFile sysFile);
	//删除文件
	int deleteById(String id);
	//根据id查询文件
	SysFile queryById(String id);
	//查找真实文件
	List<SysFile> queryRealFile(Map<String, Object> map);
	//根据组id查询文件
	List<SysFile> queryFileByGroupId(String groupId);
	//根据组查询文件
	List<SysFile> queryFileByGroup(Map<String, Object> paramMap);
	
}
