package com.ybsx.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.wf.etp.authz.ApiInterceptor;
import com.ybsx.base.interceptor.CorsInterceptor;
import com.ybsx.base.permi.EtpCache;
import com.ybsx.base.permi.UserRealm;

/**
 * 对客户端请求进行拦截，允许其跨域请求
 * @author zhouKai
 * @createDate 2017年11月6日 下午2:42:44
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private UserRealm userRealm;

	@Autowired
	private EtpCache etpCache;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CorsInterceptor()).addPathPatterns("/**");

		ApiInterceptor interceptor = new ApiInterceptor();
		// 设置realm
		interceptor.setUserRealm(userRealm);
		// 设置缓存
		interceptor.setCache(etpCache);
		// 生成token的key,可以不写默认是'e-t-p' 
		interceptor.setTokenKey("token_");
		// 注册拦截器
		InterceptorRegistration ir = registry.addInterceptor(interceptor);
		// 配置拦截的路径
		ir.addPathPatterns("/manage/**");
		// 配置不拦截的路径, 排除登录、获取图像验证码
		ir.excludePathPatterns("/manage/login", "/manage/image/captcha");

	}

}
