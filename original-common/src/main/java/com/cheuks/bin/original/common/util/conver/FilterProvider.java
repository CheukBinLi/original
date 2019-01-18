package com.cheuks.bin.original.common.util.conver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FilterProvider {
	
	private final Map<String, Filter> filters;
	
	private Object additional;

	public FilterProvider(Filter... filters) {
		super();
		if (null == filters) {
			this.filters = new ConcurrentHashMap<String, Filter>(0);
			return;
		}
		this.filters = new ConcurrentHashMap<String, Filter>(filters.length);
		for (Filter filter : filters) {
			this.filters.put(null == filter.getClazz() ? "*" : filter.getClazz().getName(), filter);
		}
	}

	public Filter getFilterByClass(Class<?> clazz) {
		return getFilter(null == clazz ? "*" : clazz.getName());
	}

	public Filter getFilter(String clazz) {
		return filters.get(null == clazz ? "*" : clazz);
	}

	public static boolean isInclude(String name, Filter... filters) {
		boolean result = false;
		if (null == name || null == filters || filters.length < 1)
			return result;
		for (Filter filter : filters) {
			if (null == filter || filter.exceptsIsEmpty()) {
				continue;
			}
			if (result = filter.getExcepts().contains(name))
				return result;
		}
		return result;
	}

	public static boolean isIgnore(String name, Filter... filters) {
		if (null == name || null == filters || filters.length < 1)
			return false;
		for (Filter filter : filters) {
			if (null == filter) {
				continue;
			}
			if (filter.isOnlyIncude()) {
				if (!filter.includesIsEmpty() && filter.getIncludes().containsKey(name)) {
					return false;
				}
				return true;
			} else if (!filter.includesIsEmpty() && filter.getIncludes().containsKey(name)) {
				return false;
			} else if (!filter.exceptsIsEmpty() && filter.getExcepts().contains(name)) {
				return true;
			}
		}
		return false;

	}

	public static String getCurrentValueFormat(String name, Filter... filters) {
		return getCurrentValueFormat(name, null, filters);
	}

	public static String getCurrentValueFormat(String name, String defaultFormat, Filter... filters) {
		if (null == name || null == filters || filters.length < 1)
			return defaultFormat;
		String result;
		for (Filter filter : filters) {
			if (null == filter || filter.includesIsEmpty()) {
				continue;
			}
			result = filter.getIncludes().get(name);
			if (null != result) {
				return result;
			}
		}
		return defaultFormat;
	}

	public static ReplaceProvider getCurrentReplaceProvider(String name, Filter filter) {
		if (null == name || null == filter)
			return null;
		//		for (Filter filter : filters) {
		//			if (null == filter || filter.getReplaces().isEmpty()) {
		//				continue;
		//			}
		//		}
		return filter.getReplaces().get(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAdditional() {
		return (T) additional;
	}

	public FilterProvider setAdditional(Object additional) {
		this.additional = additional;
		return this;
	}
	
	
}