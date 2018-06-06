package com.ybsx.controller.tool;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ybsx.base.ResultBody;
import com.ybsx.util.sms.SmsUtil;

/**
 * 短信控制器
 * @author zhouKai
 * @createDate 2017年12月4日 下午5:43:57
 */
@RestController
@RequestMapping(value = "/sms")
public class SmsController {
	
	/**
	 * 发送短信验证码
	 * "request ip is"+httpServletRequest.getHeader("x-real-ip")
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendVerifyCode", method = RequestMethod.GET)
	public ResultBody<?> sendVerifyCode(String mobile, HttpServletRequest request) throws Exception {
		String realIp = request.getHeader("x-real-ip");
		SmsUtil.sendVerifyCode(realIp, mobile);
		return new ResultBody<>();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
