package tk.ljyuan71.filegroup.service;

import java.util.List;
import java.util.Map;

import tk.ljyuan71.file.utils.Pager;
import tk.ljyuan71.file.utils.QueryFilter;
import tk.ljyuan71.filegroup.model.FileGroup;
import tk.ljyuan71.user.model.SysUser;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */
public interface FileGroupService {
	/**插入文件*/
	int insert(FileGroup fileGroup);
	/**更新文件状态*/
	int updateStatus(Map<String, Object> map);
	/**彻底删除文件*/
	int deleteById(String id);
	/**根据id查询文件*/
	FileGroup queryById(String id);
	/**查找文件*/
	Pager<FileGroup> queryAll(QueryFilter filter);
	/**获取总记录条数*/
	int queryTotal(Map<String, Object> filter);
	/**根据组查询文件*/
	List<FileGroup> queryFileByGroup(Map<String, Object> paramMap);
	/**根据文件id删除文件状态*/
	int delById(String id);
	/**根据文件组id删除文件状态*/
	int delByGroupId(String groupId);
	/**根据文件组pid删除文件状态*/
	int delByGroupPid(String groupPid);
	/**小数量的批量添加文件*/
	int inserts(List<FileGroup> fileGroups,SysUser sysUser);
	
}
