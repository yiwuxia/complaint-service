package com.ybsx.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * spring工具类
 * @author zhouKai
 * @createDate 2018年1月29日 下午2:53:22
 */
@Component
public class SpringUtil implements ServletContextListener {

	private Logger logger = Logger.getLogger(getClass());
	
	// spring容器
	public static WebApplicationContext springContext;
	public static SpringUtil _this;
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("SpringUtil.contextInitialized()");
		springContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		_this = this;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("SpringUtil.contextDestroyed()");
	}

}
