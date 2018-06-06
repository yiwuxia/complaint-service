package com.ybsx.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wf.etp.authz.exception.EtpException;
import com.ybsx.base.aspect.RequestLogAspect;
import com.ybsx.base.exception.ServiceException;
import com.ybsx.base.state.ExceptionState;

/**
 * 对controller层添加统一异常处理
 * @author zhouKai
 * @createDate 2017年11月6日 下午2:42:44
 */
@RestControllerAdvice
public class GlobalErrorHandler {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@ExceptionHandler(value = Throwable.class)
	public ResultBody<Object> handle(HttpServletRequest req, Throwable error) {
		ResultBody<Object> result = null;
		String params = RequestLogAspect.requestParams.get();
		if (params == null) { // 未进入RequestLogAspect就发送了异常
			StringBuilder sb = new StringBuilder();
			String contentType = req.getContentType();
			if (contentType != null && contentType.contains("application/json")) { // json请求
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
					String line = null;
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
				} catch (IOException e) {
					this.logger.error(null, error);
				}
			} else {
				req.getParameterMap().forEach((k, v) -> {
					sb.append(k + "=" + Arrays.toString(v) + ", ");
				});
			}			
			params = sb.toString();
		}
		this.logger.error("getRequestURI = " + req.getRequestURI());
		this.logger.error("params = " + params);
		this.logger.error(null, error);

		// 处理spring系统的异常 EtpException
		if (error instanceof MethodArgumentTypeMismatchException) {
			result = new ResultBody<>(ExceptionState.General.METHOD_ARGUMENT_TYPE_MISMATCH);
		} else if (error instanceof EtpException) {
			result = new ResultBody<>(ExceptionState.Permission.ERROR);
		}
		if (result != null) {
			try {
				logger.error("response json format: " + objectMapper.writeValueAsString(result));
			} catch (JsonProcessingException e) {
			}
			return result;
		}

		// 处理自定义异常
		Throwable rootCause = error;
		while (rootCause.getCause() != null) {
			rootCause = rootCause.getCause();
		}
		if (rootCause instanceof ServiceException) { // 是自定义的业务异常
			ServiceException se = (ServiceException) rootCause;
			return new ResultBody<>(se.getState());
		} else {
			result = new ResultBody<>(ExceptionState.General.UNKNOWN_FAIL);
			result.setMessage(rootCause.toString());
			try {
				logger.error("response json format: " + objectMapper.writeValueAsString(result));
			} catch (JsonProcessingException e) {
			}
			return result;
		}
	}

}
