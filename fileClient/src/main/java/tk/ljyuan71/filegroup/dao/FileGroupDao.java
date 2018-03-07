package tk.ljyuan71.filegroup.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import tk.ljyuan71.filegroup.model.FileGroup;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */
@Repository
public interface FileGroupDao {
	//插入用户
	int insert(FileGroup fileGroup);
	//更新文件状态
	int updateStatus(Map<String, Object> map);
	//删除文件
	int deleteById(String id);
	//根据id查询文件
	FileGroup queryById(String id);
	//查找文件
	List<FileGroup> queryAll(Map<String, Object> map);
	//获取总记录条数
	int queryTotal(Map<String, Object> filter);
	//根据组查询文件
	List<FileGroup> queryFileByGroup(Map<String, Object> paramMap);
	
}
