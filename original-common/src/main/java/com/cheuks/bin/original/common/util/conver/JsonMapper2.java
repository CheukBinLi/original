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

@Deprecated
@SuppressWarnings("unchecked")
public class JsonMapper2 {

	final static Logger LOG = LoggerFactory.getLogger(JsonMapper2.class);

	protected static final String DEFAULT_EOL = "\r\n";

	protected static final String COMPACT_EOL = "";

	private ReflectionUtil reflectionUtil = ReflectionUtil.instance();

	private static JsonMapper2 INSTANCE = new JsonMapper2();

	private volatile SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private volatile JsonMapperPropertyInclusion defaultPropertyInclusion = JsonMapperPropertyInclusion.ALWAYS;

	public SimpleDateFormat getDefaultFormat() {
		return defaultFormat;
	}

	public JsonMapper2 setDefaultFormat(String format) {
		this.defaultFormat = new SimpleDateFormat(format);
		return this;
	}

	public JsonMapperPropertyInclusion getDefaultPropertyInclusion() {
		return defaultPropertyInclusion;
	}

	public JsonMapper2 setDefaultPropertyInclusion(JsonMapperPropertyInclusion defaultPropertyInclusion) {
		this.defaultPropertyInclusion = defaultPropertyInclusion;
		return this;
	}

	public void setDefaultFormat(SimpleDateFormat defaultFormat) {
		this.defaultFormat = defaultFormat;
	}

	protected JsonMapper2() {
	}

	public static JsonMapper2 newInstance(boolean isSingle) {
		if (isSingle) {
			if (null == INSTANCE) {
				synchronized (JsonMapper2.class) {
					if (null == INSTANCE) {
						INSTANCE = new JsonMapper2();
					}
				}
			}
			return INSTANCE;
		}
		return new JsonMapper2();
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

	public String writer(final Object o, FilterProvider filterProvider, boolean withAlias, boolean withOutTransient, boolean filterSpecialCharacters) throws Exception {
		return writerString(o, filterProvider, withAlias, withOutTransient, true, filterSpecialCharacters);
	}

	public String writer(final Object o, FilterProvider filterProvider, boolean withAlias, boolean withOutTransient) throws Exception {
		return writerString(o, filterProvider, withAlias, withOutTransient, true, false);
	}

	public String writer(final Object o, FilterProvider filterProvider, boolean withAlias, boolean withOutTransient, boolean eol, Map<String, ? extends Object>... additionalAttributes) throws Exception {
		return writer(o, filterProvider, withAlias, withOutTransient, eol, false, additionalAttributes);
	}

	public String writer(final Object o, FilterProvider filterProvider, boolean withAlias, boolean withOutTransient, boolean eol, boolean filterSpecialCharacters, Map<String, ? extends Object>... additionalAttributes) throws Exception {
		StringBuilder additional = null;
		String subResult;
		if (null != additionalAttributes && additionalAttributes.length > 0) {
			additional = new StringBuilder();
			for (Map<String, ? extends Object> attributes : additionalAttributes) {
				//				recursionSub(null, attributes, subResult, filterProvider, withAlias, withOutTransient);
				subResult = writerString(attributes, filterProvider, withAlias, withOutTransient, false, filterSpecialCharacters);
				additional.append(subResult.length() > 0 ? (subResult.toString() + ",") : COMPACT_EOL);
			}
			additional.setLength(additional.length() - 1);
		}
		String result = writerString(o, filterProvider, withAlias, withOutTransient, false, filterSpecialCharacters);
		return (null == additional ? "{" + result + "}" : "{" + result + (result.length() > 0 ? "," : COMPACT_EOL) + additional.toString() + "}") + (eol ? DEFAULT_EOL : COMPACT_EOL);
	}

	StringBuilder checkBrackets(StringBuilder json, char startWith, char endWith, String startValue, String endValue) {
		if (json.charAt(0) != startWith) {
			json.insert(0, startValue);
		}
		if (json.charAt(json.length() - 1) != endWith) {
			json.append(endValue);
		}
		return json;
	}

	String startWith(StringBuilder json, char startWith, String withValue, String withoutValue) {
		if (json.length() < 1)
			return withoutValue;
		return json.charAt(0) != startWith ? withValue : withoutValue;
	}

	String endWith(StringBuilder json, char endWith, String withValue, String withoutValue) {
		if (json.length() < 1)
			return withValue;
		return json.charAt(json.length() - 1) != endWith ? withValue : withoutValue;
	}

	String checkAndPackerJson(StringBuilder json, String start, String end, String startValue, String endValue) {
		String defaultStart = null == startValue ? start : startValue;
		String defaultEnd = null == endValue ? end : endValue;
		if (json.charAt(0) != '{') {
			json.insert(0, defaultStart);
		}
		if (json.charAt(json.length() - 1) != '}') {
			json.append(defaultEnd);
		}
		return json.toString();
	}

	String checkAndPackerJson(String json, String start, String end, String startValue, String endValue) {
		String defaultStart = null == startValue ? start : startValue;
		String defaultEnd = null == endValue ? end : endValue;
		String empty = "";
		return (json.startsWith(start) ? empty : defaultStart) + json + (json.endsWith(end) ? empty : defaultEnd);
	}

	protected String writerString(final Object o, FilterProvider filterProvider, boolean withAlias, boolean withOutTransient, boolean brackets, boolean filterSpecialCharacters) throws Exception {
		if (null == o) {
			return brackets ? "{}" : COMPACT_EOL;
		}
		ClassInfo classInfo = ClassInfo.getClassInfo(o.getClass());
		StringBuilder result;
		if (classInfo.isBasicOrArrays()) {
			/** 基本类型 */
			return brackets ? checkAndPackerJson(Type.valueToString(o, classInfo, null, filterSpecialCharacters), "{", "}", "{\"", "\"}") : "\"" + Type.valueToString(o, classInfo, null, filterSpecialCharacters) + "\"";
		} else if (classInfo.isDate()) {
			/** 日期 */
			return brackets ? "{\"" + defaultFormat.format(o) + "\"}" : "\"" + defaultFormat.format(o) + "\"";
		} else if (classInfo.isMapOrSetOrCollection()) {
			/** 集合 */
			recursionSub(null, o, result = new StringBuilder(), filterProvider, withAlias, withOutTransient, filterSpecialCharacters);
		} else {
			result = new StringBuilder("{");
			recursion(o, filterProvider, result, withAlias, withOutTransient, filterSpecialCharacters);
			result.append("}");
		}
		if (brackets)
			return result.toString();
		return result.substring(1, result.length() - 1);
	}

	private void recursion(Object o, FilterProvider filterProvider, final StringBuilder result, boolean withAlias, boolean withOutTransient, boolean filterSpecialCharacters) throws Exception {
		ClassInfo currentClassInfo = ClassInfo.getClassInfo(o.getClass());
		ClassInfo subClassInfo;
		String tagName;
		Object tempValue;
		FieldInfo fieldInfo;
		Filter currentClazz = null == filterProvider ? null : filterProvider.getFilterByClass(o.getClass());
		Filter filterAll = null == filterProvider ? null : filterProvider.getFilterByClass(null);
		if (null == o || currentClassInfo.isBasicOrArrays()) {
			/** 基本类型 */
			result.append(Type.valueToString(o, currentClassInfo, null, filterSpecialCharacters));
			return;
		} else if (currentClassInfo.isDate()) {
			/** 日期 */
			result.append(defaultFormat.format(o));
		} else if (currentClassInfo.isMapOrSetOrCollection()) {
			/** 集合 */
			recursionSub(null, o, result, filterProvider, withAlias, withOutTransient, filterSpecialCharacters);
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
				if (subClassInfo.isMapOrSetOrCollection()) {
					recursionSub(tagName, tempValue, result, filterProvider, withAlias, withOutTransient, filterSpecialCharacters);
				} else if (subClassInfo.isBasicOrArrays()) {
					result.append(Type.valueToJson(tagName, tempValue, FilterProvider.getCurrentValueFormat(tagName, currentClazz, filterAll), subClassInfo, null, filterSpecialCharacters));
				} else if (subClassInfo.isDate()) {
					String valueFormat = FilterProvider.getCurrentValueFormat(tagName, currentClazz, filterAll);
					result.append("\"").append(tagName).append("\":\"").append(null == valueFormat ? defaultFormat.format(tempValue) : new SimpleDateFormat(valueFormat).format(tempValue)).append("\"");
				} else {
					result.append("\"").append(tagName).append("\":").append(startWith(result, '{', "", "{"));
					recursion(tempValue, filterProvider, result, withAlias, withOutTransient, filterSpecialCharacters);
					result.append(endWith(result, '}', "", "}"));
				}
				result.append(",");
			}
		}
		if (result.length() > 1)
			result.setLength(result.length() - 1);
	}

	private void recursionSub(final String tagName, final Object value, final StringBuilder result, FilterProvider filterProvider, boolean withAlias, boolean withOutTransient, boolean filterSpecialCharacters) throws Exception {
		int len;
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
			result.append(startWith(result, '{', "", "{"));
			end = "}";
			it = map.entrySet().iterator();
		} else if (currentClassInfo.isCollection() || currentClassInfo.isSet()) {
			collection = (Collection<?>) value;
			if (collection.isEmpty()) {
				result.setLength(result.length() - 1);
				result.append(":null");
				return;
			}
			result.append(startWith(result, '[', "", "["));
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
				if (currentClassInfo.isMap()) {
					result.append(Type.nullToJson(subTagName)).append(",");
				}
				continue;
			}

			subClassInfo = ClassInfo.getClassInfo(tempSubValue.getClass());

			if (subClassInfo.isMapOrSetOrCollection()) {
				recursionSub(hasTagName ? null : subTagName, tempSubValue, result, filterProvider, withAlias, withOutTransient, filterSpecialCharacters);
			} else if (subClassInfo.isBasicOrArrays()) {
				valueFormat = FilterProvider.getCurrentValueFormat(currentClassInfo.isMap() ? subTagName : tagName, currentClazz, filterAll);
				result.append(currentClassInfo.isMap() ? Type.valueToJson(subTagName, tempSubValue, valueFormat, subClassInfo, null, filterSpecialCharacters) : Type.valueToString4Json(tempSubValue, valueFormat, subClassInfo, null, filterSpecialCharacters));
			} else if (subClassInfo.isDate()) {
				valueFormat = FilterProvider.getCurrentValueFormat(subTagName, currentClazz, filterAll);
				result.append("\"").append(subTagName).append("\":\"").append(null == valueFormat ? defaultFormat.format(tempSubValue) : new SimpleDateFormat(valueFormat).format(tempSubValue)).append("\"");
			} else {
				result.append(startWith(result, '{', "", "{"));
				len = result.length();
				recursion(tempSubValue, filterProvider, result, withAlias, withOutTransient, filterSpecialCharacters);
				if (result.length() > len) {
					result.append(endWith(result, '}', "", "}"));
				}
			}
			result.append(",");
		}
		result.setLength(result.length() - 1);
		result.append(endWith(result, end.charAt(0), "", end));
	}

	public static void main(String[] args) throws Throwable {
		//		long now = System.currentTimeMillis();
		//		//		Filter f = new Filter(ClassInfo.class, "a", "b", "c", "e", "f", "g");
		//		Filter f = Filter.build(ClassInfo.class).addExcept("a", "b", "c", "e", "f", "g").addInclude("小绿:aaa", "Ignore");
		//		List<Filter> list = new LinkedList<>();
		//		list.add(f);
		//		Map<String, Object> xx = new HashMap<>();
		//		xx.put("oh shit", list);
		//		xx.put("oh shit", "aaaaaaaaaaaa\"\"aaaaaaaaa");
		//		xx.put("date_Time", new Date());
		//		xx.put("Ignore", "哇哈哈");
		//		//		xx.put("Ignore", "\"哇1哈哈\"");
		//		//		//						//
		//		//
		//		FilterProvider provider = new FilterProvider(Filter.build(HashMap.class).addInclude("小绿:%s你好呀!"), Filter.build(Filter.class)/* .addExcept("includes") */, Filter.build(null).addExcept("Ignore", "小绿").addInclude("date_Time:我要系yyyy年MM月dd日D日既HH:mm:ss打七小绿"));
		//		//		System.out.println(INSTANCE.writer(f, provider, true, false, true, true, xx));
		//		System.out.println(INSTANCE.writer(list, null, false, false, true, true));
		//				System.out.println(INSTANCE.writeToString(list, null) + "   " + (System.currentTimeMillis() - now));
		//				now = System.currentTimeMillis();
		//				System.out.println(INSTANCE.writeToString(list, null) + "   " + (System.currentTimeMillis() - now));
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
