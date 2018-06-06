package com.ybsx.base.yml;

/**
 * 海草文件系统
 * @author zhouKai
 * @createDate 2018年4月19日 下午4:36:03
 */
public class Seaweed {

	// 内网地址
	public String serverIp;
	// 海草文件系统，上传操作端口号
	public String assignPort;
	// 海草文件系统，删除操作端口号
	public String deletePort;
	// 公网地址
	public String publicUrl;

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getAssignPort() {
		return assignPort;
	}

	public void setAssignPort(String assignPort) {
		this.assignPort = assignPort;
	}

	public String getDeletePort() {
		return deletePort;
	}

	public void setDeletePort(String deletePort) {
		this.deletePort = deletePort;
	}

	public String getPublicUrl() {
		return publicUrl;
	}

	public void setPublicUrl(String publicUrl) {
		this.publicUrl = publicUrl;
	}

}
