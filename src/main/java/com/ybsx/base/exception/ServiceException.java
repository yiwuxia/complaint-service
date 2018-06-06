package com.ybsx.base.exception;

import com.ybsx.base.state.ExceptionState;

/**
 * 业务层异常
 * @author zhouKai
 * @createDate 2018年1月30日 下午3:01:51
 */
public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 异常码
	private ExceptionState state;

	public ExceptionState getState() {
		return state;
	}

	public ServiceException(ExceptionState state) {
		super(state.getMessage());
		this.state = state;
	}

	public ServiceException(ExceptionState state, Throwable cause) {
		super(state.getMessage(), cause);
		this.state = state;
	}

}
