package com.cheuks.bin.original.common.util.reflection;

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

import com.cheuks.bin.original.common.util.ReflectionUtil;

public class ObjectToJson {

	private ReflectionUtil reflectionUtil = ReflectionUtil.instance();

	private static ObjectToJson INSTANCE = new ObjectToJson();

	private volatile SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private volatile DefaultPropertyInclusion defaultPropertyInclusion = DefaultPropertyInclusion.ALWAYS;

	public static enum DefaultPropertyInclusion {
													ALWAYS,
													NON_NULL,
													NON_DEFAULT,
													NON_EMPTY
	}

	public SimpleDateFormat getDefaultFormat() {
		return defaultFormat;
	}

	public ObjectToJson setDefaultFormat(String format) {
		this.defaultFormat = new SimpleDateFormat(format);
		return this;
	}

	public DefaultPropertyInclusion getDefaultPropertyInclusion() {
		return defaultPropertyInclusion;
	}

	public ObjectToJson setDefaultPropertyInclusion(DefaultPropertyInclusion defaultPropertyInclusion) {
		this.defaultPropertyInclusion = defaultPropertyInclusion;
		return this;
	}

	public void setDefaultFormat(SimpleDateFormat defaultFormat) {
		this.defaultFormat = defaultFormat;
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

	@SuppressWarnings("unused")
	public String writeToString(final Object o, FilterProvider filterProvider) throws Exception {
		ClassInfo classInfo = new ClassInfo(o.getClass());
		StringBuilder result;
		if (null == o) {
			return "{}";
		} else if (classInfo.isBasicOrArrays()) {
			/** 基本类型 */
			return "{\"" + Type.valueToString(o, classInfo) + "\"}";
		} else if (Date.class.equals(o.getClass())) {
			/** 日期 */
			return "{\"" + defaultFormat.format(o) + "\"}";
		} else if (classInfo.isMapOrCollection()) {
			/** 集合 */
			recursionSub(classInfo, null, o, result = new StringBuilder(), filterProvider);
		} else {
			recursion(o, classInfo, filterProvider, result = new StringBuilder());
		}
		return result.toString();
	}

	private void recursion(Object o, final ClassInfo classInfo, FilterProvider filterProvider, final StringBuilder result) throws Exception {
		ClassInfo currentClassInfo = classInfo.getClassInfo(o.getClass());
		ClassInfo subClassInfo;
		String tagName;
		Object tempValue;
		Filter currentClazz = null == filterProvider ? null : filterProvider.getFilterByClass(o.getClass());
		Filter filterAll = null == filterProvider ? null : filterProvider.getFilterByClass(null);

		if (null == o || currentClassInfo.isBasicOrArrays()) {
			/** 基本类型 */
			result.append(Type.valueToString(o, classInfo));
			return;
		} else if (Date.class.equals(o.getClass())) {
			/** 日期 */
			result.append(defaultFormat.format(o));
		} else if (currentClassInfo.isMapOrCollection()) {
			/** 集合 */
			recursionSub(classInfo, null, o, result, filterProvider);
		} else {
			/** 特殊对象 */
			if (null == currentClassInfo.getFields())
				currentClassInfo.setFields(reflectionUtil.scanClassField4List(currentClassInfo.getClazz(), true, true, false));
			for (Field field : currentClassInfo.getFields()) {
				tempValue = field.get(o);
				tagName = field.getName();
				//1-过滤
				//2-空校验
				//3-建模
				if ((null != currentClazz && currentClazz.getExcepts().contains(tagName)) || (null != filterAll && filterAll.getExcepts().contains(tagName))) {
					continue;
				} else if (null == tempValue) {
					result.append(Type.nullToJson(tagName)).append(",");
					continue;
				}
				System.out.println(field.getName());
				subClassInfo = classInfo.getClassInfo(tempValue.getClass());
				if (subClassInfo.isMapOrCollection()) {
					recursionSub(classInfo, field.getName(), tempValue, result, filterProvider);
				} else if (subClassInfo.isBasicOrArrays()) {
					result.append(Type.valueToJson(tempValue, subClassInfo));
				} else if (Date.class.equals(tempValue.getClass())) {
					result.append("\"").append(field.getName()).append("\":\"").append(defaultFormat.format(tempValue)).append("\"");
				} else {
					recursion(tempValue, classInfo, filterProvider, result);
				}
				result.append(",");
			}
		}
		if (result.length() > 1)
			result.setLength(result.length() - 1);
	}

	@SuppressWarnings("unused")
	private void recursionSub(final ClassInfo classInfo, final String tagName, final Object value, final StringBuilder result, FilterProvider filterProvider) throws Exception {
		boolean hasTagName = null != tagName;
		Map<?, ?> map = null;
		Object tempSubValue;
		ClassInfo currentClassInfo = classInfo.getClassInfo(value.getClass());
		Collection<?> collection = null;
		ClassInfo subClassInfo;
		Iterator<?> it;
		Entry<?, ?> en;
		String end = "]";
		String subTagName = tagName;
		Filter currentClazz = null == filterProvider ? null : filterProvider.getFilterByClass(value.getClass());
		Filter filterAll = null == filterProvider ? null : filterProvider.getFilterByClass(null);
		if (hasTagName)
			result.append("\"").append(tagName).append("\":");
		if (currentClassInfo.isMap()) {
			map = (Map<?, ?>) value;
			if (map.isEmpty()) {
				result.setLength(result.length() - 1);
				result.append(":null");
				return;
			}
			result.append("{");
			end = "}";
			it = map.entrySet().iterator();
		} else if (currentClassInfo.isCollection()) {
			collection = (Collection<?>) value;
			if (collection.isEmpty()) {
				result.setLength(result.length() - 1);
				result.append(":null");
				return;
			}
			result.append("[");
			it = collection.iterator();
		} else {
			System.err.println("unkonw type:" + value.getClass());
			return;
		}

		while (it.hasNext()) {
			tempSubValue = it.next();
			if (currentClassInfo.isMap()) {
				en = (Entry<?, ?>) tempSubValue;
				tempSubValue = en.getValue();
				subTagName = en.getKey().toString();
			}

			if ((null != currentClazz && currentClazz.getExcepts().contains(subTagName)) || (null != filterAll && filterAll.getExcepts().contains(subTagName))) {
				continue;
			}

			if (null == tempSubValue) {
				result.append(Type.nullToJson(subTagName)).append(",");
				continue;
			}

			subClassInfo = classInfo.getClassInfo(tempSubValue.getClass());

			if (subClassInfo.isMapOrCollection()) {
				recursionSub(classInfo, subTagName, tempSubValue, result, filterProvider);
			} else if (subClassInfo.isBasicOrArrays()) {
				//				result.append(Type.valueToJson(subTagName, tempSubValue, subClassInfo));
				result.append(Type.valueToString4Json(tempSubValue, subClassInfo));
			} else if (Date.class.equals(tempSubValue.getClass())) {
				result.append("\"").append(currentClassInfo.getName()).append("\":\"").append(defaultFormat.format(tempSubValue)).append("\"");
			} else {
				recursion(tempSubValue, classInfo, filterProvider, result);
			}
			result.append(",");
		}
		result.setLength(result.length() - 1);

		result.append(end);
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

		Filter f = new Filter(ClassInfo.class, "a", "b", "c", "e", "f", "g");
		List<Filter> list = new LinkedList<>();
		list.add(f);

		//		System.out.println(INSTANCE.writeToString("xxxxxxxxxxx", null));
		System.out.println(INSTANCE.writeToString(list, null));

		String a = "1896a7242805f2b72b9d94631aae6ed0.tomcat.tar";
		System.err.println(a.substring(a.lastIndexOf(".") + 1));
	}

}
