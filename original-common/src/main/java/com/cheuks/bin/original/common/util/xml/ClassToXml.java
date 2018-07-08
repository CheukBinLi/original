package com.cheuks.bin.original.common.util.xml;

import java.lang.reflect.Field;
import java.util.List;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
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
		return recursion(this, null, null);
	}

	public String toXml(Object o) throws Throwable {
		return recursion(o, null, null);
	}

	/** 递归 */
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

}
