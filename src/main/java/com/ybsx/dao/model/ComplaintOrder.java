package com.ybsx.dao.model;

import java.util.Date;

import com.ybsx.util.BaseEntity;


/**
 * 对应表： complaint_order（投诉工单）
 * @author zhouKai
 * @createDate 2018年4月19日 上午11:10:50
 */
public class ComplaintOrder extends BaseEntity {

	// 自增主键
	private Integer id;
	// 用户ID
	private Integer uid;
	// 姓名
	private String name;
	// 手机号
	private String mobile;
	// 投诉的房间
	private String room;
	// 身份证图片地址
	private String idImage;
	// 投诉的内容
	private String content;
	//	审核状态，first：初审，positive：正审，last:最后处理，end；结束
	private String trailState;
	//	工单结束原因，normal：正常处理结束，customer_cancel：用户取消
	private String endReason;

	private Date createTime;

	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getIdImage() {
		return idImage;
	}

	public void setIdImage(String idImage) {
		this.idImage = idImage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTrailState() {
		return trailState;
	}

	public void setTrailState(String trailState) {
		this.trailState = trailState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getEndReason() {
		return endReason;
	}

	public void setEndReason(String endReason) {
		this.endReason = endReason;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/*
	 * 审核状态，first：初审，positive：正审，last:最后处理，end；结束 
	 */
	public interface TrailState {
		String FIRST = "first";
		String POSITIVE = "positive";
		String LAST = "last";
		String END = "end";
	}
	
	/*
	 * 工单结束原因，normal：正常处理结束，customer_cancel：用户取消, first_trail_refuse:初审是拒绝
	 */
	public interface EndReason {
		String NORMAL = "normal";
		String FIRST_TRAIL_REFUSE = "first_trail_refuse";
		String CUSTOMER_CANCEL = "customer_cancel";
	}
	


}
