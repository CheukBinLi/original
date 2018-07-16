package com.cheuks.bin.original.common.util.conver;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.util.reflection.ClassInfo;
import com.cheuks.bin.original.common.util.reflection.FieldInfo;
import com.cheuks.bin.original.common.util.reflection.ReflectionUtil;
import com.cheuks.bin.original.common.util.reflection.Type;

public class JsonMapper {

	final static Logger LOG = LoggerFactory.getLogger(JsonMapper.class);

	private ReflectionUtil reflectionUtil = ReflectionUtil.instance();

	private static JsonMapper INSTANCE = new JsonMapper();

	private volatile SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private volatile JsonMapperPropertyInclusion defaultPropertyInclusion = JsonMapperPropertyInclusion.ALWAYS;

	public SimpleDateFormat getDefaultFormat() {
		return defaultFormat;
	}

	public JsonMapper setDefaultFormat(String format) {
		this.defaultFormat = new SimpleDateFormat(format);
		return this;
	}

	public JsonMapperPropertyInclusion getDefaultPropertyInclusion() {
		return defaultPropertyInclusion;
	}

	public JsonMapper setDefaultPropertyInclusion(JsonMapperPropertyInclusion defaultPropertyInclusion) {
		this.defaultPropertyInclusion = defaultPropertyInclusion;
		return this;
	}

	public void setDefaultFormat(SimpleDateFormat defaultFormat) {
		this.defaultFormat = defaultFormat;
	}

	protected JsonMapper() {
	}

	public static JsonMapper newInstance(boolean isSingle) {
		if (isSingle) {
			if (null == INSTANCE) {
				synchronized (JsonMapper.class) {
					if (null == INSTANCE) {
						INSTANCE = new JsonMapper();
					}
				}
			}
			return INSTANCE;
		}
		return new JsonMapper();
	}

	public String writeToStringWithAlias(final Object o, FilterProvider filterProvider) throws Exception {
		return writer(o, filterProvider, true);
	}

	public String writeToString(final Object o, FilterProvider filterProvider) throws Exception {
		return writer(o, filterProvider, false);
	}

	//	public <T> T readToObject(Class<T> type, byte[] data) throws Exception {
	//		T result = type.newInstance();
	//
	//		ClassInfo current = ClassInfo.getClassInfo(result.getClass());		
	//		
	//		
	//		
	//		
	//		return result;
	//	}

	@SuppressWarnings("unused")
	private String writer(final Object o, FilterProvider filterProvider, boolean withAlias) throws Exception {
		ClassInfo classInfo = ClassInfo.getClassInfo(o.getClass());
		StringBuilder result;
		if (null == o) {
			return "{}";
		} else if (classInfo.isBasicOrArrays()) {
			/** 基本类型 */
			return "{\"" + Type.valueToString(o, classInfo) + "\"}";
		} else if (classInfo.isDate()) {
			/** 日期 */
			return "{\"" + defaultFormat.format(o) + "\"}";
		} else if (classInfo.isMapOrCollection()) {
			/** 集合 */
			recursionSub(null, o, result = new StringBuilder(), filterProvider, withAlias);
		} else {
			result = new StringBuilder("{");
			recursion(o, filterProvider, result, withAlias);
			result.append("}");
		}
		return result.toString();
	}

	private void recursion(Object o, FilterProvider filterProvider, final StringBuilder result, boolean withAlias) throws Exception {
		ClassInfo currentClassInfo = ClassInfo.getClassInfo(o.getClass());
		ClassInfo subClassInfo;
		String tagName;
		Object tempValue;
		Filter currentClazz = null == filterProvider ? null : filterProvider.getFilterByClass(o.getClass());
		Filter filterAll = null == filterProvider ? null : filterProvider.getFilterByClass(null);

		if (null == o || currentClassInfo.isBasicOrArrays()) {
			/** 基本类型 */
			result.append(Type.valueToString(o, currentClassInfo));
			return;
		} else if (currentClassInfo.isDate()) {
			/** 日期 */
			result.append(defaultFormat.format(o));
		} else if (currentClassInfo.isMapOrCollection()) {
			/** 集合 */
			recursionSub(null, o, result, filterProvider, withAlias);
		} else {
			/** 特殊对象 */
			if (null == currentClassInfo.getFields())
				currentClassInfo.setFields(reflectionUtil.scanClassFieldInfo4List(currentClassInfo.getClazz(), true, true, true));
			for (FieldInfo fieldInfo : currentClassInfo.getFields()) {
				tempValue = fieldInfo.getField().get(o);
				tagName = withAlias ? fieldInfo.getAliasOrFieldName() : fieldInfo.getField().getName();
				//1-过滤
				//2-空校验
				//3-建模
				if ((null != currentClazz && currentClazz.getExcepts().contains(tagName)) || (null != filterAll && filterAll.getExcepts().contains(tagName))) {
					continue;
				} else if (null == tempValue) {
					result.append(Type.nullToJson(tagName)).append(",");
					continue;
				}
				subClassInfo = ClassInfo.getClassInfo(tempValue.getClass());
				if (LOG.isDebugEnabled())
					LOG.debug("field:{} type:{}", tagName, subClassInfo.getClazz().getName());
				if (subClassInfo.isMapOrCollection()) {
					recursionSub(tagName, tempValue, result, filterProvider, withAlias);
				} else if (subClassInfo.isBasicOrArrays()) {
					result.append(Type.valueToJson(tagName, tempValue, subClassInfo));
				} else if (subClassInfo.isDate()) {

					result.append("\"").append(tagName).append("\":\"").append(defaultFormat.format(tempValue)).append("\"");

				} else {
					result.append("\"").append(tagName).append("\":").append("{");
					recursion(tempValue, filterProvider, result, withAlias);
					result.append("}");
				}
				result.append(",");
			}
		}
		if (result.length() > 1)
			result.setLength(result.length() - 1);
	}

	private void recursionSub(final String tagName, final Object value, final StringBuilder result, FilterProvider filterProvider, boolean withAlias) throws Exception {
		boolean hasTagName = null != tagName;
		Map<?, ?> map = null;
		Object tempSubValue;
		ClassInfo currentClassInfo = ClassInfo.getClassInfo(value.getClass());
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

			subClassInfo = ClassInfo.getClassInfo(tempSubValue.getClass());

			if (subClassInfo.isMapOrCollection()) {
				recursionSub(hasTagName ? null : subTagName, tempSubValue, result, filterProvider, withAlias);
			} else if (subClassInfo.isBasicOrArrays()) {
				result.append(currentClassInfo.isMap() ? Type.valueToJson(subTagName, tempSubValue, subClassInfo) : Type.valueToString4Json(tempSubValue, subClassInfo));
			} else if (subClassInfo.isDate()) {
				result.append("\"").append(currentClassInfo.getName()).append("\":\"").append(defaultFormat.format(tempSubValue)).append("\"");
			} else {
				result.append("{");
				recursion(tempSubValue, filterProvider, result, withAlias);
				result.append("}");
			}
			result.append(",");
		}
		result.setLength(result.length() - 1);
		result.append(end);
	}

	public static void main(String[] args) throws Throwable {
		//		long now = System.currentTimeMillis();
		//		Filter f = new Filter(ClassInfo.class, "a", "b", "c", "e", "f", "g");
		//		List<Filter> list = new LinkedList<>();
		//		list.add(f);
		//
		//		System.out.println(INSTANCE.writeToString("xxxxxxxxxxx", null));
		//		System.out.println(INSTANCE.writeToString(list, null) + "   " + (System.currentTimeMillis() - now));
		//		now = System.currentTimeMillis();
		//		System.out.println(INSTANCE.writeToString(list, null) + "   " + (System.currentTimeMillis() - now));
		//
		//		List<Integer> ii = new LinkedList<>(Arrays.asList(1, 3, 4, 56, 76, 6, 54, 2));
		//		Map<String, Object> x = new HashMap<>();
		//		x.put("oh shit", ii);
		//		List<Map<String, Object>> p = new ArrayList<>();
		//		p.add(x);
		//		BasePage<Map<String, Object>> page = new BasePage<>(p, 1, 1, 1, 1);
		//		now = System.currentTimeMillis();
		//		System.out.println(INSTANCE.writeToStringWithAlias(page, null) + " 1  " + (System.currentTimeMillis() - now));
		//		now = System.currentTimeMillis();
		//		System.out.println(INSTANCE.writeToString(page, null) + " 2  " + (System.currentTimeMillis() - now));
		//
		//		com.cheuks.bin.original.common.util.conver.ObjectToJson j = com.cheuks.bin.original.common.util.conver.ObjectToJson.newInstance();
		//
		//		now = System.currentTimeMillis();
		//		System.out.println(j.writeToString(page, null) + " 1  " + (System.currentTimeMillis() - now));
		//		now = System.currentTimeMillis();
		//		System.out.println(j.writeToString(page, null) + " 2  " + (System.currentTimeMillis() - now));
		//
		//		String a = "1896a7242805f2b72b9d94631aae6ed0.tomcat.tar";
		//		System.err.println(a.substring(a.lastIndexOf(".") + 1));
		//		System.err.println(ClassInfo.class.toString());
	}

}
