package com.ybsx.dao.mapper.master;

/**
 * 基础mapper
 * @author zhouKai
 * @createDate 2017年12月4日 下午5:48:09
 */
public interface BaseMapper<T> {

	int deleteByPrimaryKey(Integer id);

	int insert(T record);

	int insertSelective(T record);

	T selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(T record);

	int updateByPrimaryKey(T record);

}
