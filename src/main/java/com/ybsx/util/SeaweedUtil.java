package com.ybsx.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybsx.base.yml.YmlConfig;

/**
 * 海草文件系统工具类
 * @author zhouKai
 * @createDate 2018年4月19日 下午4:50:14
 */
public class SeaweedUtil {

	private static Logger logger = Logger.getLogger(SeaweedUtil.class);
	
	// 获取fid和存储位置URL
	private static String assignUrl;
	// 删除文件的URL
	private static String deleteUrl;

	private static String tag = "-->";

	static {
		// # 海草文件系统，内网IP地址
		String serverIp = YmlConfig._this.seaweed.serverIp;
		// # 海草文件系统，上传操作端口号
		String assignPort = YmlConfig._this.seaweed.assignPort;
		// # 海草文件系统，删除操作端口号
		String deletePort = YmlConfig._this.seaweed.deletePort;
		// 拼接成如下地址： http://192.168.2.11:9333/dir/assign
		assignUrl = "http://" + serverIp + ":" + assignPort + "/dir/assign";
		deleteUrl = "http://" + serverIp + ":" + deletePort + "/FID";
	}

	/**
	 * 保存文件
	 * @param file 待上传的目标文件
	 * @return url 文件的海草地址
	 */
	public static String saveFile(File file) {
		/*
		 * 1. 得到fid和存储位置url 得到如下格式字符串
		 * {"fid":"5,059c4b525428","url":"192.168.2.11:8060","publicUrl":"192.168.2.11:8060","count":1}
		 */
		try {
			String json = UrlUtil.doPost(assignUrl, null);
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("rawtypes")
			Map map = objectMapper.readValue(json, Map.class);
			String fid = map.get("fid") + "";
			String url = map.get("url") + "";
			String publicUrl = map.get("publicUrl") + "";
			url = "http://" + url + "/" + fid;

			// 2. 把目标文件发送到海草文件系统
			logger.info(tag + " saveFile:" + url);
			Map<String, File> files = new HashMap<>();
			files.put("file", file);
			String result = UrlUtil.upload(url, null, files);
//			String result = HTTPUtil.httpPut(url, file.getAbsolutePath());
			// {"name":"aa00.txt","size":61}
			logger.info(tag + " saveFile result:" + result);
			return "http://" + publicUrl + "/" + fid;
		} catch (IOException e) {
			throw new RuntimeException("文件" + file.getName() + "上传到海草文件系统失败。", e);
		}
	}

	/**
	 * 根据fid删除文件
	 * @param fid 待删除的fid
	 * @return
	 */
	public static String deleteByFid(String fid) {
		String url = deleteUrl.replace("FID", fid);
		try {
			logger.info(tag + " deleteFile:" + url);
			String result = UrlUtil.doDelete(url);
			return result;
		} catch (FileNotFoundException e) {
			logger.info(tag + e.getMessage());
			return null;
		} catch (IOException e) {
			throw new RuntimeException("文件" + fid + "删除失败。", e);
		}
	}

	/**
	 * 根据url删除文件
	 * @param url
	 */
	public static void deleteByUrl(String url) {
		try {
			String fid = url.substring(url.lastIndexOf("/") + 1);
			deleteByFid(fid);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	
	

}
