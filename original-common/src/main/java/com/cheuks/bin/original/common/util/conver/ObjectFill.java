package com.cheuks.bin.original.common.util.conver;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.common.annotation.reflect.Replace;
import com.cheuks.bin.original.common.util.reflection.ClassInfo;
import com.cheuks.bin.original.common.util.reflection.FieldInfo;
import com.cheuks.bin.original.common.util.reflection.ReflectionUtil;
import com.cheuks.bin.original.common.util.reflection.Type;

public class ObjectFill {

	protected final static Map<String, Map<String, Field>> FIELDS = new ConcurrentSkipListMap<String, Map<String, Field>>();

	protected final static Map<String, Map<String, Replace>> REPLACE_FIELDS = new ConcurrentSkipListMap<String, Map<String, Replace>>();

	private static volatile SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static volatile SimpleDateFormat dateFormatShort = new SimpleDateFormat("yyyy-MM-dd");

	public ObjectFill setDateFormat(String format) {
		dateFormat.applyPattern(format);
		return this;
	}

	public static void scanClass(Class<?> c) {
		// FIELDS.put(c, fieldsConvertMap(c.getDeclaredFields()));
		try {
			FIELDS.put(c.getName(), ReflectionUtil.instance().scanClassField4Map(c, true, false, false, true));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// public Map<String, Field> fieldsConvertMap(Field... o) {
	// Map<String, Field> map = new HashMap<String, Field>();
	// for (Field f : o) {
	// f.setAccessible(true);
	// map.put(f.getName(), f);
	// }
	// return map;
	// }

	public static final <T> T fillObject(Class<T> t, Map<String, ?> data, DateFormat dateFormat) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		return fillObject(t.newInstance(), data, dateFormat);
	}

	public static final <T> T fillObject(Class<T> t, Map<String, ?> data) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		return fillObject(t.newInstance(), data);
	}

	public static final Map<String, Object> objectToMap(Object o, String... ignore) throws IllegalArgumentException, IllegalAccessException {
		return objectToMap(o, false, ignore);
	}

	public static final Map<String, Object> objectToMap(Object o, boolean useAlias, String... ignore) throws IllegalArgumentException, IllegalAccessException {
		return objectToMap(o, false, false, ignore);
	}

	public static final Map<String, Object> objectToMap(Object o, boolean withOutNull, boolean useAlias, String... ignore) throws IllegalArgumentException, IllegalAccessException {
		return objectToMap(o, withOutNull, useAlias, false, ignore);
	}

	public static final Map<String, Object> objectToMap(Object o, boolean withOutNull, boolean useAlias, boolean UnderscoreCamel, String... ignore)
			throws IllegalArgumentException, IllegalAccessException {
		if (!FIELDS.containsKey(o.getClass().getName()))
			scanClass(o.getClass());
		Map<String, Field> fields = FIELDS.get(o.getClass().getName());
		Map<String, Object> result = new HashMap<String, Object>();
		Alias alias;
		String name = null;
		Set<String> ignoreField = null;
		if (null != ignore && ignore.length > 0) {
			ignoreField = new HashSet<String>(Arrays.asList(ignore));
		}
		Object value = null;
		for (Entry<String, Field> en : fields.entrySet()) {
			if ((null == (value = en.getValue().get(o)) && withOutNull) || null != ignoreField && ignoreField.contains(en.getKey())) {
				continue;
			}
			if (useAlias) {
				alias = en.getValue().getAnnotation(Alias.class);
				if (null != alias) {
					name = alias.value();
					name = name.length() > 0 ? name : en.getKey();
				} else {
					name = en.getKey();
				}
			} else {
				name = en.getKey();
			}
			result.put(UnderscoreCamel ? StringUtil.toLowerCaseUnderscoreCamel(name) : name, value);
		}
		return result;
	}

	public static final String objectToUrlParams(Object o, boolean useAlias, String... ignore) throws IllegalArgumentException, IllegalAccessException {
		if (!FIELDS.containsKey(o.getClass().getName()))
			scanClass(o.getClass());
		Map<String, Field> fields = FIELDS.get(o.getClass().getName());
		StringBuilder result = new StringBuilder();
		Alias alias;
		String name = null;
		Set<String> ignoreField = null;
		if (null != ignore && ignore.length > 0) {
			ignoreField = new HashSet<String>(Arrays.asList(ignore));
		}
		String value;
		for (Entry<String, Field> en : fields.entrySet()) {
			if (null == (value = en.getValue().get(o).toString()) || null != ignoreField && ignoreField.contains(en.getKey())) {
				continue;
			}
			if (useAlias) {
				alias = en.getValue().getAnnotation(Alias.class);
				if (null != alias) {
					name = alias.value();
					name = name.length() > 0 ? name : en.getKey();
					result.append("&").append(name).append("=").append(value);
				}
				continue;
			} else {
				result.append("&").append(en.getKey()).append("=").append(value);
			}
		}
		return result.length() > 0 ? result.substring(1) : "";
	}

	public static final String objectCaseUnderscoreCamelToUrlParams(Object o, boolean underscoreCamel, String... ignore) throws IllegalArgumentException, IllegalAccessException {
		if (!FIELDS.containsKey(o.getClass().getName()))
			scanClass(o.getClass());
		Map<String, Field> fields = FIELDS.get(o.getClass().getName());
		StringBuilder result = new StringBuilder();
		Set<String> ignoreField = null;
		if (null != ignore && ignore.length > 0) {
			ignoreField = new HashSet<String>(Arrays.asList(ignore));
		}
		String value;
		for (Entry<String, Field> en : fields.entrySet()) {
			if (null == (value = en.getValue().get(o).toString()) || null != ignoreField && ignoreField.contains(en.getKey())) {
				continue;
			}
			result.append("&").append(underscoreCamel ? StringUtil.toLowerCaseUnderscoreCamel(en.getKey()) : en.getKey()).append("=").append(value);

		}
		return result.length() > 0 ? result.substring(1) : "";
	}

	public static final <T> T fillObject(T t, Map<String, ?> data, String... ignore) throws IllegalArgumentException, IllegalAccessException {
		return fillObject(t, data, null, ignore);
	}

	public static final <T> T fillObject(T t, Map<String, ?> data, DateFormat dateFormat, String... ignore) throws IllegalArgumentException, IllegalAccessException {
		// System.out.println(t.getClass());
		Class<?> c = t.getClass();
		if (!FIELDS.containsKey(c.getName()))
			scanClass(c);
		Map<String, Field> fields = FIELDS.get(c.getName());
		@SuppressWarnings("unchecked")
		Set<String> ignores = null == ignore ? CollectionUtil.EMPTY_SET : new HashSet<String>(Arrays.asList(ignore));
		Object value;
		Field field;
		for (Entry<String, ?> en : data.entrySet()) {
			value = en.getValue();
			if (null == value || ignores.contains(en.getKey()) || null == (field = fields.get(en.getKey())))
				continue;
			try {
				field.set(t, getValue(field.getType(), value, dateFormat));
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return t;
	}

	public static Object getValue(Class<?> c, Object data, DateFormat dateFormat) {
		String simpleName = c.getSimpleName();
		boolean isArray = data.getClass().isArray();
		try {
			if (c.isArray()) {
				System.err.println("数组末实现");
				return null;
			} else if (simpleName.equalsIgnoreCase("String"))
				return getFirstValue(isArray, data);
			else if (simpleName.equalsIgnoreCase("boolean") || simpleName.equalsIgnoreCase("Boolean"))
				return Boolean.valueOf(getFirstValue(isArray, data));
			else if (simpleName.equalsIgnoreCase("int") || simpleName.equalsIgnoreCase("Integer"))
				return Integer.valueOf(getFirstValue(isArray, data));
			else if (simpleName.equalsIgnoreCase("byte"))
				return Byte.valueOf(getFirstValue(isArray, data));
			else if (simpleName.equalsIgnoreCase("char") || simpleName.equalsIgnoreCase("Character"))
				return Character.valueOf(getFirstValue(isArray, data).charAt(0));
			else if (simpleName.equalsIgnoreCase("double"))
				return Double.valueOf(getFirstValue(isArray, data));
			else if (simpleName.equalsIgnoreCase("long"))
				return Long.valueOf(getFirstValue(isArray, data));
			else if (simpleName.equalsIgnoreCase("short"))
				return Short.valueOf(getFirstValue(isArray, data));
			else if (simpleName.equalsIgnoreCase("float"))
				return Float.valueOf(getFirstValue(isArray, data));
			else if (simpleName.equalsIgnoreCase("Date")) {
				return stringToDate(data.toString(), dateFormat);
			}
			return data;
		} catch (Exception e) {
			return null;
		}
	}

	static Date stringToDate(String date, DateFormat dateFormat) {
		Date result = null;
		try {
			if (null != dateFormat)
				result = dateFormat.parse(date);
			else
				result = ObjectFill.dateFormat.parse(date);
		} catch (ParseException e) {
			try {
				if (null != dateFormat)
					result = ObjectFill.dateFormat.parse(date);
				else
					result = ObjectFill.dateFormatShort.parse(date);
			} catch (ParseException e1) {
				return null;
			}
		}
		return result;
	}

	public Object getValue(Class<?> c, Object data) {
		return getValue(c, data, dateFormat);
	}

	private static String getFirstValue(boolean isArray, Object data) {
		return isArray ? ((String[]) data)[0] : data.toString();
	}

	public static void xcopy(final Object source, final Object target, boolean notNull, boolean notTransient, String... ignores) throws Exception {
		if (null == source || null == target)
			return;
		final ClassInfo a = ClassInfo.getClassInfo(source.getClass());
		if (a.isMapOrSetOrCollection() || a.isArrays() || a.isBasicOrArrays())
			return;

		final ClassInfo b = ClassInfo.getClassInfo(target.getClass());
		if (b.isMapOrSetOrCollection() || b.isArrays() || b.isBasicOrArrays())
			return;
		if (null == a.getFields())
			a.setFields(ReflectionUtil.instance().scanClassFieldInfo4Map(a.getClazz(), true, true, true));
		if (null == b.getFields())
			b.setFields(ReflectionUtil.instance().scanClassFieldInfo4Map(b.getClazz(), true, true, true));

		Set<String> igonre = null == ignores ? null : new HashSet<String>(Arrays.asList(ignores));
		Object value = null;
		for (Entry<String, FieldInfo> en : a.getFields().entrySet()) {
			if (null != igonre && igonre.contains(en.getKey())) {
				continue;
			}
			if ((notTransient && en.getValue().isTransient()) || (null == (value = en.getValue().getField().get(source)) && notNull)) {
				continue;
			}
			FieldInfo fieldInfo = b.getFields().get(en.getKey());
			if (null != fieldInfo) {
				fieldInfo.getField().set(target, value);
			}
		}
	}
	
	public static <T> T replaceValue(T t, Replace... replaces) throws  Throwable {
		if (null == t)
			return t;
		final ClassInfo a = ClassInfo.getClassInfo(t.getClass());
		if (a.isMapOrSetOrCollection() || a.isArrays() || a.isBasicOrArrays())
			return t;

		if (null == a.getFields())
			a.setFields(ReflectionUtil.instance().scanClassFieldInfo4Map(a.getClazz(), true, true, true));
		
//		REPLACE_FIELDS.containsKey(t.getClass().getName());
		
		Map<String, Replace> replaceField = REPLACE_FIELDS.get(t.getClass().getName());
		if (null == replaceField) {
			replaceField = new HashMap<String, Replace>();
			Replace replace;
			Field field;
			for (Entry<String, FieldInfo> item : a.getFields().entrySet()) {
				field = item.getValue().getField();
				replace = field.getDeclaredAnnotation(Replace.class);
				if (null == replace)
					continue;
				replaceField.put(field.getName(), replace);
				Object value = null == (value = field.get(t)) ? value : Type.valueToString(value, ClassInfo.getClassInfo(value.getClass()), null, false);
				
				field.set(Type.getValue(item.getValue().getField().getClass(), field.get(t).toString(), null), value);
			}
			REPLACE_FIELDS.put(t.getClass().getName(), replaceField);
			return t;
		}
		
//		for

		return null;
	}

}
