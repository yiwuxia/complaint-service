package com.ybsx.base.state;

/**
 * 全局响应状态信息枚举类
 * @author zhouKai
 * @createDate 2017年8月2日 上午9:24:08
 */
public enum SuccessState implements ResponseState {

	SUCCESS("0", "成功");

	private String code;

	private String message;

	private SuccessState(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
