package com.ybsx.base.state;

/**
 * 响应状态
 * @author zhouKai
 * @createDate 2017年8月2日 上午9:22:31
 */
public interface ResponseState {

	/**
	 * 获取错误码
	 * @return
	 */
	String getCode();
	
	/**
	 * 获取错误信息
	 * @return
	 */
	String getMessage();
}
