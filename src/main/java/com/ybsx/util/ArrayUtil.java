package com.ybsx.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.ybsx.Application;

/**
 * 数组工具类
 * @author zhouKai
 * @createDate ‎2017‎年‎8‎月‎14‎日 ‏‎20:09:52
 */
public class ArrayUtil {

	// 本工程的包路径
	private static String ownerPkName = Application.class.getPackage().getName(); 

	/**
	 * 把数组转化为字符串，增强 Arrays.asList()的功能
	 * @param objs 只包含8种基本类型及其包装类型，和本项目下的类
	 * @return 
	 * @throws Exception
	 */
	public static String objs2string(Object[] objs) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean globalFlag = false;
		for (Object arg : objs) {
			Class<? extends Object> cl = arg.getClass();
			if (cl.isArray()) {
				Object[] childs = array2objs(arg);
				if (globalFlag) {
					sb.append(", " + objs2string(childs));
				} else {
					sb.append(objs2string(childs));
					globalFlag = true;
				}
			} else {
				 // 对本工程内的类，输出其属性的值，field = value， 不包含为null的字段
				if (!cl.isPrimitive() && cl.getPackage().getName().startsWith(ownerPkName)) {
					if (globalFlag) {
						sb.append(", " + cl.getSimpleName() + " [");
					} else {
						sb.append(cl.getSimpleName() + " [");
						globalFlag = true;
					}
					try {
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
					} catch (Exception e) {
						throw new RuntimeException(e);
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
	private static Object[] array2objs(Object obj) {
		String name = obj.getClass().getName();
		switch (name) {
		case "[B":
			return array2objs0(obj, Array::getByte);
		case "[S":
			return array2objs0(obj, Array::getShort);
		case "[I":
			return array2objs0(obj, Array::getInt);
		case "[J":
			return array2objs0(obj, Array::getLong);
		case "[F":
			return array2objs0(obj, Array::getFloat);
		case "[D":
			return array2objs0(obj, Array::getDouble);
		case "[C":
			return array2objs0(obj, Array::getChar);
		case "[Z":
			return array2objs0(obj, Array::getBoolean);
		default:
			return array2objs0(obj, Array::get);
		}
	}

	private static Object[] array2objs0(Object obj, BiFunction<Object, Integer, Object> biFun) {
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
