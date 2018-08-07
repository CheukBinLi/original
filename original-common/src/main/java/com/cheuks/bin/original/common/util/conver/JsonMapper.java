package com.cheuks.bin.original.common.util.conver;

import java.lang.reflect.Modifier;
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

	protected static final String DEFAULT_EOL = "\r\n";

	protected static final String COMPACT_EOL = "";

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

	public String writeToStringWithAliasAndWithTransient(final Object o, FilterProvider filterProvider) throws Exception {
		return writer(o, filterProvider, true, false);
	}

	public String writeToStringWithAliasAndWithOutTransient(final Object o, FilterProvider filterProvider) throws Exception {
		return writer(o, filterProvider, true, true);
	}

	public String writeToStringWithTransient(final Object o, FilterProvider filterProvider) throws Exception {
		return writer(o, filterProvider, false, false);
	}

	public String writeToStringWithOutTransient(final Object o, FilterProvider filterProvider) throws Exception {
		return writer(o, filterProvider, false, true);
	}

	public String writer(final Object o, FilterProvider filterProvider, boolean withAlias, boolean withOutTransient) throws Exception {
		return writerString(o, filterProvider, withAlias, withOutTransient, true);
	}

	@SuppressWarnings("unchecked")
	public String writer(final Object o, FilterProvider filterProvider, boolean withAlias, boolean withOutTransient, boolean eol, Map<String, ? extends Object>... additionalAttributes) throws Exception {
		StringBuilder additional = null;
		String subResult;
		if (null != additionalAttributes && additionalAttributes.length > 0) {
			additional = new StringBuilder();
			for (Map<String, ? extends Object> attributes : additionalAttributes) {
				//				recursionSub(null, attributes, subResult, filterProvider, withAlias, withOutTransient);
				subResult = writerString(attributes, filterProvider, withAlias, withOutTransient, false);
				additional.append(subResult.length() > 0 ? (subResult.toString() + ",") : COMPACT_EOL);
			}
			additional.setLength(additional.length() - 1);
		}
		String result = writerString(o, filterProvider, withAlias, withOutTransient, false);
		return (null == additional ? "{" + result + "}" : "{" + result + (result.length() > 0 ? "," : COMPACT_EOL) + additional.toString() + "}") + (eol ? DEFAULT_EOL : COMPACT_EOL);
	}

	protected String writerString(final Object o, FilterProvider filterProvider, boolean withAlias, boolean withOutTransient, boolean brackets) throws Exception {
		if (null == o) {
			return brackets ? "{}" : COMPACT_EOL;
		}
		ClassInfo classInfo = ClassInfo.getClassInfo(o.getClass());
		StringBuilder result;
		if (classInfo.isBasicOrArrays()) {
			/** 基本类型 */
			return brackets ? "{\"" + Type.valueToString(o, classInfo) + "\"}" : "\"" + Type.valueToString(o, classInfo) + "\"";
		} else if (classInfo.isDate()) {
			/** 日期 */
			return brackets ? "{\"" + defaultFormat.format(o) + "\"}" : "\"" + defaultFormat.format(o) + "\"";
		} else if (classInfo.isMapOrCollection()) {
			/** 集合 */
			recursionSub(null, o, result = new StringBuilder(), filterProvider, withAlias, withOutTransient);
		} else {
			result = new StringBuilder("{");
			recursion(o, filterProvider, result, withAlias, withOutTransient);
			result.append("}");
		}
		if (brackets)
			return result.toString();
		return result.substring(1, result.length() - 1);
	}

	private void recursion(Object o, FilterProvider filterProvider, final StringBuilder result, boolean withAlias, boolean withOutTransient) throws Exception {
		ClassInfo currentClassInfo = ClassInfo.getClassInfo(o.getClass());
		ClassInfo subClassInfo;
		String tagName;
		Object tempValue;
		FieldInfo fieldInfo;
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
			recursionSub(null, o, result, filterProvider, withAlias, withOutTransient);
		} else {
			/** 特殊对象 */
			if (null == currentClassInfo.getFields())
				//				currentClassInfo.setFields(reflectionUtil.scanClassFieldInfo4List(currentClassInfo.getClazz(), true, true, true));
				currentClassInfo.setFields(reflectionUtil.scanClassFieldInfo4Map(currentClassInfo.getClazz(), true, true, true));
			for (Entry<String, FieldInfo> en : currentClassInfo.getFields().entrySet()) {
				fieldInfo = en.getValue();
				if (fieldInfo.isAlias() || (withOutTransient && Modifier.isTransient(fieldInfo.getField().getModifiers())) || FilterProvider.isIgnore(tagName = fieldInfo.getAliasOrFieldName(withAlias), currentClazz, filterAll))
					continue;
				tempValue = fieldInfo.getField().get(o);
				if (null == tempValue) {
					result.append(Type.nullToJson(tagName)).append(",");
					continue;
				}
				subClassInfo = ClassInfo.getClassInfo(tempValue.getClass());
				if (LOG.isDebugEnabled())
					LOG.debug("field:{} type:{}", tagName, subClassInfo.getClazz().getName());
				if (subClassInfo.isMapOrCollection()) {
					recursionSub(tagName, tempValue, result, filterProvider, withAlias, withOutTransient);
				} else if (subClassInfo.isBasicOrArrays()) {
					result.append(Type.valueToJson(tagName, tempValue, FilterProvider.getCurrentValueFormat(tagName, currentClazz, filterAll), subClassInfo));
				} else if (subClassInfo.isDate()) {
					String valueFormat = FilterProvider.getCurrentValueFormat(tagName, currentClazz, filterAll);
					result.append("\"").append(tagName).append("\":\"").append(null == valueFormat ? defaultFormat.format(tempValue) : new SimpleDateFormat(valueFormat).format(tempValue)).append("\"");
				} else {
					result.append("\"").append(tagName).append("\":").append("{");
					recursion(tempValue, filterProvider, result, withAlias, withOutTransient);
					result.append("}");
				}
				result.append(",");
			}
		}
		if (result.length() > 1)
			result.setLength(result.length() - 1);
	}

	private void recursionSub(final String tagName, final Object value, final StringBuilder result, FilterProvider filterProvider, boolean withAlias, boolean withOutTransient) throws Exception {
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
		String valueFormat;
		if (hasTagName)
			result.append("\"").append(tagName).append("\":");
		if (currentClassInfo.isMap()) {
			map = (Map<?, ?>) value;
			if (map.isEmpty()) {
				result.setLength(result.length() > 0 ? result.length() - 1 : 0);
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

			if (FilterProvider.isIgnore(subTagName, currentClazz, filterAll)) {
				continue;
			}
			if (null == tempSubValue) {
				result.append(Type.nullToJson(subTagName)).append(",");
				continue;
			}

			subClassInfo = ClassInfo.getClassInfo(tempSubValue.getClass());

			if (subClassInfo.isMapOrCollection()) {
				recursionSub(hasTagName ? null : subTagName, tempSubValue, result, filterProvider, withAlias, withOutTransient);
			} else if (subClassInfo.isBasicOrArrays()) {
				valueFormat = FilterProvider.getCurrentValueFormat(currentClassInfo.isMap() ? subTagName : tagName, currentClazz, filterAll);
				result.append(currentClassInfo.isMap() ? Type.valueToJson(subTagName, tempSubValue, valueFormat, subClassInfo) : Type.valueToString4Json(tempSubValue, valueFormat, subClassInfo));
			} else if (subClassInfo.isDate()) {
				valueFormat = FilterProvider.getCurrentValueFormat(subTagName, currentClazz, filterAll);
				result.append("\"").append(subTagName).append("\":\"").append(null == valueFormat ? defaultFormat.format(tempSubValue) : new SimpleDateFormat(valueFormat).format(tempSubValue)).append("\"");
			} else {
				result.append("{");
				recursion(tempSubValue, filterProvider, result, withAlias, withOutTransient);
				result.append("}");
			}
			result.append(",");
		}
		result.setLength(result.length() - 1);
		result.append(end);
	}

	public static void main(String[] args) throws Throwable {
		//				long now = System.currentTimeMillis();
		//				//		Filter f = new Filter(ClassInfo.class, "a", "b", "c", "e", "f", "g");
		//				Filter f = Filter.build(ClassInfo.class).addExcept("a", "b", "c", "e", "f", "g").addInclude("小绿:aaa");
		//				List<Filter> list = new LinkedList<>();
		//				list.add(f);
		//				Map<String, Object> xx = new HashMap<>();
		//				xx.put("oh shit", list);
		//				xx.put("date_Time", new Date());
		//				xx.put("Ignore", "哇哈哈");
		//		//						//
		//
		//				FilterProvider provider = new FilterProvider(Filter.build(HashMap.class).addInclude("小绿:%s你好呀!"),Filter.build(Filter.class)/* .addExcept("includes") */, Filter.build(null).addExcept("Ignore","小绿").addInclude("date_Time:我要系yyyy年MM月dd日D日既HH:mm:ss打七小绿"));
		//				System.out.println(INSTANCE.writer(f, provider, true, false, true, xx));
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
