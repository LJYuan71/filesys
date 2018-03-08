package tk.ljyuan71.file.utils;

import java.io.InputStream;
import java.util.Properties;


/**
 * @author ljyuan 2018年1月29日
 * @Description:  
 */
public class FileSysConfig {
	
	private static FileSysConfig instance = new FileSysConfig();
	/**
	 * 单例，不允许实例化
	 */
	private FileSysConfig(){}
	
	/**
	 * 系统默认配置
	 */
	private Properties configs = new Properties();
	
	/**
	 * 系统URL配置
	 */
	private Properties urls = new Properties();
	
	static{
		loadConfig();
	}
	public enum ConfigType{
		/** 系统配置 */
		system,
		/** url配置 */
		url
	}
	private static void loadConfig(){
		InputStream in = null;
		try {
			in = FileSysConfig.class.getResourceAsStream("/fileConfig.properties");
			instance.configs.load(in);
			in = FileSysConfig.class.getResourceAsStream("/fileUrl.properties");
			instance.urls.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(in != null){
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	String getConfig(String key, ConfigType type){
		String preExpansion = null;
		switch(type){
		case system:{
			preExpansion = instance.configs.getProperty(key);
			if (preExpansion != null) {
				break;
			}
			return "";
		}
		case url:{
			preExpansion = instance.urls.getProperty(key);
			if (preExpansion == null) {
				return "";
			}
		}
		}
		return preExpansion.trim();
	}
	/**
	 * 获取系统配置
	 */
	public static String getSystemConfig(String key) {
		return instance.getConfig(key, ConfigType.system);
	}
	/**
	 * 获取有关url配置
	 */
	public static String getUrlConfig(String key) {
		return instance.getConfig(key, ConfigType.url);
	}
	
	public static void main(String[] args) {
		System.out.println("BBB:"+instance.configs.getProperty("filesys_200"));
		System.out.println("AAA:"+FileSysConfig.getSystemConfig("filesys_200"));
	}
}
