package com.cheuks.bin.original.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReflectionUtil {

	private static final ReflectionUtil INSTANCE = new ReflectionUtil();

	public static final ReflectionUtil instance() {
		return INSTANCE;
	}

	public Map<String, Field> scanClassField4Map(Class<?> clazz, boolean isAccessible, boolean hasSetting) throws NoSuchFieldException, SecurityException {
		if (null == clazz) return null;

		LinkedList<Class<?>> classes = new LinkedList<Class<?>>();
		classes.add(clazz);
		Class<?> tempClass;
		Class<?> currentClass = clazz;
		List<Field> fields = new ArrayList<Field>();
		List<Method> methods = new ArrayList<Method>();

		//向上遍历父类
		while (true) {
			if (null == (tempClass = clazz.getSuperclass()) || tempClass == currentClass) break;
			classes.addLast(currentClass = tempClass);
		}
		for (int i = 0, len = classes.size(); i < len; i++) {
			tempClass = classes.removeLast();
			fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			if (hasSetting) {
				methods.addAll(Arrays.asList(tempClass.getDeclaredMethods()));
			}
		}

		Map<String, Field> result = new HashMap<String, Field>();
		Set<String> settingMethodName = null;
		settingMethodName = new HashSet<String>();
		for (Method m : methods) {
			if (m.getName().startsWith("set")) {
				settingMethodName.add(m.getName().substring(3).toLowerCase());
			}
		}
		for (Field f : fields) {
			if (!hasSetting || settingMethodName.contains(f.getName().toLowerCase())) {
				if (isAccessible) f.setAccessible(true);
				result.put(f.getName(), f);
			}
		}
		return result;
	}

	public List<Field> scanClassField4List(Class<?> clazz, boolean isAccessible, boolean hasSetting) throws NoSuchFieldException, SecurityException {
		if (null == clazz) return null;

		LinkedList<Class<?>> classes = new LinkedList<Class<?>>();
		classes.add(clazz);
		Class<?> tempClass;
		Class<?> currentClass = clazz;
		List<Field> fields = new ArrayList<Field>();
		List<Method> methods = new ArrayList<Method>();

		//向上遍历父类
		while (true) {
			if (null == (tempClass = clazz.getSuperclass()) || tempClass == currentClass) break;
			classes.addLast(currentClass = tempClass);
		}
		for (int i = 0, len = classes.size(); i < len; i++) {
			tempClass = classes.removeLast();
			fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			if (hasSetting) {
				methods.addAll(Arrays.asList(tempClass.getDeclaredMethods()));
			}
		}

		List<Field> result = new ArrayList<Field>();
		Set<String> settingMethodName = null;
		if (hasSetting) {
			settingMethodName = new HashSet<String>();
			for (Method m : methods) {
				if (m.getName().startsWith("set")) {
					settingMethodName.add(m.getName().substring(3).toLowerCase());
				}
			}
		}
		for (Field f : fields) {
			if (!hasSetting || settingMethodName.contains(f.getName().toLowerCase())) {
				if (isAccessible) f.setAccessible(true);
				result.add(f);
			}
		}
		return result;
	}

}
