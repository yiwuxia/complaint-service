package com.ybsx.base.yml;

/**
 * 数据源 data source
 * @author zhouKai
 * @createDate 2018年1月23日 下午3:32:38
 */
public class DS {

	// 地址 
	public String url;
	// 用户名
	public String username;
	// 密码
	public String password;
	// 驱动
	public String driver;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

}