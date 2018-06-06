package com.ybsx.entity;

import java.util.Date;
import java.util.List;

import com.ybsx.dao.model.ComplaintAttachment;
import com.ybsx.dao.model.ComplaintOrder;

/**
 * 工单信息
 * @author zhouKai
 * @createDate 2018年4月23日 下午3:14:08
 */
public class OrderVO extends ComplaintOrder {

	// 附件
	private List<ComplaintAttachment> attachments;
	// 初审备注
	private String firstComment;
	// 正审备注
	private String positiveComment;
	// 最终处理备注
	private String endComment;
	// 通过初审的时间 
	private Date firstTime;
	// 通过正审的时间
	private Date positiveTime;
	// 通过最终处理的时间
	private Date lastTime;
	// 初审状态，wait_for_trail：待审核，passed：通过，refused：拒绝',
	private String firstState;
	// malicious_complaint:拒绝原因,name_error:恶意投诉，image_error:姓名和身份证不一致，图片不清晰
	private String refuseReason;

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

	public String getEndComment() {
		return endComment;
	}

	public void setEndComment(String endComment) {
		this.endComment = endComment;
	}

	public Date getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}

	public Date getPositiveTime() {
		return positiveTime;
	}

	public void setPositiveTime(Date positiveTime) {
		this.positiveTime = positiveTime;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getFirstState() {
		return firstState;
	}

	public void setFirstState(String firstState) {
		this.firstState = firstState;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

}
