package com.ybsx.base.yml;

/**
 * redis配置信息
 * @author zhouKai
 * @createDate 2018年1月31日 上午10:13:32
 */
public class Redis {
	public String host;
	public int port;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}