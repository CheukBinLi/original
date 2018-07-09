package com.cheuks.bin.original.common.util.conver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FilterProvider {
	private final Map<String, Filter> filters;

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

}