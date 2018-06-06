package com.ybsx.base.aspect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.ybsx.Application;
import com.ybsx.util.ArrayUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis通用缓存拦截器
 * @author zhouKai
 * @createDate ‎2017‎年‎8‎月‎14‎日 ‏‎20:09:52
 */
//@Aspect
@Component
public class MapperAspect {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private JedisPool jedisPool;

	@Autowired
	private ObjectMapper objectMapper;
	
	// 本工程的包路径
	private String ownerPkName = Application.class.getPackage().getName(); 
	
	// key的过期时间，单位秒
	private int timeOut = 1000;

	/**
	 * 对dao层所有以select开头的查询进行拦截
	 * 拦截请求方法
	 * @author zhoukai
	 */
	@Pointcut(value = "execution(public * com.ybsx.dao.mapper.master..*.select*(..))")
	public void mybatis() {

	}

	/**
	 * 使用redis中的string类型<br/>
	 * key的形式如下： TraceCodeMapper.selectCheckCode(..)[101000000393, 395],
	 * value为当前查询结果的json字符串.	<br/>
	 * 目前只支持形式如下的4中返回类型；	<br/><pre>
	 * 	1. Object	<br/>
	 * 	2. Map&ltString, Person&gt	<br/>
	 * 	3. List&ltPerson&gt	<br/>
	 * 	4. List&ltMap&ltString, Person&gt&gt	<br/></pre>
	 * 
	 * 注意：
	 * 		如果dao层方法的形参包含POJO，在这个POJO中不要给查询中用不到的属性赋值，
	 * 		因为redis中的key中，不包含为null的key，
	 * 		在手动更新缓存时，也要注意此点。
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around(value = "mybatis()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		Jedis jedis = jedisPool.getResource();
		try {
			Signature signature = joinPoint.getSignature();
//			String key = signature.toShortString() + this.args2string(joinPoint.getArgs());
			// 此key不包含为null的属性
			String key = signature.toShortString() + ArrayUtil.objs2string(joinPoint.getArgs());
			Boolean exists = jedis.exists(key);
			if (exists) { // 存在
				logger.info("hit-->" + key);
				Class<?> target = signature.getDeclaringType();
				String methodName = signature.getName();
				Method method = null;
				Method[] methods = target.getMethods();
				for (Method m : methods) {
					if (m.getName().equals(methodName)) {
						method = m;
						break;
					}
				}

				Type grt = method.getGenericReturnType();
				if (grt instanceof ParameterizedType) { // 有泛型
					TypeFactory typeFactory = objectMapper.getTypeFactory();
					JavaType javaType = null;
					
					ParameterizedType pt = (ParameterizedType) grt;
					Class<?> parametersFor = (Class<?>) pt.getRawType();
					Type[] atArgs = pt.getActualTypeArguments();
					
					if (parametersFor == List.class) { // 返回值是List集合
						if (atArgs[0] instanceof ParameterizedType) { // 形如：List<Map<String, Person>>
							ParameterizedType mapPt = (ParameterizedType) atArgs[0];
							Type[] mapActual = mapPt.getActualTypeArguments();
							JavaType childType = typeFactory.constructParametrizedType(HashMap.class
																					, Map.class
																					, (Class<?>) mapActual[0]
																					, (Class<?>) mapActual[1]);
							javaType = typeFactory.constructParametrizedType(ArrayList.class, parametersFor, childType);
						} else { // 形如：java.util.List<Person>
							javaType = typeFactory.constructParametrizedType(ArrayList.class
																			, parametersFor
																			, (Class<?>) atArgs[0]);
						}
					} else if (parametersFor == Map.class) { // 返回值是Map集合， 形如：Map<String, Person>
						javaType = typeFactory.constructParametrizedType(HashMap.class
																		, parametersFor
																		, (Class<?>) atArgs[0]
																		, (Class<?>) atArgs[1]);
					}
					result = objectMapper.readValue(jedis.get(key), javaType);
				} else { // 无泛型，形如：PromotionInfo
					result = objectMapper.readValue(jedis.get(key), (Class<?>) grt);
				}
			} else {
				result = joinPoint.proceed();
				jedis.setex(key, timeOut, objectMapper.writeValueAsString(result));
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 把数组转化为字符串，增强 Arrays.asList()的功能
	 * @param args 只包含8种基本类型及其包装类型，和本项目下的类
	 * @return 
	 * @throws Exception
	 */
	private String args2string(Object[] args) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean globalFlag = false;
		for (Object arg : args) {
			Class<? extends Object> cl = arg.getClass();
			if (cl.isArray()) {
				Object[] childs = this.array2objs(arg);
				if (globalFlag) {
					sb.append(", " + this.args2string(childs));
				} else {
					sb.append(this.args2string(childs));
					globalFlag = true;
				}
			} else {
				if (!cl.isPrimitive() && cl.getPackage().getName().startsWith(ownerPkName)) { // 本工程内的类
					if (globalFlag) {
						sb.append(", " + cl.getSimpleName() + " [");
					} else {
						sb.append(cl.getSimpleName() + " [");
						globalFlag = true;
					}
					boolean flag = false;
					for (Field f : cl.getDeclaredFields()) {
						f.setAccessible(true);
						String name = f.getName();
						Object value = f.get(arg);
						if (value != null) {
							if (flag) {
								sb.append(", " + name + "=" + value);
							} else {
								sb.append(name + "=" + value);
								flag = true;
							}
						}
					}
					sb.append("]");
				} else {
					if (globalFlag) {
						sb.append(", " + arg);
					} else {
						sb.append(arg);
						globalFlag = true;
					}
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 把obj转化为数组
	 * @param obj	真实类型是数组
	 * @return
	 */
	private Object[] array2objs(Object obj) {
		String name = obj.getClass().getName();
		switch (name) {
		case "[B":
			return this.array2objs0(obj, Array::getByte);
		case "[S":
			return this.array2objs0(obj, Array::getShort);
		case "[I":
			return this.array2objs0(obj, Array::getInt);
		case "[J":
			return this.array2objs0(obj, Array::getLong);
		case "[F":
			return this.array2objs0(obj, Array::getFloat);
		case "[D":
			return this.array2objs0(obj, Array::getDouble);
		case "[C":
			return this.array2objs0(obj, Array::getChar);
		case "[Z":
			return this.array2objs0(obj, Array::getBoolean);
		default:
			return this.array2objs0(obj, Array::get);
		}
	}

	private Object[] array2objs0(Object obj, BiFunction<Object, Integer, Object> biFun) {
		List<Object> ls = new ArrayList<>();
		int i = 0;
		while (true) {
			try {
				ls.add(biFun.apply(obj, i++));
			} catch (Exception e) {
				break;
			}
		}
		return ls.toArray();
	}
	
}
