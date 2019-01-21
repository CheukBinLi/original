package com.cheuks.bin.original.common.util.xml;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.common.util.conver.CollectionUtil;
import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.common.util.reflection.ClassInfo;
import com.cheuks.bin.original.common.util.reflection.FieldInfo;
import com.cheuks.bin.original.common.util.reflection.ReflectionCache;
import com.cheuks.bin.original.common.util.reflection.ReflectionUtil;

/***
 * 
 * @Title: original-common
 * @Description: model 转 xml 末支持map，待写
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日 下午10:07:49
 *
 */
public class ClassToXml {

	protected ClassToXml() {
	}

	private static ClassToXml INSTANCE;

	private ReflectionCache reflectionCache = ReflectionCache.newInstance();

	private ReflectionUtil reflectionUtil = ReflectionUtil.instance();

	public final static ClassToXml newInstance() {
		if (null == INSTANCE) {
			synchronized (ClassToXml.class) {
				if (null == INSTANCE) {
					INSTANCE = new ClassToXml();
				}
			}
		}
		return INSTANCE;
	}

	public String toXml() throws Throwable {
		//		return recursion(this, null, null);
		StringBuilder result = new StringBuilder();
		recursion(this, result, false, false);
		return result.toString();
	}

	public String toXml(Object o) throws Throwable {
		return toXml(o, false);
	}

	public String toXml(Object o, boolean underscoreCamel) throws Throwable {
		StringBuilder result = new StringBuilder();
		recursion(o, result, false, underscoreCamel);
		return result.toString();
	}

	public String toXml(Object o, boolean withOutNull, boolean underscoreCamel) throws Throwable {
		StringBuilder result = new StringBuilder();
		recursion(o, result, withOutNull, underscoreCamel);
		return result.toString();
	}

	/** 递归 */
	@Deprecated
	@SuppressWarnings("unused")
	private String recursion(final Object o, Class<?> superClass, final List<Field> fieldData) throws Throwable {
		List<Field> fields = null == fieldData ? reflectionCache.getFields4List(null == superClass ? o.getClass() : superClass, true, true) : fieldData;
		StringBuilder result = new StringBuilder();
		Alias alias;
		String tagName;
		Object tempValue;
		Class<?> tempValueClass;
		List<Field> subField;
		List<?> tempList;
		for (Field field : fields) {
			alias = field.getAnnotation(Alias.class);
			tagName = (null != alias && alias.value().length() > 0) ? alias.value() : field.getName();
			result.append("<").append(tagName).append(">");
			tempValue = field.get(o);
			if (!(tempValueClass = field.getType()).isPrimitive() && !reflectionUtil.isWrapperClass(field.getType())) {
				//搜索list(map末实现)
				subField = reflectionUtil.searchCollection(field, true);
				if (null != subField) {
					tempList = (List<?>) tempValue;
					for (Object v : tempList) {
						result.append(recursion(v, null, subField));
					}
				} else {
					result.append(recursion(tempValue, tempValueClass, null));
				}
			} else if (null != tempValue) {
				//                result.append(tempValue);
				result.append("<![CDATA[").append(tempValue).append("]]>");
			}
			result.append("</").append(tagName).append(">");
		}
		return result.toString();
	}

	private void recursion(final Object o, final StringBuilder result, boolean withOutNull, boolean underscoreCamel) throws Throwable {
		if (null == result)
			return;
		ClassInfo classInfo = ClassInfo.getClassInfo(o.getClass());
		if (classInfo.isBasicOrArrays() || classInfo.isCollection() || classInfo.isDate() || classInfo.isSet())
			return;

		String tagName;
		Object tempValue;
		Field field;
		ClassInfo subClassInfo;

		Map<String, FieldInfo> fields = CollectionUtil.newInstance().isEmpty(classInfo.getFields()) ? reflectionUtil.scanClassFieldInfo4Map(classInfo.getClazz(), true, true, true) : classInfo.getFields();

		for (Entry<String, FieldInfo> en : fields.entrySet()) {
			field = en.getValue().getField();
			tagName = en.getValue().getAliasOrFieldName();
			tagName = underscoreCamel ? StringUtil.toLowerCaseUnderscoreCamel(tagName) : tagName;
			tempValue = field.get(o);
			if (withOutNull && null == tempValue)
				continue;
			result.append("<").append(tagName).append(">");
			if (null != tempValue) {
				subClassInfo = ClassInfo.getClassInfo(tempValue.getClass());
				if (subClassInfo.isMapOrSetOrCollection()) {
					recursionSub(tagName, tempValue, result, withOutNull, underscoreCamel);
				} else if (null != tempValue) {
					result.append("<![CDATA[").append(tempValue).append("]]>");
				}
			}
			result.append("</").append(tagName).append(">");
		}
	}

	private void recursionSub(final String tagName, final Object value, final StringBuilder result, boolean withOutNull, boolean underscoreCamel) throws Throwable {
		boolean hasTagName = null != tagName;
		Map<?, ?> map = null;
		Object tempSubValue;
		ClassInfo currentClassInfo = ClassInfo.getClassInfo(value.getClass());
		Collection<?> collection = null;
		ClassInfo subClassInfo;
		Iterator<?> it;
		Entry<?, ?> en;
		String subTagName = tagName;

		if (currentClassInfo.isMap()) {
			map = (Map<?, ?>) value;
			if (map.isEmpty()) {
				return;
			}
			it = map.entrySet().iterator();
		} else if (currentClassInfo.isCollection() || currentClassInfo.isSet()) {
			collection = (Collection<?>) value;
			if (collection.isEmpty()) {
				return;
			}
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

			if (withOutNull && null == tempSubValue) {
				continue;
			}

			subClassInfo = ClassInfo.getClassInfo(tempSubValue.getClass());

			if (subClassInfo.isMapOrSetOrCollection()) {
				recursionSub(hasTagName ? null : subTagName, tempSubValue, result, withOutNull, underscoreCamel);
			} else if (subClassInfo.isBasicOrArrays()) {
				result.append(currentClassInfo.isMap() ? "<" + (subTagName = underscoreCamel ? StringUtil.toUpperCaseFirstOne(subTagName) : subTagName) + "><![CDATA[" + null == tempSubValue ? "" : tempSubValue + "]]></" + subTagName + ">"
						: "<" + tagName + "><![CDATA[" + null == tempSubValue ? "" : tempSubValue + "]]></" + tagName + ">");
			} else {
				recursion(tempSubValue, result, withOutNull, underscoreCamel);
			}
		}
	}

}
