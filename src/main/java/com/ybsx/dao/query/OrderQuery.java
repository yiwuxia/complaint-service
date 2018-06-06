package com.ybsx.dao.query;

import java.util.Calendar;
import java.util.Date;

/**
 * 工单查询
 * @author zhouKai
 * @createDate 2018年4月25日 下午3:52:52
 */
public class OrderQuery extends BaseQuery {

	// 用户ID
	private Integer uid;
	//   `trail_state` enum('first','positive','end') DEFAULT 'first' COMMENT '审核状态，first：初审，positive：正审，end；结束',
	private String trailState;
	// 1: 近一年， 2： 近两年
	private Integer year;
	// 工单最小创建时间
	private Date minTime;
	// 工单ID
	private Integer orderId;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getTrailState() {
		return trailState;
	}

	public void setTrailState(String trailState) {
		this.trailState = trailState;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -year);
		this.minTime = calendar.getTime();
	}

	public Date getMinTime() {
		return minTime;
	}

	public void setMinTime(Date minTime) {
		this.minTime = minTime;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}
