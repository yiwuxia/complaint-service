package com.ybsx.entity;

import java.util.List;

import com.ybsx.dao.model.ComplaintOrder;
import com.ybsx.util.BaseEntity;

/**
 * 新增工单
 * @author zhouKai
 * @createDate 2018年4月19日 下午12:16:15
 */
public class AddOrder extends BaseEntity {

	private ComplaintOrder order;
	// 附件地址
	private List<String> addresses;
	// 短信验证码
	private String verifyCode;

	public ComplaintOrder getOrder() {
		return order;
	}

	public void setOrder(ComplaintOrder order) {
		this.order = order;
	}

	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

}
