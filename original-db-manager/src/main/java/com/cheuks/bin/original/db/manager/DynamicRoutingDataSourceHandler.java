package com.cheuks.bin.original.db.manager;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

public class DynamicRoutingDataSourceHandler {

	private static ThreadLocal<String> dataSource = new ThreadLocal<String>();

	private Map<String, String> dataSourceRegex = new ConcurrentHashMap<String, String>();

	public static String get() {
		return dataSource.get();
	}

	public static void setting(String dataSource) {
		DynamicRoutingDataSourceHandler.dataSource.set(dataSource);
	}

	public void dynamicRouting(JoinPoint joinPoint) {
		MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint = (MethodInvocationProceedingJoinPoint) joinPoint;
		String methodName = methodInvocationProceedingJoinPoint.getSignature().getName();
		Entry<String, String> regex;
		Iterator<Entry<String, String>> it = dataSourceRegex.entrySet().iterator();
		while (it.hasNext()) {
			regex = it.next();
			if (methodName.matches(regex.getKey())) {
				dataSource.set(regex.getValue());
				return;
			}
		}
		dataSource.set(null);
	}

	public Map<String, String> getDataSourceRegex() {
		return dataSourceRegex;
	}

	public DynamicRoutingDataSourceHandler setDataSourceRegex(Map<String, String> dataSourceRegex) {
		if (null == dataSourceRegex)
			return this;
		this.dataSourceRegex.clear();
		for (Entry<String, String> en : dataSourceRegex.entrySet()) {
			this.dataSourceRegex.put(String.format("^(/|.*/|.*)?%s$", en.getKey().replace("*", ".*").replace(".*.*", ".*")), en.getValue());
		}
		return this;
	}

}
