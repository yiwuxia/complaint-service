package com.ybsx.base.yml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 对应配置文件 *.yml
 * 
 * @author zhouKai
 * @createDate 2018年1月23日 下午3:32:10
 */
@Component
@ConfigurationProperties(prefix = "myYml")
public class YmlConfig {

	public static YmlConfig _this;

	// <-- 以下变量用于测试
	public String simpleProp;
	public String[] arrayProps;
	public List<Map<String, String>> listProp1 = new ArrayList<>(); // 接收prop1里面的属性值
	public List<String> listProp2 = new ArrayList<>(); // 接收prop2里面的属性值
	public Map<String, String> mapProps = new HashMap<>(); // 接收prop1里面的属性值
	// -->

	// 数据源
	public DS datasource;
	// 海草配置信息
	public Seaweed seaweed;

	public String tempDir;
	// 发送短信验证码的地址
	public String smsUrl;

	// redis配置信息
	public Redis redis;
	// 运行环境，prod：生产环境 ，dev：开发环境，test：测试环境
	@Value(value = "${spring.profiles.active}")
	public String active;

	public String getSimpleProp() {
		return simpleProp;
	}

	public void setSimpleProp(String simpleProp) {
		this.simpleProp = simpleProp;
	}

	public String[] getArrayProps() {
		return arrayProps;
	}

	public void setArrayProps(String[] arrayProps) {
		this.arrayProps = arrayProps;
	}

	public List<Map<String, String>> getListProp1() {
		return listProp1;
	}

	public void setListProp1(List<Map<String, String>> listProp1) {
		this.listProp1 = listProp1;
	}

	public List<String> getListProp2() {
		return listProp2;
	}

	public void setListProp2(List<String> listProp2) {
		this.listProp2 = listProp2;
	}

	public Map<String, String> getMapProps() {
		return mapProps;
	}

	public void setMapProps(Map<String, String> mapProps) {
		this.mapProps = mapProps;
	}

	public DS getDatasource() {
		return datasource;
	}

	public void setDatasource(DS datasource) {
		this.datasource = datasource;
	}

	public Seaweed getSeaweed() {
		return seaweed;
	}

	public void setSeaweed(Seaweed seaweed) {
		this.seaweed = seaweed;
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getSmsUrl() {
		return smsUrl;
	}

	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}

	public Redis getRedis() {
		return redis;
	}

	public void setRedis(Redis redis) {
		this.redis = redis;
	}

}