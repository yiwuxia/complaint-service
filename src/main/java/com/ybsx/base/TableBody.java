package com.ybsx.base;

/**
 * 承载表格数据
 * @author zhouKai
 * @createDate 2018年5月9日 下午12:11:54
 */
public class TableBody<T> extends ResultBody<T> {

	private Integer count;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
