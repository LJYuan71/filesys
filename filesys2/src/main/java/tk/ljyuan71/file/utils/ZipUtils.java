package tk.ljyuan71.file.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * @author ljyuan 2017年12月19日
 * @Description:  
 */
public class ZipUtils {
	
	/**
	 * 往输出流里添加压缩文件并写出
	 * @author ljyuan 2017年12月19日
	 * @param inFile 添加的压缩文件
	 * @param out zip输出流
	 * @param fileName 文件名称
	 */
	public static void putAndReadFile(File inFile, ZipOutputStream out, String fileName){
		if (inFile.isFile()) {
			 FileInputStream fis = null;
			 try {
				ZipEntry entry = new ZipEntry(fileName);
				out.putNextEntry(entry);
				int len = 0 ;
		        byte[] buffer = new byte[4096];
		        fis = new FileInputStream(inFile);
		        while ((len = fis.read(buffer)) > 0) {
		            out.write(buffer, 0, len);
		            out.flush();
		        }
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.closeEntry();
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
