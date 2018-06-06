package com.ybsx.dao.mapper.master;

import java.util.List;

import com.ybsx.dao.query.BaseQuery;
import com.ybsx.dao.query.OrderQuery;
import com.ybsx.entity.LastTrailOrder;
import com.ybsx.entity.FirstTrailOrder;
import com.ybsx.entity.OrderVO;
import com.ybsx.entity.PositiveTrailOrder;

/**
 * 投诉综合 mapper
 * @author zhouKai
 * @createDate 2018年4月19日 上午11:19:20
 */
public interface ComplaintMapper {
	

	/**
	 * 查询初审状态的工单，分页查询
	 * @param query
	 * @return
	 */
	List<FirstTrailOrder> selectFirstTrailOrder(BaseQuery query);

	/**
	 * 初审状态的工单的个数
	 * @param query
	 * @return
	 */
	Integer selectFirstTrailOrderCnt(BaseQuery query);

	/**
	 * 查询正审状态的工单，分页查询
	 * @param query
	 * @return
	 */
	List<PositiveTrailOrder> selectPositiveTrailOrder(BaseQuery query);

	/**
	 * 正审状态工单的个数
	 * @param query
	 * @return
	 */
	Integer selectPositiveTrailOrderCnt(BaseQuery query);

	List<LastTrailOrder> selectLastTrailOrder(BaseQuery query);

	Integer selectLastTrailOrderCnt(BaseQuery query);
	
	/**
	 * 查询用户投诉工单
	 * @param query
	 * @return
	 */
	List<OrderVO> selectOrderVOs(OrderQuery query);

	/**
	 * 查询用户投诉单的个数
	 * @param query
	 * @return
	 */
	Integer selectOrderVOCnt(OrderQuery query);

	/**
	 * 根据工单ID查询工单信息
	 * @param orderId
	 * @return
	 */
	OrderVO selectOrderVO(Integer orderId);

	
	
	
	
	
	
	
	
}