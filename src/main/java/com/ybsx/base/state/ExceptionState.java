package com.ybsx.base.state;

/**
 * controller层产生的未处理异常状态
 * 
 * @author zhouKai
 * @createDate 2017年11月3日 上午11:17:23
 */
public class ExceptionState implements ResponseState {

	// 异常码
	private String code;
	// 异常信息
	private String message;

	protected ExceptionState(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	/*
	 * 一般异常状态
	 */
	public static final class General extends ExceptionState {
		public static final General UNKNOWN_FAIL = new General("-1", "未知错误");
		public static final General METHOD_ARGUMENT_TYPE_MISMATCH = new General("10", "请求参数类型不匹配");
		public static final General LOGIN_FAIL = new General("20", "用户名或密码错误");

		protected General(String code, String message) {
			super(code, message);
		}
	}

	/*
	 * 权限异常
	 */
	public static final class Permission extends ExceptionState {
		public static final Permission ERROR = new Permission("400", "会话过期");
		public static final Permission UNAUTHORIZED = new Permission("403", "没有访问权限");
		public static final Permission EXPIRED_TOKEN = new Permission("401", "登录已过期");
		public static final Permission ERROR_TOKEN = new Permission("401", "身份验证失败");
		public static final Permission ALREADY_USER = new Permission("410", "权限已被使用");

		protected Permission(String code, String message) {
			super(code, message);
		}
	}

	/*
	 * 参数异常状态
	 */
	public static final class Parameter extends ExceptionState {
		public static final Parameter ERROR = new Parameter("500", "参数错误");

		protected Parameter(String code, String message) {
			super(code, message);
		}
	}

	/*
	 * 账号相关异常状态
	 */
	public static final class Account extends ExceptionState {
		public static final Account EXISTED = new Account("600", "账号已经存在");
		public static final Account RELATED = new Account("610", "用户已被关联");

		protected Account(String code, String message) {
			super(code, message);
		}
	}

	/*
	 * 购买课程的异常状态
	 */
	public static final class PurchaseCourse extends ExceptionState {
		public static final PurchaseCourse PURCHASE_FAIL = new PurchaseCourse("1010", "购买失败");
		public static final PurchaseCourse ALREADY_PURCHASED = new PurchaseCourse("1020", "已经购买，不可重复购买");
		public static final PurchaseCourse ALREADY_PACKAGE = new PurchaseCourse("1030", "已经包月");
		public static final PurchaseCourse PACKAGE_UNAVAILABLE = new PurchaseCourse("1030", "当前老师未开启包月功能");

		protected PurchaseCourse(String code, String message) {
			super(code, message);
		}
	}

	/*
	 * 登录相关的异常状态
	 */
	public static final class Login extends ExceptionState {
		public static final Login INCOMPLETE_INFORMATION = new Login("1100", "信息不完整");
		public static final Login VERCODE_ERROR = new Login("1110", "验证码错误");
		public static final Login NOT_TEACHER = new Login("1120", "不是老师");
		public static final Login NOT_MATCH = new Login("1130", "用户名或密码错误");

		protected Login(String code, String message) {
			super(code, message);
		}
	}

	public static class SmsState extends ExceptionState {
		public static final SmsState SEND_FAIL = new SmsState("1010", "验证码发送失败");
		public static final SmsState OVER_LIMIT = new SmsState("1015", "超出发送次数限制");
		public static final SmsState CODE_ERROR = new SmsState("1020", "验证码错误");

		protected SmsState(String code, String message) {
			super(code, message);
		}
	}

	/*
	 * 投诉工单状态 异常信息
	 */
	public static class OrderState extends ExceptionState {
		public static final OrderState CHANGE_FAIL = new OrderState("2010", "投诉工单修改失败");
		public static final OrderState COMPLAINT_FORBIDDEN = new OrderState("30", "同一用户，同一房间，3月之内只能投诉一次");

		protected OrderState(String code, String message) {
			super(code, message);
		}
	}

}
