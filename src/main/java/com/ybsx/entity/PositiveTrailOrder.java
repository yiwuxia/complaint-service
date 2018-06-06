package com.ybsx.entity;

import java.util.List;

import com.ybsx.dao.model.ComplaintAttachment;
import com.ybsx.dao.model.ComplaintOrder;

/**
 * 正审状态的工单
 * 
 * @author zhouKai
 * @createDate 2018年4月23日 下午3:14:08
 */
public class PositiveTrailOrder extends ComplaintOrder {

	// 附件地址
	private List<ComplaintAttachment> attachments;
	// 初审备注
	private String firstComment;
	// 正审备注
	private String positiveComment;
	// enum('wait_for_trail','passed') DEFAULT NULL COMMENT '待审核，通过',
	private String state;
	// 调解人员
	private String mediator;

	public List<ComplaintAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ComplaintAttachment> attachments) {
		this.attachments = attachments;
	}

	public String getFirstComment() {
		return firstComment;
	}

	public void setFirstComment(String firstComment) {
		this.firstComment = firstComment;
	}

	public String getPositiveComment() {
		return positiveComment;
	}

	public void setPositiveComment(String positiveComment) {
		this.positiveComment = positiveComment;
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

}
