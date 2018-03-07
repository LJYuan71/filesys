package tk.ljyuan71.file.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ljyuan 2017年12月13日
 * @Description:
 */
public class FileSysUtils {
	/**
	 * 根据文件计算出文件的MD5
	 * @param file
	 * @return 32位md5值
	 */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = null;
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			int byteLen;
			if (file.length() > 2147483648L) {
				byteLen = 8192;
			} else if (file.length() > 1073741824L) {
				byteLen = 4096;
			} else if (file.length() > 524288000L) {
				byteLen = 2048;
			} else {
				byteLen = 1024;
			}
			buffer = new byte[byteLen];
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, byteLen)) != -1) {
				digest.update(buffer, 0, len);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	/**
     * 生成文件存放路径
     * @param catalogPath 初始目录路径
     * @param md5 文件md5 
     * @return 
     */
	public static String parsePath(String catalogPath, String md5) {
		return parsePath(catalogPath, md5, null);
	}
	
	/**
     * 根据时间获取文件存放路径
     * @param catalogPath 初始目录路径
     * @param md5 文件md5
     * @param fileUploadDate 文件上传的时间
     * @return 
     */
	public static String parsePath(String catalogPath, String md5, Long fileUploadDate) {
		String path = catalogPath.replace('\\', '/');
		if (!"/".equals(path.substring(path.length() - 1))){
			path += "/";
		}
		String rule = ""; // 分布规则
		Date date = null;
		if ( fileUploadDate != null) {
			date = new Date(fileUploadDate);
		} else {
			date = new Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String dateStr = sdf.format(date);
		rule += dateStr + "/";
		rule += md5.substring(md5.length() - 4, md5.length() - 2) + "/";
		rule += md5.substring(md5.length() - 2) + "/";
		return path + rule;
	}
	/**
	 * 获取文件目录下的文件
	 * @author ljyuan 2017年12月19日
	 * @param file 文件目录
	 * @param length 文件长度
	 * @return 没有时文件目录
	 */
	public static File getFile(File file,final String fileName, long length){
		//1.尝试使用文件名+后缀的获取文件
		File  f1 = new File(file.getAbsolutePath(),fileName);
		if (f1.exists() && f1.isFile()) {
			return f1;
		}
		//2.如果名称+后缀不匹配时去掉后缀名匹配
		File[] fileList = file.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return fileName.substring(0, fileName.lastIndexOf(".")).equals(name.substring(0, name.lastIndexOf(".")));
			}
			
		});
		for (File f2 : fileList) {
			if (f2.isFile() && f2.length() == length) {
				return f2;//名称和大小匹配
			}
		}
		return file;
	}
	/**
	 * 获取请求ip
	 * @author ljyuan 2018年1月18日
	 */
	public static String getRequestIp(HttpServletRequest request){
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
	
	/**
	 * 字符Md5加密
	 */
	public static String getMD5(String key, String pKey) {
		try {
			if (key == null || "".equals(key.trim())) {
				return "";
			}
			if (pKey == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				pKey = sdf.format(new Date());
			}
			key = key + pKey;
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(key.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bs.length; i++) {
				sb.append(Character.forDigit((bs[i] >>> 4) & 0x0F, 16)).append(
						Character.forDigit(bs[i] & 0x0F, 16));
			}
			return sb.toString();
		} catch (Exception e) {
		}
		return "";
	}
	/**
	 * 获取用户对应的文件上传URL
	 * @author ljyuan 2018年1月31日
	 * @param userId
	 * @return
	 */
	public static Map<String, String> getFileUrl(){
		String filesys = FileSysConfig.getSystemConfig("filesys");
		String filePrivateKey = FileSysConfig.getSystemConfig("filePrivateKey");
		String pKey = FileSysUtils.getMD5(filePrivateKey, null);
		String filesysUrl = FileSysConfig.getUrlConfig("filesysUrl");
		String fileDownLoadUrl = FileSysConfig.getUrlConfig("fileDownLoadUrl");
		String fileDelUrl = FileSysConfig.getUrlConfig("fileDelUrl");
		String fileGroupDelUrl = FileSysConfig.getUrlConfig("fileGroupDelUrl");
		String queryFilesUrl = FileSysConfig.getUrlConfig("queryFilesUrl");
		String imgUploadUrl = FileSysConfig.getUrlConfig("imgUploadUrl");
		String fileUploadUrl = FileSysConfig.getUrlConfig("fileUploadUrl");
		String chunkUploadUrl = FileSysConfig.getUrlConfig("chunkUploadUrl");
		String crossDomainUrl = FileSysConfig.getUrlConfig("crossDomainUrl");
		Map<String, String> fileUrl = new HashMap<String, String>();
		fileUrl.put("filesysUrl", filesysUrl);
		fileUrl.put("fileDownLoadUrl", fileDownLoadUrl);
		fileUrl.put("fileDelUrl", fileDelUrl);
		fileUrl.put("fileGroupDelUrl", fileGroupDelUrl);
		fileUrl.put("queryFilesUrl", queryFilesUrl);
		fileUrl.put("filesys", filesys);
		fileUrl.put("filePrivateKey", pKey);
		fileUrl.put("imgUploadUrl", imgUploadUrl);
		fileUrl.put("fileUploadUrl", fileUploadUrl);
		fileUrl.put("chunkUploadUrl", chunkUploadUrl);
		fileUrl.put("crossDomainUrl", crossDomainUrl);
		return fileUrl;
	}
	
	public static void main(String[] args) {
		System.out.println(getMD5("c8fd380d8710b308a47d14e574e890ae", null));
	}
	
}
