package com.ybsx.dao.query;

import com.ybsx.util.BaseEntity;

/**
 * 基础查询条件实体类
 * @author zhouKai
 * 2018年4月18日 上午11:35:45
 */
public class BaseQuery extends BaseEntity {

	// 第几页， 1， 2， 3
	private Integer page;
	// 每页的记录数
	private Integer limit;
	// 偏移量
	private Integer offset;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
		if (this.limit != null) {
			this.offset = (page - 1) * limit;
		}
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
		if (this.page != null) {
			this.offset = (page - 1) * limit;
		}
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

}
