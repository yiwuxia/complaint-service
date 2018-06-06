package com.ybsx.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * URL工具类
 * @author zhouKai 2018年3月9日 下午4:02:58
 */
public class UrlUtil {

	private final static String tag = "-->";
	
	private static Logger logger = Logger.getLogger(UrlUtil.class);
	
	/**
	 * get请求
	 * @param url URL路径
	 * @param params 请求参数
	 * @return 服务器响应的结果
	 * @throws IOException
	 */
	public static String doGet(String url, List<Param> params) throws IOException {
		logger.info(tag + url);
		if (params != null) {
			url += "?" + formUrlEncoded(params);
		}
		URLConnection conn = new URL(url).openConnection();
		return getResponse(conn);
	}

	/**
	 * post请求，Content-Type = applicationi/x-www-form-urlencoded; charset=UTF-8
	 * @param url
	 *            URL路径
	 * @param params
	 *            请求参数
	 * @return 响应结果
	 * @throws Exception
	 */
	public static String doPost(String url, List<Param> params) throws IOException {
		logger.info(tag + url);
		URLConnection connection = new URL(url).openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		String requestBody = params == null ? "" : formUrlEncoded(params);
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "utf8"))) {
			bw.write(requestBody);
		}
		return getResponse(connection);
	}

	/**
	 * post请求，发送json数据，Content-Type = application/json;charset=UTF-8
	 * @param url
	 *            URL路径
	 * @param json
	 *            请求参数，json格式
	 * @return 返回的结果
	 * @throws IOException
	 */
	public static String doPostJson(String url, String json) throws IOException {
		logger.info(tag + url);
		URLConnection conn = new URL(url).openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "utf8"));) {
			bw.write(json);
		}
		return getResponse(conn);
	}

	/**
	 * 获取网络请求的结果
	 * @param conn
	 *            网络请求链接
	 * @return
	 * @throws IOException
	 */
	private static String getResponse(URLConnection conn) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf8"));) {
			String line = null;
			StringBuffer result = new StringBuffer();
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		}
	}

	/**
	 * 参数列表转化为 x-www-form-urlencoded
	 * @param params
	 * @return
	 * @throws IOException
	 */
	private static String formUrlEncoded(List<Param> params) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (Param param : params) {
			String name = URLEncoder.encode(param.name, "utf-8");
			String value = URLEncoder.encode(param.value.toString(), "utf-8");
			sb.append(name + "=" + value + "&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/*
	// payload
	------WebKitFormBoundary6iUxMRnMq93k5U3J
	Content-Disposition: form-data; name="file"; filename="01.xlsx"
	Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
	
	
	------WebKitFormBoundary6iUxMRnMq93k5U3J
	Content-Disposition: form-data; name="file2"; filename="data.sql"
	Content-Type: application/octet-stream
	
	
	------WebKitFormBoundary6iUxMRnMq93k5U3J
	Content-Disposition: form-data; name="age"
	
	aa
	------WebKitFormBoundary6iUxMRnMq93k5U3J
	Content-Disposition: form-data; name="address"
	
	bb
	------WebKitFormBoundary6iUxMRnMq93k5U3J--
	 * 
	 */
	/**
	 * multipart/form-data
	 * @param url URL路径
	 * @param texts 文本数据
	 * @param files 文件数据
	 * @return
	 * @throws IOException
	 */
	public static String upload(String url, List<Param> texts, Map<String, File> files) throws IOException {
		logger.info(tag + url);
		String crlf = "\r\n"; // carriage return, line feed
		String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW"; // boundary就是request头和上传文件内容的分隔符

		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setChunkedStreamingMode(1024 * 1024);
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

		try (OutputStream out = conn.getOutputStream();) {
			out.write(crlf.getBytes());
			if (texts != null) {
				StringBuilder sb = new StringBuilder();
				for (Param param : texts) {
					sb.append("--" + boundary + crlf);
					sb.append("Content-Disposition: form-data; name=\"" + param.name + "\"" + crlf);
					sb.append(crlf);
					sb.append(param.value + crlf);
				}
				out.write(sb.toString().getBytes());
			}

			if (files != null) {
				String contentType = "application/octet-stream";
				for (Entry<String, File> entry : files.entrySet()) {
					File file = entry.getValue();
					StringBuilder sb = new StringBuilder();
					sb.append("--" + boundary + crlf);
					sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + file.getName()
							+ "\"");
					sb.append(crlf);
					sb.append("Content-Type:" + contentType + crlf);
					sb.append(crlf);
					out.write(sb.toString().getBytes());

					try (FileInputStream in = new FileInputStream(file);) {
						int len = 0;
						byte[] buf = new byte[1024 * 10];
						while ((len = in.read(buf)) != -1) {
							out.write(buf, 0, len);
						}
					}
					out.write(crlf.getBytes());
				}
			}
			out.write(("--" + boundary + "--" + crlf).getBytes());
		}
		return getResponse(conn);
	}

	/**
	 * DELETE 请求
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String doDelete(String url) throws IOException {
		logger.info(tag + url);
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("DELETE");
		return getResponse(conn);
	}

	public static class Param {
		public String name;
		public Object value;

		public Param(String name, Object value) {
			this.name = name;
			this.value = value;
		}
	}


}
