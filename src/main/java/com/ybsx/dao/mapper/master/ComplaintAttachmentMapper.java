package com.ybsx.dao.mapper.master;

import java.util.List;

import com.ybsx.dao.model.ComplaintAttachment;

/**
 * 投诉工单附件
 * @author zhouKai
 * @createDate 2018年4月19日 上午11:19:20
 */
public interface ComplaintAttachmentMapper extends BaseMapper<ComplaintAttachment> {

    /**
     * 
     * @param orderIds 工单ID集合
     * @return
     */
	List<ComplaintAttachment> select(List<Integer> orderIds);
	
	/**
	 * 根据工单ID删除
	 * @param orderId 工单ID
	 * @return
	 */
	int delete(int orderId);
	
    
}