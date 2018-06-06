package com.ybsx.util.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybsx.base.exception.ServiceException;
import com.ybsx.base.state.ExceptionState.SmsState;
import com.ybsx.base.yml.YmlConfig;
import com.ybsx.util.UrlUtil;

/**
 * 手机短信工具类
 * @author zhouKai
 * @createDate 2018年4月24日 下午4:59:27
 */
public class SmsUtil {

	private static Logger logger = Logger.getLogger(SmsUtil.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	// 记录发送的验证码
	private static Map<String, SendCode> codeCache = new HashMap<>();
	private static ReadWriteLock codeReadWriteLock = new ReentrantReadWriteLock();
	private static Lock codeReadLock = codeReadWriteLock.readLock();
	private static Lock codeWriteLock = codeReadWriteLock.writeLock();
	
	// 记录ip对验证码的请求
	private static Map<String, IPRecord> ipCache = new HashMap<>();
	private static ReadWriteLock ipReadWriteLock = new ReentrantReadWriteLock();
	// 读锁
	private static Lock ipReadLock = ipReadWriteLock.readLock();
	// 写锁
	private static Lock ipWriteLock = ipReadWriteLock.writeLock();
	
	// 验证码过期时间，30分钟
	private static long expireTime = TimeUnit.MINUTES.toMillis(30);
	// 同一IP地址请求发送短信的次数限制
	private static int limitCnt;
	
	
	static {
		if (YmlConfig._this.active.equals("prod")) { // 生产环境最多发送3此
			limitCnt = 3;
		} else { // 非生产环境，最多发送100次
			limitCnt = 100;
		}
		
		// 定时清理 codeCache		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				codeWriteLock.lock();
				try {
					logger.info(codeCache);
					Iterator<SendCode> iterator = codeCache.values().iterator();
					iterator.forEachRemaining(sc -> {
						if (System.currentTimeMillis() - sc.time > expireTime ) {
							iterator.remove();
						}
					});
				} finally {
					codeWriteLock.unlock();
				}
			}
		}, TimeUnit.HOURS.toMillis(1), TimeUnit.DAYS.toMillis(1));
		
		// 定时清理 ipCache
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				ipWriteLock.lock();
				try {
					logger.info(ipCache);
					Iterator<IPRecord> iterator = ipCache.values().iterator();
					iterator.forEachRemaining(record -> {
						record.times.removeIf(sendTime -> { // 一天前发送
							return System.currentTimeMillis() - sendTime > TimeUnit.DAYS.toMillis(1);
						});
						if (record.times.size() == 0) {
							iterator.remove();
						}
					});
				} finally {
					ipWriteLock.unlock();
				}
			}
		}, TimeUnit.MINUTES.toMillis(10), TimeUnit.DAYS.toMillis(1));
	}

	/**
	 * 发送验证码
	 * @param mobile
	 * @throws IOException
	 */
	public static void sendVerifyCode(String ip, String mobile) throws IOException {
		logger.info("ip:" + ip + ", mobile:" + mobile);
		beforeSend(ip);
		
		Map<String, String> map = new HashMap<>();
		map.put("phone", mobile);
		String code = String.format("%06d", new Random().nextInt(1000 * 1000));
		map.put("code", code);
		String json = objectMapper.writeValueAsString(map);
		String result = UrlUtil.doPostJson(YmlConfig._this.smsUrl, json);
		Response response = objectMapper.readValue(result, Response.class);
		if (!response.getSuccess()) {
			throw new ServiceException(SmsState.SEND_FAIL);
		}

		afterSend(mobile, code, ip);
	}
	
	/**
	 * 发送前验证ip是否超过验证码的请求次数，3次
	 * @param mobile 手机号
	 * @param code  验证码
	 * @param ip IP地址
	 */
	private static void afterSend(String mobile, String code, String ip) {
		codeWriteLock.lock();
		try {
			codeCache.put(mobile, new SendCode(code, System.currentTimeMillis()));
		} finally {
			codeWriteLock.unlock();
		}
		
		ipWriteLock.lock();
		try {
			IPRecord record = ipCache.get(ip);
			if (record == null) {
				record = new IPRecord();
				List<Long> times = new ArrayList<>();
				times.add(System.currentTimeMillis());
				record.times = times;
				ipCache.put(ip, record);
			} else {
				record.times.add(System.currentTimeMillis());
			}
		} finally {
			ipWriteLock.unlock();
		}
	}

	/**
	 * 发送验证码前，判断是否超出请求次数限制
	 * @param ip
	 */
	private static void beforeSend(String ip) {
		ipReadLock.lock();
		try {
			IPRecord record = ipCache.get(ip);
			if (record != null && record.times.size() >= limitCnt ) { // 超出发送次数限制
				throw new ServiceException(SmsState.OVER_LIMIT);
			}
		} finally {
			ipReadLock.unlock();
		}
	}

	/**
	 * 判断手机号和验证码是否匹配
	 * @param mobile 手机号
	 * @param code 验证码
	 * @return
	 */
	public static Boolean check(String mobile, String code) {
		codeWriteLock.lock();
		try {
//			return true;
			SendCode sendCode = codeCache.get(mobile);
			if (sendCode != null) {
				// 验证码相同，且在有效期内
				boolean res = code.equals(sendCode.code) && (System.currentTimeMillis() - sendCode.time < expireTime);
				if (res) {
					codeCache.remove(mobile);
				}
				return res;
			}
			return false;
		} finally {
			codeWriteLock.unlock();
		}
		
	}
	

}
