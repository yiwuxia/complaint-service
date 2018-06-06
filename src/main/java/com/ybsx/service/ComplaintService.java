package com.ybsx.service;

import java.util.List;

import com.ybsx.base.TableBody;
import com.ybsx.dao.model.ComplaintTrailFirst;
import com.ybsx.dao.model.ComplaintTrailLast;
import com.ybsx.dao.model.ComplaintTrailPositive;
import com.ybsx.dao.query.BaseQuery;
import com.ybsx.dao.query.OrderQuery;
import com.ybsx.entity.AddOrder;
import com.ybsx.entity.FirstTrailOrder;
import com.ybsx.entity.LastTrailOrder;
import com.ybsx.entity.OrderVO;
import com.ybsx.entity.PositiveTrailOrder;

/**
 * 投诉业务接口
 * @author zhouKai
 * @createDate 2018年4月19日 上午11:30:46
 */
public interface ComplaintService {

	void save(AddOrder addOrder);

	TableBody<List<FirstTrailOrder>> listFirstTrail(BaseQuery query);

	void change(AddOrder order);

	void firstTrail(ComplaintTrailFirst first);

	TableBody<List<PositiveTrailOrder>> listPositiveTrail(BaseQuery query);

	void positiveTrail(ComplaintTrailPositive positive);

	TableBody<List<LastTrailOrder>> listLastTrail(BaseQuery query);

	void lastTrail(ComplaintTrailLast trailEnd);

	TableBody<List<OrderVO>> listOrder(OrderQuery query);
	
	void cancel(Integer orderId);

	OrderVO getOrderVO(Integer orderId);

}
