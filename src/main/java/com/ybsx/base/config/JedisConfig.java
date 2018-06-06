package com.ybsx.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ybsx.base.yml.YmlConfig;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置
 * @author zhouKai
 * @createDate 2018年5月7日 下午5:09:17
 */
@Configuration
public class JedisConfig {


	@Autowired
	private YmlConfig ymlConfig;
	
    /**
     * redis缓冲池
     */
    @Bean
    public JedisPool getJedisPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(100);
		JedisPool jedisPool = new JedisPool(config, ymlConfig.redis.host, ymlConfig.redis.port);
		return jedisPool;
	}
    
}
