package com.ybsx.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.ybsx.base.yml.YmlConfig;

/**
 * 文件工具类
 * @author zhouKai
 * @createDate 2018年5月14日 下午3:27:10
 */
public class FileUtil {

	/**
	 * 删除文件夹
	 * @param dir 待删除的文件夹
	 * @return true: delete success, false: delete fail
	 */
	public static boolean deleteDir(File dir) {
		if (!dir.exists()) {
			return true;
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				if (!deleteDir(file)) {
					return false;
				}
			} else {
				if (!file.delete()) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * 创建临时文件夹，其路径是： YmlConfig._this.tempDir
	 * @return
	 */
	public static File mkTempDir() {
		File dir = new File(YmlConfig._this.tempDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	

	public static void copy(InputStream is, File file) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file);) {
			byte[] bs = new byte[1024 * 1024];
			int len = 0;
			while ((len = is.read(bs)) != -1) {
				fos.write(bs, 0, len);
			}
		}
	}
	

}
