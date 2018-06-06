package com.ybsx.dao.model;

import java.util.Date;

import com.ybsx.util.BaseEntity;

/**
 * 对应表： complaint_trail_positive（投诉正森）
 * @author zhouKai
 * @createDate 2018年4月19日 上午11:10:50
 */
public class ComplaintTrailPositive extends BaseEntity {

	// 对应投诉订单的主键，即complaint_order.id
	private Integer id;
	// 备注
	private String comment;
	// enum('wait_for_trail','passed') DEFAULT NULL COMMENT '待审核，通过',
	private String state;
	// 调解人员
	private String mediator;

	private Date createTime;

	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getMediator() {
		return mediator;
	}

	public void setMediator(String mediator) {
		this.mediator = mediator;
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

	/*
	 * enum('wait_for_trail','passed') DEFAULT NULL COMMENT '待审核，通过',
	 */
	public interface State {
		String WAIT_FOR_TRAIL = "wait_for_trail";
		String PASSED = "passed";
	}

}
