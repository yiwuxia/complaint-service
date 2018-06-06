package com.ybsx.base.aspect;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 对controller层的所有public方法进行日志记录 添加类/接口功能描述
 * @author zhouKai
 * @createDate 2017年11月6日 下午2:42:44
 */
@Aspect
@Component
public class RequestLogAspect {
	
	/**
	 * 日志实例
	 */
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * 开始时间
	 */
	private ThreadLocal<Long> startTime = new ThreadLocal<>();
	// 保存每次请求的参数在其线程中
	public static ThreadLocal<String> requestParams = new ThreadLocal<>();

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 拦截请求方法
	 * @author zhoukai
	 */
	@Pointcut(value = "execution(public * com.ybsx.controller..*.*(..))")
	public void requestLog() {

	}

	/**
	 * 请求拦截之前处理方法
	 * @param joinPoint JoinPoint
	 * @author zhoukai
	 */
	@Before(value = "requestLog()")
	public void doBefore(JoinPoint joinPoint) {
		this.startTime.set(System.currentTimeMillis());
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		logger.info("\n");
		logger.info("URL: " + request.getRequestURL().toString());
		logger.info("request method: " + request.getMethod());
		logger.info("remote address: " + request.getRemoteAddr());
		logger.info("remote port: " + request.getRemotePort());
		Signature signature = joinPoint.getSignature();
		logger.info("CLASS_METHOD: " + signature.getDeclaringTypeName() + "." + signature.getName());
		String params = Arrays.toString(joinPoint.getArgs());
		requestParams.set(params);
		logger.info("args: " + params);
	}

	/**
	* 请求拦截后处理方法
	* @param result Object
	* @author zhoukai
	*/
	@AfterReturning(value = "requestLog()", returning = "result")
	public void doAfterReturning(Object result) {
		try {
			logger.info("response json format: " + objectMapper.writeValueAsString(result));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logger.info("spend time: " + (System.currentTimeMillis() - this.startTime.get()));
		logger.info("\n");
	}

	
}
