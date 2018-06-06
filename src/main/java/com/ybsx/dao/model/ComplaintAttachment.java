package com.ybsx.dao.model;

import java.util.Date;

import com.ybsx.util.BaseEntity;

/**
 * 对应表： complaint_attachment（投诉工单附件）
 * @author zhouKai
 * @createDate 2018年4月19日 上午11:10:50
 */
public class ComplaintAttachment extends BaseEntity {

	// 自增主键 
	private Integer id;
	// 工单id，ComplaintOrder.id
	private Integer orderId;
	// 附件地址
	private String address;

	private Date createTime;

	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
