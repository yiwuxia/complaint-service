package com.ybsx.base;

import java.util.function.Consumer;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.ybsx.base.state.ExceptionState;
import com.ybsx.base.state.SuccessState;

/**
 * 业务处理结果描述对象
 * @author zhouKai
 * @createDate 2017年11月6日 下午2:42:44
 */
public class ResultBody<T> {

	/**成功失败标志*/
	private boolean success;

	/**返回结果表述*/
	private String message;

	/**返回结果码*/
	private String code;

	/**返回数据实体*/
	private T data;

	/**
	 * 当业务层无返回值时，使用此无参构造器
	 */
	public ResultBody() {
		this.success = true;
		this.code = SuccessState.SUCCESS.getCode();
		this.message = SuccessState.SUCCESS.getMessage();
	}

	/**
	 * 当出现异常时，调用此构造函数
	 * @param state 手动设置的异常状态
	 */
	public ResultBody(ExceptionState state) {
		this.success = false;
		this.code = state.getCode();
		this.message = state.getMessage();
	}

	/**
	 * 未出错误时，调用此构造器返回结果
	 * @param data 返回的核心数据
	 */
	public ResultBody(T data) {
		this.success = true;
		this.code = SuccessState.SUCCESS.getCode();
		this.message = SuccessState.SUCCESS.getMessage();
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String restCode) {
		this.code = restCode;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	/**
	 * 在未发生异常时，包装返回的结果
	 * @param consumer 
	 */
	public static ResultBody<Object> process(Consumer<ResultBody<Object>> consumer) {
		ResultBody<Object> result = new ResultBody<>();
		consumer.accept(result);
		result.setCode(SuccessState.SUCCESS.getCode());
		result.setMessage(SuccessState.SUCCESS.getMessage());
		result.setSuccess(true);
		return result;
	}

	public static ResultBody<?> error(String message) {
		ResultBody<?> rb = new ResultBody<>();
		rb.setSuccess(false);
		rb.setCode("1");
		rb.setMessage(message);
		return rb;
	}

	public static ResultBody<?> ok(String message) {
		ResultBody<?> rb = new ResultBody<>();
		rb.setSuccess(true);
		rb.setCode(SuccessState.SUCCESS.getCode());
		rb.setMessage(message);
		return rb;
	}

}
