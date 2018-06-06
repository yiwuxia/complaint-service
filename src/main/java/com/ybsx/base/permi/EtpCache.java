package com.ybsx.base.permi;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wf.etp.authz.IEtpCache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 权限缓存
 * @author zhouKai
 * @createDate 2018年4月9日 下午5:28:44
 */
@Component
public class EtpCache extends IEtpCache {

	@Autowired
	private JedisPool jedisPool;
	
	public static final int etpIndex = 5;

	

	@Override
	public List<String> getSet(String key) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.select(etpIndex);
			return jedis.lrange(key, 0, -1);
		}
	}

	@Override
	public boolean putSet(String key, Set<String> values) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.select(etpIndex);
			String[] strings = new String[values.size()];
			values.toArray(strings);
			Long cnt = jedis.rpush(key, strings);
			return cnt > 0;
		}
	}

	@Override
	public boolean removeSet(String key, String value) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.select(etpIndex);
			return jedis.lrem(key, 1, value) > 0;
		}
	}

	@Override
	public boolean delete(String key) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.select(etpIndex);
			return jedis.del(key) > 0;
		}
	}

	@Override
	public boolean delete(Collection<String> keys) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.select(etpIndex);
			String[] arrKey = new String[keys.size()];
			keys.toArray(arrKey);
			return jedis.del(arrKey) > 0;
		}
	}

	@Override
	public Set<String> keys(String pattern) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.select(etpIndex);
			return jedis.keys(pattern);
		}
	}

}
