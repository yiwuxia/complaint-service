package com.ybsx.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ybsx.base.TableBody;
import com.ybsx.base.exception.ServiceException;
import com.ybsx.base.state.ExceptionState;
import com.ybsx.base.state.ExceptionState.OrderState;
import com.ybsx.base.state.ExceptionState.SmsState;
import com.ybsx.dao.mapper.master.ComplaintAttachmentMapper;
import com.ybsx.dao.mapper.master.ComplaintMapper;
import com.ybsx.dao.mapper.master.ComplaintOrderMapper;
import com.ybsx.dao.mapper.master.ComplaintTrailFirstMapper;
import com.ybsx.dao.mapper.master.ComplaintTrailLastMapper;
import com.ybsx.dao.mapper.master.ComplaintTrailPositiveMapper;
import com.ybsx.dao.model.ComplaintAttachment;
import com.ybsx.dao.model.ComplaintOrder;
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
import com.ybsx.service.ComplaintService;
import com.ybsx.util.SeaweedUtil;
import com.ybsx.util.sms.SmsUtil;

/**
 * 投诉业务实现
 * @author zhouKai
 * @createDate 2018年4月19日 上午11:30:35
 */
@Service
public class ComplaintServiceImpl implements ComplaintService {

	// 投诉综合 mapper
	@Autowired
	private ComplaintMapper complaintMapper;

	// 投诉工单 mapper
	@Autowired
	private ComplaintOrderMapper complaintOrderMapper;

	// 投诉初审 mapper
	@Autowired
	private ComplaintTrailFirstMapper complaintTrailFirstMapper;

	// 投诉附件 mapper
	@Autowired
	private ComplaintAttachmentMapper complaintAttachmentMapper;

	// 投诉正审  mapper
	@Autowired
	private ComplaintTrailPositiveMapper complaintTrailPositiveMapper;

	// 投诉工单的最终处理结果  mapper
	@Autowired
	private ComplaintTrailLastMapper complaintTrailLastMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(AddOrder addOrder) {
		ComplaintOrder order = addOrder.getOrder();
		Boolean right = SmsUtil.check(order.getMobile(), addOrder.getVerifyCode());
		if (!right) {
			throw new ServiceException(SmsState.CODE_ERROR);
		}
		Integer cnt = complaintOrderMapper.selectLast3monthCnt(order.getUid(), order.getRoom());
		if (cnt > 0) { // 同一房间，3月之内只能投诉一次
			throw new ServiceException(ExceptionState.OrderState.COMPLAINT_FORBIDDEN);
		}
		
		Date date = new Date();
		order.setCreateTime(date);
		order.setUpdateTime(date);
		// 1. 保存投诉工单
		complaintOrderMapper.insertSelective(order);
		// 获取自增主键
		Integer orderId = order.getId();

		// 2. 保存工单附件
		this.saveAttachments(addOrder.getAddresses(), date, orderId);
		// 3. 保存工单初审记录
		ComplaintTrailFirst trailFirst = new ComplaintTrailFirst();
		trailFirst.setCreateTime(date);
		trailFirst.setUpdateTime(date);
		trailFirst.setId(orderId);
		trailFirst.setState(ComplaintTrailFirst.State.WAIT_FOR_TRAIL);
		complaintTrailFirstMapper.insertSelective(trailFirst);
	}

	/**
	 * @param addOrder
	 * @param date
	 * @param orderId
	 */
	@Transactional(rollbackFor = Exception.class)
	private void saveAttachments(List<String> addresses, Date date, Integer orderId) {
		ComplaintAttachment attachment = new ComplaintAttachment();
		attachment.setCreateTime(date);
		attachment.setUpdateTime(date);
		attachment.setOrderId(orderId);
		for (String add : addresses) {
			attachment.setAddress(add);
			complaintAttachmentMapper.insertSelective(attachment);
		}
	}

	@Override
	public TableBody<List<FirstTrailOrder>> listFirstTrail(BaseQuery query) {
		List<FirstTrailOrder> orders = complaintMapper.selectFirstTrailOrder(query);
		List<Integer> orderIds = orders.stream().map(ComplaintOrder::getId).collect(Collectors.toList());
		if (orderIds.size() > 0) {
			Map<Integer, List<ComplaintAttachment>> map = complaintAttachmentMapper.select(orderIds).stream()
					.collect(Collectors.groupingBy(ComplaintAttachment::getOrderId));

			for (FirstTrailOrder order : orders) {
				List<String> addresses = map.get(order.getId())
											.stream()
											.map(ComplaintAttachment::getAddress)
											.collect(Collectors.toList());
				order.setAddresses(addresses);
			}
		}

		TableBody<List<FirstTrailOrder>> result = new TableBody<>();
		result.setData(orders);
		result.setCount(complaintMapper.selectFirstTrailOrderCnt(query));
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void change(AddOrder addOrder) {
		ComplaintOrder order = addOrder.getOrder();
		Boolean right = SmsUtil.check(order.getMobile(), addOrder.getVerifyCode());
		if (!right) {
			throw new ServiceException(SmsState.CODE_ERROR);
		}

		Integer orderId = order.getId();
		Date date = new Date();
		ComplaintOrder oldOrder = complaintOrderMapper.selectByPrimaryKey(orderId);
		List<String> addresses = addOrder.getAddresses();
		switch (oldOrder.getTrailState()) {
		case ComplaintOrder.TrailState.FIRST: // 初审状态可以修改全部字段
			order.setUpdateTime(date);
			// 1. 更新工单
			complaintOrderMapper.updateByPrimaryKeySelective(order);
			// 2. 删除原有全部附件，插入新附件
			updateAllAttachment(orderId, date, addresses);
			break;
		case ComplaintOrder.TrailState.POSITIVE: // 正审状态只能修改投诉内容和附件
			oldOrder.setUpdateTime(date);
			oldOrder.setContent(order.getContent());
			// 1. 更新工单
			complaintOrderMapper.updateByPrimaryKeySelective(oldOrder);
			// 2. 删除原有全部附件，插入新附件
			updateAllAttachment(orderId, date, addresses);
			break;
		default:
			throw new ServiceException(OrderState.CHANGE_FAIL);
		}
	}

	/**
	 * 根据工单ID更新全部附件
	 * @param orderId 投诉工单ID
	 * @param date 修改日期
	 * @param addresses 新附件地址
	 */
	private void updateAllAttachment(Integer orderId, Date date, List<String> addresses) {
		List<ComplaintAttachment> attachments = complaintAttachmentMapper.select(Arrays.asList(orderId));
		attachments.stream().map(ComplaintAttachment::getAddress)
			.filter(a -> !addresses.contains(a)) // 过滤出不存在的地址
			.map(a -> { // 得到文件id
			return a.substring(a.lastIndexOf("/") + 1);
		}).forEach(fid -> SeaweedUtil.deleteByFid(fid));
		
		complaintAttachmentMapper.delete(orderId);
		this.saveAttachments(addresses, date, orderId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void firstTrail(ComplaintTrailFirst first) {
		Date date = new Date();
		first.setUpdateTime(date);
		complaintTrailFirstMapper.updateByPrimaryKeySelective(first);

		Integer orderId = first.getId();
		String state = first.getState();
		if (ComplaintTrailFirst.State.PASSED.equals(state)) { // 初审通过，进入正审
			// 1. 修改工单状态为正审
			ComplaintOrder order = new ComplaintOrder();
			order.setUpdateTime(date);
			order.setId(orderId);
			order.setTrailState(ComplaintOrder.TrailState.POSITIVE);
			complaintOrderMapper.updateByPrimaryKeySelective(order);
			// 2. 插入正审记录
			ComplaintTrailPositive positive = new ComplaintTrailPositive();
			positive.setCreateTime(date);
			positive.setUpdateTime(date);
			positive.setId(orderId);
			positive.setState(ComplaintTrailPositive.State.WAIT_FOR_TRAIL);
			complaintTrailPositiveMapper.insertSelective(positive);
		} else if (ComplaintTrailFirst.State.REFUSED.equals(state)) { // 初审被拒绝，结束工单，设置结束原因
			ComplaintOrder order = new ComplaintOrder();
			order.setId(orderId);
			order.setUpdateTime(date);
			order.setTrailState(ComplaintOrder.TrailState.END);
			order.setEndReason(ComplaintOrder.EndReason.FIRST_TRAIL_REFUSE);
			complaintOrderMapper.updateByPrimaryKeySelective(order);
		}
	}

	@Override
	public TableBody<List<PositiveTrailOrder>> listPositiveTrail(BaseQuery query) {
		List<PositiveTrailOrder> orders = complaintMapper.selectPositiveTrailOrder(query);
		List<Integer> orderIds = orders.stream().map(ComplaintOrder::getId).collect(Collectors.toList());
		if (orderIds.size() > 0) {
			Map<Integer, List<ComplaintAttachment>> map = complaintAttachmentMapper.select(orderIds).stream()
					.collect(Collectors.groupingBy(ComplaintAttachment::getOrderId));
			for (PositiveTrailOrder order : orders) {
				order.setAttachments(map.get(order.getId()));
			}
		}

		TableBody<List<PositiveTrailOrder>> result = new TableBody<>();
		result.setData(orders);
		result.setCount(complaintMapper.selectPositiveTrailOrderCnt(query));
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void positiveTrail(ComplaintTrailPositive positive) {
		Date date = new Date();
		positive.setUpdateTime(date);
		complaintTrailPositiveMapper.updateByPrimaryKeySelective(positive);
		
		if (ComplaintTrailPositive.State.PASSED.equals(positive.getState())) {
			// 通过正审进入最后处理环节
			Integer orderId = positive.getId();
			ComplaintOrder order = new ComplaintOrder();
			order.setUpdateTime(date);
			order.setId(orderId);
			order.setTrailState(ComplaintOrder.TrailState.LAST);
			complaintOrderMapper.updateByPrimaryKeySelective(order);

			this.insertComplaintTrailLast(orderId, date, "", ComplaintTrailLast.State.WAIT_FOR_TRAIL);
		}
	}

	/**
	 * 出入工单的最终处理
	 * @param orderId 工单ID
	 * @param date 插入时间
	 * @param comment 备注
	 * @param state 状态
	 */
	@Transactional(rollbackFor = Exception.class)
	private void insertComplaintTrailLast(Integer orderId, Date date, String comment, String state) {
		ComplaintTrailLast trailEnd = new ComplaintTrailLast();
		trailEnd.setCreateTime(date);
		trailEnd.setUpdateTime(date);
		trailEnd.setState(state);
		trailEnd.setId(orderId);
		trailEnd.setComment(comment);
		complaintTrailLastMapper.insertSelective(trailEnd);
	}

	@Override
	public TableBody<List<LastTrailOrder>> listLastTrail(BaseQuery query) {
		List<LastTrailOrder> orders = complaintMapper.selectLastTrailOrder(query);
		List<Integer> orderIds = orders.stream().map(ComplaintOrder::getId).collect(Collectors.toList());
		if (orderIds.size() > 0) {
			Map<Integer, List<ComplaintAttachment>> map = complaintAttachmentMapper.select(orderIds).stream()
					.collect(Collectors.groupingBy(ComplaintAttachment::getOrderId));
			for (LastTrailOrder order : orders) {
				order.setAttachments(map.get(order.getId()));
			}
		}

		TableBody<List<LastTrailOrder>> result = new TableBody<>();
		result.setData(orders);
		result.setCount(complaintMapper.selectLastTrailOrderCnt(query));
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void lastTrail(ComplaintTrailLast trailEnd) {
		Date date = new Date();
		trailEnd.setUpdateTime(date);
		complaintTrailLastMapper.updateByPrimaryKeySelective(trailEnd);

		ComplaintOrder order = new ComplaintOrder();
		order.setUpdateTime(date);
		order.setId(trailEnd.getId());
		order.setTrailState(ComplaintOrder.TrailState.END);
		order.setEndReason(ComplaintOrder.EndReason.NORMAL);
		complaintOrderMapper.updateByPrimaryKeySelective(order);
	}

	@Override
	public TableBody<List<OrderVO>> listOrder(OrderQuery query) {
		List<OrderVO> orders = complaintMapper.selectOrderVOs(query);
		List<Integer> orderIds = orders.stream().map(ComplaintOrder::getId).collect(Collectors.toList());
		if (orderIds.size() > 0) {
			Map<Integer, List<ComplaintAttachment>> map = complaintAttachmentMapper.select(orderIds).stream()
					.collect(Collectors.groupingBy(ComplaintAttachment::getOrderId));
			for (OrderVO order : orders) {
				order.setAttachments(map.get(order.getId()));
			}
		}

		TableBody<List<OrderVO>> result = new TableBody<>();
		result.setData(orders);
		result.setCount(complaintMapper.selectOrderVOCnt(query));
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void cancel(Integer orderId) {
		ComplaintOrder order = complaintOrderMapper.selectByPrimaryKey(orderId);
		Date date = new Date();
		String comment = "用户取消投诉";
		switch (order.getTrailState()) {
		case ComplaintOrder.TrailState.FIRST: // 初审
			// 1. 修改初审状态为通过，且添加备注
			ComplaintTrailFirst trailFirst = new ComplaintTrailFirst();
			trailFirst.setId(orderId);
			trailFirst.setUpdateTime(date);
			trailFirst.setComment(comment);
			trailFirst.setState(ComplaintTrailFirst.State.PASSED);
			complaintTrailFirstMapper.updateByPrimaryKeySelective(trailFirst);
			// 2. 直接进入最终处理，且状态为处理结束
			this.insertComplaintTrailLast(orderId, date, comment, ComplaintTrailLast.State.ENDED);
			break;
		case ComplaintOrder.TrailState.POSITIVE: // 正审
			// 1. 修改正审状态为通过，且添加备注
			ComplaintTrailPositive positive = new ComplaintTrailPositive();
			positive.setUpdateTime(date);
			positive.setId(orderId);
			positive.setComment(comment);
			positive.setState(ComplaintTrailPositive.State.PASSED);
			complaintTrailPositiveMapper.updateByPrimaryKeySelective(positive);
			// 2. 直接进入最终处理，且状态为处理结束
			this.insertComplaintTrailLast(orderId, date, comment, ComplaintTrailLast.State.ENDED);
			break;
		case ComplaintOrder.TrailState.END: // 最终处理
			// 1. 修改最终处理结果的状态为已结束，且添加备注
			ComplaintTrailLast trailEnd = new ComplaintTrailLast();
			trailEnd.setUpdateTime(date);
			trailEnd.setId(orderId);
			trailEnd.setComment(comment);
			trailEnd.setState(ComplaintTrailLast.State.ENDED);
			complaintTrailLastMapper.updateByPrimaryKeySelective(trailEnd);
			break;
		default:
			break;
		}
		// 3. 修改工单的处理状态为已结束
		order.setUpdateTime(date);
		order.setTrailState(ComplaintOrder.TrailState.END);
		order.setEndReason(ComplaintOrder.EndReason.CUSTOMER_CANCEL);
		complaintOrderMapper.updateByPrimaryKeySelective(order);
	}

	@Override
	public OrderVO getOrderVO(Integer orderId) {
		OrderVO order = complaintMapper.selectOrderVO(orderId);
		List<ComplaintAttachment> attachments = complaintAttachmentMapper.select(Arrays.asList(order.getId()));
		order.setAttachments(attachments);
		return order;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
