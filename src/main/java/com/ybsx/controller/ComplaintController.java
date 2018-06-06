package com.ybsx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ybsx.base.ResultBody;
import com.ybsx.base.yml.YmlConfig;
import com.ybsx.dao.model.ComplaintTrailFirst;
import com.ybsx.dao.model.ComplaintTrailLast;
import com.ybsx.dao.model.ComplaintTrailPositive;
import com.ybsx.dao.query.BaseQuery;
import com.ybsx.dao.query.OrderQuery;
import com.ybsx.entity.AddOrder;
import com.ybsx.service.ComplaintService;	


/**
 * 投诉控制器
 * @author zhouKai
 * @createDate 2018年4月19日 上午11:28:09
 */
@RestController
@RequestMapping(value = "/complaint")
public class ComplaintController {

	@Autowired
	private YmlConfig ymlConfig;

	@Autowired
	private ComplaintService complaintService;
	
	/**
	 * 添加投诉信息
	 * @param addOrder
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResultBody<?> add(@RequestBody AddOrder addOrder) {
		complaintService.save(addOrder);
		return new ResultBody<>();
	}
	
	/**
	 * 修改投诉
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public ResultBody<?> change(@RequestBody AddOrder order) {
		complaintService.change(order);
		return new ResultBody<>();
	}

	/**
	 * 用户取消工单
	 * @param orderId 工单ID
	 * @return
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public ResultBody<?> cancel(Integer orderId) {
		complaintService.cancel(orderId);
		return new ResultBody<>();
	}

	/**
	 * 获取工单信息
	 * @param orderId 工单ID
	 * @return
	 */
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	public ResultBody<?> order(Integer orderId) {
		return new ResultBody<>(complaintService.getOrderVO(orderId));
	}
	
	/**
	 * 用户工单分页查询
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/listOrder", method = RequestMethod.POST)
	public ResultBody<?> listOrder(OrderQuery query) {
		return complaintService.listOrder(query);
	}
	
	/**
	 * 初审列表
	 * @param query 查询条件
	 * @return
	 */
	@RequestMapping(value = "/listFirstTrail", method = RequestMethod.GET)
	public ResultBody<?> listFirstTrail(BaseQuery query) {
		return complaintService.listFirstTrail(query);
	}
	
	
	/**
	 * 对工单进行初审
	 * @param first 
	 * @return
	 */
	@RequestMapping(value = "/firstTrail", method = RequestMethod.POST)
	public ResultBody<?> firstTrail(@RequestBody ComplaintTrailFirst first) {
		complaintService.firstTrail(first);
		return new ResultBody<>();
	}

	/**
	 * 正审列表
	 * @param query 查询条件
	 * @return
	 */
	@RequestMapping(value = "/listPositiveTrail", method = RequestMethod.GET)
	public ResultBody<?> listPositiveTrail(BaseQuery query) {
		return complaintService.listPositiveTrail(query);
	}

	/**
	 * 正审
	 * @param positive 
	 * @return
	 */
	@RequestMapping(value = "/positiveTrail", method = RequestMethod.POST)
	public ResultBody<?> positiveTrail(@RequestBody ComplaintTrailPositive positive) {
		complaintService.positiveTrail(positive);
		return new ResultBody<>();
	}

	/**
	 * 最终处理状态工单列表
	 * @param query 查询条件
	 * @return
	 */
	@RequestMapping(value = "/listLastTrail", method = RequestMethod.GET)
	public ResultBody<?> listLastTrail(BaseQuery query) {
		return complaintService.listLastTrail(query);
	}

	/**
	 * 最终处理
	 * @param positive 
	 * @return
	 */
	@RequestMapping(value = "/lastTrail", method = RequestMethod.POST)
	public ResultBody<?> lastTrail(@RequestBody ComplaintTrailLast trailEnd) {
		complaintService.lastTrail(trailEnd);
		return new ResultBody<>();
	}
	
	
	
	
	
	
	
	
}
