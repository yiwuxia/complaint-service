package com.ybsx.entity;

import java.util.List;

import com.ybsx.dao.model.ComplaintOrder;

/**
 * 初审状态的工单
 * @author zhouKai
 * @createDate 2018年4月23日 下午3:14:08
 */
public class FirstTrailOrder extends ComplaintOrder {

	// 附件地址
	private List<String> addresses;
	// 初审备注
	private String comment;
	// enum('wait_for_trail','passed','refused') DEFAULT NULL COMMENT '待审核，通过，拒绝',
	private String state;

	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
