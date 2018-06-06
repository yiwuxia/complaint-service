package com.ybsx.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BaseEntity {

	@Override
	public String toString() {
		Class<? extends Object> clzz = this.getClass();
		String className = clzz.getSimpleName();
		ArrayList<String> fieldKVs = new ArrayList<>();
		do {
			for (Field field : clzz.getDeclaredFields()) {
				field.setAccessible(true);
				try {
					Object value = field.get(this);
					fieldKVs.add(field.getName() + "=" + value);
				} catch (Exception e) {}
			}
			clzz = clzz.getSuperclass();
		} while (clzz != Object.class);

		String result = fieldKVs.stream().collect(Collectors.joining(", ", className + " [", "]"));
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
