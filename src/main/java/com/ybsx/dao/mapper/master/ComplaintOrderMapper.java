package com.ybsx.dao.mapper.master;

import org.apache.ibatis.annotations.Param;

import com.ybsx.dao.model.ComplaintOrder;

/**
 * 投诉工单
 * @author zhouKai
 * @createDate 2018年4月19日 上午11:19:20
 */
public interface ComplaintOrderMapper extends BaseMapper<ComplaintOrder> {
	
	/**
	 * 查询最近3个月的投诉记录数
	 * @return
	 */
	Integer selectLast3monthCnt(@Param("uid") Integer uid, @Param("room") String room);
	
	
	
	
	
	
	
	
	
	
	
	
	

}