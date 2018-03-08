package tk.ljyuan71.file.utils;

import java.util.ResourceBundle;

/**
 * @author ljyuan 2017年12月14日
 * @Description:  读取文件的配置项
 * 使用ResourceBundle读取配置文件项时，如果不存在指定的key会抛出异常MissingResourceException
 */
public class FileConfig {
	
	private static ResourceBundle fileConfigResource = ResourceBundle.getBundle("fileConfig");//读取配置文件bcp_config.properties
	
	//从配置文件中获取对应key的值
	public static String get(String key) {
		String value = fileConfigResource.getString(key);
		return value.trim();
	}
	
	public static void main(String[] args) {
		System.out.println("aa:"+get("eee"));
	}
	
}
