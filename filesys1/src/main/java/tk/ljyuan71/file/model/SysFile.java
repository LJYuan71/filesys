package tk.ljyuan71.file.model;

import java.io.Serializable;
import java.util.UUID;

import tk.ljyuan71.file.utils.FileConfig;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */
public class SysFile implements Serializable{

	private static final long serialVersionUID = 826445432484314669L;
	//文件唯一标识
	private String fileId;
	//文件对应系统id
	private String sysId;
	//上传用户id
	private String userId;
	//文件名称
	private String title;
	//文件格式
	private String ext;
	//文件长度
	private Long length;
	//文件MD5值
	private String md5;
	//文件上传时间(给用户看)
	private Long uploadTime;
	//文件上传真实时间(与保存文件的路径有关)
	private Long realTime;
	//是否是真实文件
	private Short realFile;
	//文件组id
	private String groupId;
	//文件组对应的id
	private String groupPid;
	//状态:1正在使用(正式库)2历史文件(历史库)3已移
	private Short status;
	//是否可以删除(0不能删除 1可以删除[默认])
	private Short canDel;
	//上传文件ip
	private String uploadIP;
	
	public SysFile() {
		super();
	}
	
	/**
	 * @param add 是否新增
	 */
	public SysFile(String title,boolean add) {
		super();
		this.fileId = UUID.randomUUID().toString();
		this.sysId = FileConfig.get("filesys");
		Long curTime = System.currentTimeMillis();
		this.uploadTime = curTime;
		this.status = (short)1;
		this.canDel = (short)1;
		this.title = title;
		if (add) {
			this.ext = title.substring(title.lastIndexOf("."));
			this.realTime = curTime;
			this.realFile = (short)1;
		} else {
			this.realFile = (short)0;
		}
	}
	
	/** 获取: 文件唯一标识 值  */
	public String getFileId() {
		return fileId;
	}
	/** 设置: 文件唯一标识 值 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	/** 获取: 文件对应系统id 值  */
	public String getSysId() {
		return sysId;
	}
	/** 设置: 文件对应系统id 值 */
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	/** 获取: 上传用户id 值  */
	public String getUserId() {
		return userId;
	}
	/** 设置: 上传用户id 值 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	/** 获取: 文件上传时间(给用户看) 值  */
	public Long getUploadTime() {
		return uploadTime;
	}
	/** 设置: 文件上传时间(给用户看) 值 */
	public void setUploadTime(Long uploadTime) {
		this.uploadTime = uploadTime;
	}
	/** 获取: 文件上传真实时间(与保存文件的路径有关) 值  */
	public Long getRealTime() {
		return realTime;
	}
	/** 设置: 文件上传真实时间(与保存文件的路径有关) 值 */
	public void setRealTime(Long realTime) {
		this.realTime = realTime;
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
	/** 获取: 状态:1正在使用(正式库)2历史文件(历史库)3已移 值  */
	public Short getStatus() {
		return status;
	}
	/** 设置: 状态:1正在使用(正式库)2历史文件(历史库)3已移 值 */
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

	/** 获取: 是否是真实文件 值  */
	public Short getRealFile() {
		return realFile;
	}

	/** 设置: 是否是真实文件 值 */
	public void setRealFile(Short realFile) {
		this.realFile = realFile;
	}

	/** 获取: 上传文件ip 值  */
	public String getUploadIP() {
		return uploadIP;
	}

	/** 设置: 上传文件ip 值 */
	public void setUploadIP(String uploadIP) {
		this.uploadIP = uploadIP;
	}
	
	

}
