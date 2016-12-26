package com.cheuks.bin.original.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReflectionUtil {

	private static final ReflectionUtil INSTANCE = new ReflectionUtil();

	public static final ReflectionUtil instance() {
		return INSTANCE; 
	}

	public Map<String, Field> scanClassField4Map(Class<?> clazz, boolean isAccessible, boolean hasSetting) throws NoSuchFieldException, SecurityException {
		Field[] fields = clazz.getDeclaredFields();
		Map<String, Field> result = new HashMap<String, Field>();
		Set<String> settingMethodName = null;
		if (hasSetting) {
			Method[] methods = clazz.getDeclaredMethods();
			settingMethodName = new HashSet<String>();
			for (Method m : methods) {
				if (m.getName().startsWith("set")) {
					settingMethodName.add(m.getName().substring(3).toLowerCase());
				}
			}
		}
		for (Field f : fields) {
			if (!hasSetting || settingMethodName.contains(f.getName().toLowerCase())) {
				if (isAccessible)
					f.setAccessible(true);
				result.put(f.getName(), f);
			}
		}
		return result;
	}

	public List<Field> scanClassField4List(Class<?> clazz, boolean isAccessible, boolean hasSetting) throws NoSuchFieldException, SecurityException {
		Field[] fields = clazz.getDeclaredFields();
		List<Field> result = new ArrayList<Field>();
		Set<String> settingMethodName = null;
		if (hasSetting) {
			settingMethodName = new HashSet<String>();
			Method[] methods = clazz.getDeclaredMethods();
			for (Method m : methods) {
				if (m.getName().startsWith("set")) {
					settingMethodName.add(m.getName().substring(3).toLowerCase());
				}
			}
		}
		for (Field f : fields) {
			if (!hasSetting || settingMethodName.contains(f.getName().toLowerCase())) {
				if (isAccessible)
					f.setAccessible(true);
				result.add(f);
			}
		}
		return result;
	}

}
