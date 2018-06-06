package com.ybsx.controller.tool;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ybsx.base.ResultBody;
import com.ybsx.base.yml.YmlConfig;

/**
 * 
 * 课程控制器
 * @author zhouKai
 * @createDate 2017年12月4日 下午5:43:57
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {

	@Autowired
	private YmlConfig ymlConfig;
	
	@Autowired
	private MultipartProperties multipartProperties;
	
	@Autowired
	private ServletContext context;

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResultBody<?> get1() {
		System.out.println(ymlConfig.active);
		return new ResultBody<>(multipartProperties);
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResultBody<?> upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Part part = request.getPart("file");
		long start = System.currentTimeMillis();
		return new ResultBody<>(System.currentTimeMillis() - start);
	}
	
	
	
	
	
	
	
	
	
	
	
}
