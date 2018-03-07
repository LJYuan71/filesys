package tk.ljyuan71.filegroup.model;

import java.io.Serializable;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */
public class FileGroup implements Serializable{
	
	private static final long serialVersionUID = 6072363689858631786L;
	//文件唯一标识
	private String fileId;
	//文件组id
	private String groupId;
	//文件组对应的id
	private String groupPid;
	//文件名称
	private String title;
	//文件格式
	private String ext;
	//文件长度
	private Long length;
	//文件MD5值
	private String md5;
	//创建人id
	private String creatorId;
	//创建人名称
	private String creatorName;
	//文件上传时间
	private Long uploadTime;
	//上传文件ip
	private String uploadIP;
	//状态:0删除 1正常  2(其他状态)
	private Short status;
	//是否可以删除
	private Short canDel;
	/** 获取: 文件唯一标识 值  */
	public String getFileId() {
		return fileId;
	}
	/** 设置: 文件唯一标识 值 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	/** 获取: 文件组id 值  */
	public String getGroupId() {
		return groupId;
	}
	/** 设置: 文件组id 值 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	/** 获取: 文件组对应的id 值  */
	public String getGroupPid() {
		return groupPid;
	}
	/** 设置: 文件组对应的id 值 */
	public void setGroupPid(String groupPid) {
		this.groupPid = groupPid;
	}
	/** 获取: 文件名称 值  */
	public String getTitle() {
		return title;
	}
	/** 设置: 文件名称 值 */
	public void setTitle(String title) {
		this.title = title;
	}
	/** 获取: 文件格式 值  */
	public String getExt() {
		return ext;
	}
	/** 设置: 文件格式 值 */
	public void setExt(String ext) {
		this.ext = ext;
	}
	/** 获取: 文件长度 值  */
	public Long getLength() {
		return length;
	}
	/** 设置: 文件长度 值 */
	public void setLength(Long length) {
		this.length = length;
	}
	/** 获取: 文件MD5值 值  */
	public String getMd5() {
		return md5;
	}
	/** 设置: 文件MD5值 值 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	/** 获取: 创建人id 值  */
	public String getCreatorId() {
		return creatorId;
	}
	/** 设置: 创建人id 值 */
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	/** 获取: 创建人名称 值  */
	public String getCreatorName() {
		return creatorName;
	}
	/** 设置: 创建人名称 值 */
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	/** 获取: 文件上传时间 值  */
	public Long getUploadTime() {
		return uploadTime;
	}
	/** 设置: 文件上传时间 值 */
	public void setUploadTime(Long uploadTime) {
		this.uploadTime = uploadTime;
	}
	/** 获取: 上传文件ip 值  */
	public String getUploadIP() {
		return uploadIP;
	}
	/** 设置: 上传文件ip 值 */
	public void setUploadIP(String uploadIP) {
		this.uploadIP = uploadIP;
	}
	/** 获取: 状态:0删除 1正常  2(其他状态) 值  */
	public Short getStatus() {
		return status;
	}
	/** 设置: 状态:0删除 1正常  2(其他状态) 值 */
	public void setStatus(Short status) {
		this.status = status;
	}
	/** 获取: 是否可以删除 值  */
	public Short getCanDel() {
		return canDel;
	}
	/** 设置: 是否可以删除 值 */
	public void setCanDel(Short canDel) {
		this.canDel = canDel;
	}
	

}
