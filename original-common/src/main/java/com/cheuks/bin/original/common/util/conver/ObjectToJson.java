package com.cheuks.bin.original.common.util.conver;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.cheuks.bin.original.common.util.ReflectionCache;
import com.cheuks.bin.original.common.util.ReflectionUtil;

public class ObjectToJson {

	private ReflectionCache reflectionCache = ReflectionCache.newInstance();

	private ReflectionUtil reflectionUtil = ReflectionUtil.instance();

	private static ObjectToJson INSTANCE;

	private volatile SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public SimpleDateFormat getDefaultFormat() {
		return defaultFormat;
	}

	public ObjectToJson setDefaultFormat(String format) {
		this.defaultFormat = new SimpleDateFormat(format);
		return this;
	}

	protected ObjectToJson() {
	}

	public static ObjectToJson newInstance() {
		if (null == INSTANCE) {
			synchronized (ObjectToJson.class) {
				if (null == INSTANCE) {
					INSTANCE = new ObjectToJson();
				}
			}
		}
		return INSTANCE;
	}

	public String writeToString(Object o, FilterProvider filterProvider) throws Exception {
		StringBuilder result = new StringBuilder();
		recursion(o, null, filterProvider, result);
		return result.toString();
	}

	private void recursion(Object o, final List<Field> fieldData, FilterProvider filterProvider, final StringBuilder result) throws Exception {
		List<Field> fields = null == fieldData ? reflectionCache.getFields4List(o.getClass(), true) : fieldData;
		String tagName;
		Object tempValue;
		result.append("{");
		Filter currentClazz = null == filterProvider ? null : filterProvider.getFilterByClass(o.getClass());
		Filter filterAll = null == filterProvider ? null : filterProvider.getFilterByClass(null);
		for (Field field : fields) {
			tagName = field.getName();
			if ((null != currentClazz && currentClazz.excepts.contains(tagName)) || (null != filterAll && filterAll.getExcepts().contains(tagName))) {
				continue;
			}
			tempValue = field.get(o);
			if (null != field && !field.getType().isPrimitive() && !reflectionUtil.isWrapperClass(tempValue.getClass())) {
				recursionSub(field.getName(), tempValue, result, null, filterProvider);
			} else if (null != tempValue) {
				result.append("\"").append(tagName).append("\"").append(":\"").append(Date.class == tempValue.getClass() ? defaultFormat.format(tempValue) : tempValue.toString()).append("\"");
			}
			result.append(",");
		}
		if (result.length() > 1)
			result.setLength(result.length() - 1);
		result.append("}");
	}

	private void recursionSub(final String name, final Object value, final StringBuilder result, final List<Field> fieldData, FilterProvider filterProvider) throws Exception {
		boolean isMap;
		Map<?, ?> map = null;
		List<Field> subField;
		Object tempSubValue;
		Object tempSubKey;
		Collection<?> collection = null;
		Iterator<?> it;
		Entry<?, ?> en;
		boolean isDate;
		Filter currentClazz = null == filterProvider ? null : filterProvider.getFilterByClass(value.getClass());
		Filter filterAll = null == filterProvider ? null : filterProvider.getFilterByClass(null);
		if ((isMap = reflectionUtil.isMap(value)) || reflectionUtil.isCollection(value)) {
			if (isMap) {
				map = (Map<?, ?>) value;
				if (map.isEmpty()) {
					return;
				}
			} else {
				collection = (Collection<?>) value;
				if (collection.isEmpty()) {
					return;
				}
			}

			result.append("\"").append(name).append("\"").append(":[");
			it = isMap ? map.entrySet().iterator() : collection.iterator();
			subField = null;
			while (it.hasNext()) {
				tempSubValue = it.next();
				if (isMap) {
					en = (Entry<?, ?>) tempSubValue;
					tempSubValue = en.getValue();
					tempSubKey = en.getKey();
					if (null == tempSubKey || (null != currentClazz && currentClazz.excepts.contains(tempSubKey)) || (null != filterAll && filterAll.getExcepts().contains(tempSubKey))) {
						result.append(",");
						continue;
					}
					if ((isDate = Date.class == tempSubValue.getClass()) || tempSubValue.getClass().isPrimitive() || reflectionUtil.isWrapperClass(tempSubValue.getClass())) {
						//						result.append(writeToString(tempSubValue, subField)).append(",");
						result.append("{\"").append(tempSubKey.toString()).append("\":\"").append(isDate ? defaultFormat.format(tempSubValue) : tempSubValue).append("\"},");
					} else {
						result.append("{");
						recursionSub(tempSubKey.toString(), tempSubValue, result, null, filterProvider);
						result.append("},");
					}
				} else {
					if (null == subField) {
						subField = null == fieldData ? reflectionCache.getFields4List(tempSubValue.getClass(), true) : fieldData;
					}
					if ((isDate = Date.class == tempSubValue.getClass()) || tempSubValue.getClass().isPrimitive() || reflectionUtil.isWrapperClass(tempSubValue.getClass())) {
						result.append("\"").append(isDate ? defaultFormat.format(tempSubValue) : tempSubValue).append("\",");
					} else {
						recursion(tempSubValue, subField, filterProvider, result);
						result.append(",");
					}
				}
			}
			result.setLength(result.length() - 1);

			result.append("]");
		} else if (null != value) {
			result.append("\"").append(name).append("\"").append(":\"").append(value).append("\"");
		}
	}

	public static class FilterProvider {
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

	public static class Filter {
		private Class<?> clazz;
		private List<String> excepts;

		public Filter() {
			super();
		}

		public Filter(Class<?> clazz, String... excepts) {
			super();
			this.clazz = clazz;
			this.excepts = new LinkedList<String>(Arrays.asList(excepts));
		}

		public Class<?> getClazz() {
			return clazz;
		}

		public Filter setClazz(Class<?> clazz) {
			this.clazz = clazz;
			return this;
		}

		public List<String> getExcepts() {
			return excepts;
		}

		public Filter setExcepts(List<String> excepts) {
			this.excepts = excepts;
			return this;
		}

	}

	public static void main(String[] args) throws Throwable {
		//		Map<String, String> mmx = new HashMap<>();
		//		mmx.put("A", "A");
		//		mmx.put("B", "B");
		//		mmx.put("C", "C");
		//		mmx.put("D", "D");
		//		Result<Map<String, String>> result = new Result<Map<String, String>>("20", "哇哈哈", mmx);
		//
		//		Map<String, List<Object>> a = new HashMap<>();
		//		List<Object> list = new ArrayList<>();
		//		list.add("z");
		//		list.add("x");
		//		list.add("c");
		//		list.add("v");
		//		list.add(new Date());
		//		a.put("abc", list);
		//		System.out.println(new ObjectToJson().writeToString(result, new FilterProvider(new Filter(Result.class, "msg"), new Filter(null, "abc"))));
		//
		//		System.out.println(new ObjectToJson().writeToString(new Result<Object>("11", "xxx", a), new FilterProvider(new Filter(Result.class, "msg"), new Filter(null, "abc1"))));

	}

}
