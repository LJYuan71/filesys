package tk.ljyuan71.user.model;

import java.io.Serializable;

/**
 * @author ljyuan 2017年12月18日
 * @Description:  
 */
public class SysUser implements Serializable{

	private static final long serialVersionUID = -7124721586406909763L;
	//唯一标识
	private String id;
	//账号
	private String account;
	//密码
	private String password;
	//用户名
	private String username;
	//图片文件
	private String picFileId;
	//详细资料文件
	private String detailsFileId;
	//登录ip
	private String loginIp;
	/** 获取: 唯一标识 值  */
	public String getId() {
		return id;
	}
	/** 设置: 唯一标识 值 */
	public void setId(String id) {
		this.id = id;
	}
	/** 获取: 账号 值  */
	public String getAccount() {
		return account;
	}
	/** 设置: 账号 值 */
	public void setAccount(String account) {
		this.account = account;
	}
	/** 获取: 密码 值  */
	public String getPassword() {
		return password;
	}
	/** 设置: 密码 值 */
	public void setPassword(String password) {
		this.password = password;
	}
	/** 获取: 用户名 值  */
	public String getUsername() {
		return username;
	}
	/** 设置: 用户名 值 */
	public void setUsername(String username) {
		this.username = username;
	}
	/** 获取: 图片文件 值  */
	public String getPicFileId() {
		return picFileId;
	}
	/** 设置: 图片文件 值 */
	public void setPicFileId(String picFileId) {
		this.picFileId = picFileId;
	}
	/** 获取: 详细资料文件 值  */
	public String getDetailsFileId() {
		return detailsFileId;
	}
	/** 设置: 详细资料文件 值 */
	public void setDetailsFileId(String detailsFileId) {
		this.detailsFileId = detailsFileId;
	}
	/** 获取: 登录ip 值  */
	public String getLoginIp() {
		return loginIp;
	}
	/** 设置: 登录ip 值 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
}
