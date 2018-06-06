package com.ybsx.dao.model;

import java.util.Date;

import com.ybsx.util.BaseEntity;

/**
 * 对应表： complaint_trail_first（投诉初审）
 * @author zhouKai
 * @createDate 2018年4月19日 上午11:10:50
 */
public class ComplaintTrailFirst extends BaseEntity {

	// 对应投诉订单的主键，即complaint_order.id
	private Integer id;
	// 初审备注
	private String comment;
	// wait_for_trail：待审核，passed：通过，refused：拒绝',
	private String state;
	// malicious_complaint:拒绝原因,name_error:恶意投诉，image_error:姓名和身份证不一致，图片不清晰
	private String refuseReason;

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

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
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
	 * enum('wait_for_trail','passed','refused') DEFAULT NULL COMMENT '待审核，通过，拒绝',
	 */
	public interface State {
		String WAIT_FOR_TRAIL = "wait_for_trail";
		String PASSED = "passed";
		String REFUSED = "refused";
	}

	/*
	 * enum('malicious_complaint','name_error','image_error') DEFAULT NULL COMMENT '拒绝原因,恶意投诉，姓名和身份证不一致，图片不清晰',
	 */
	public interface RefuseReason {
		String MALICIOUS_COMPLAINT = "malicious_complaint";
		String NAME_ERROR = "name_error";
		String IMAGE_ERROR = "image_error";
	}

}
