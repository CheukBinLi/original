package com.cheuks.bin.original.common.util.conver;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.common.util.reflection.ClassInfo;
import com.cheuks.bin.original.common.util.reflection.FieldInfo;
import com.cheuks.bin.original.common.util.reflection.ReflectionUtil;
import com.cheuks.bin.original.common.util.reflection.Type;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

public class ObjectFill {

	protected final static Map<String, Map<String, Field>> FIELDS = new ConcurrentSkipListMap<String, Map<String, Field>>();

	private static volatile SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static volatile SimpleDateFormat defaultDateFormatShort = new SimpleDateFormat("yyyy-MM-dd");

	public ObjectFill setDateFormat(String format) {
		defaultDateFormat.applyPattern(format);
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

	public static final Map<String, String> objectToMapString(Object o, boolean withOutNull, boolean useAlias, String dateFormat, String... ignore) throws IllegalArgumentException, IllegalAccessException {
		return castObject(objectToMap(o, withOutNull, useAlias, false, true, dateFormat, ignore));
	}

	public static final Map<String, Object> objectToMap(Object o, boolean withOutNull, boolean useAlias, boolean underscoreCamel, String... ignore) throws IllegalArgumentException, IllegalAccessException{
		return objectToMap(o, withOutNull, useAlias, underscoreCamel, false, null, ignore);
	}

	public static final Map<String, Object> objectToMap(Object o, boolean withOutNull, boolean useAlias, boolean UnderscoreCamel, boolean toString, String dateFormat, String... ignore)
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
		DateFormat currentDateFormat = StringUtil.isBlank(dateFormat) ? ObjectFill.defaultDateFormat : new SimpleDateFormat(dateFormat);
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
			if (toString) {
				if (value instanceof Date) {
					value = currentDateFormat.format((Date) value);
				} else {
					Type.valueToString(value, null, ClassInfo.getClassInfo(value.getClass()), null, false);
				}
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
				result = ObjectFill.defaultDateFormat.parse(date);
		} catch (ParseException e) {
			try {
				if (null != dateFormat)
					result = ObjectFill.defaultDateFormat.parse(date);
				else
					result = ObjectFill.defaultDateFormatShort.parse(date);
			} catch (ParseException e1) {
				return null;
			}
		}
		return result;
	}

	public Object getValue(Class<?> c, Object data) {
		return getValue(c, data, defaultDateFormat);
	}

	private static String getFirstValue(boolean isArray, Object data) {
		return isArray ? ((String[]) data)[0] : data.toString();
	}
	
    public static interface Process<T, U> {

        default U before(U u){ return u; }

		default T after(T t){ return t; }
    }
	
	public static <S,T> List<T> xcopy(final List<S> sources, final Class<T> target, Process<T,S> process, boolean notNull, boolean notTransient, String... ignores) throws Exception {

		if (CollectionUtil.isEmpty(sources) || null == target)
			return null;
		final ClassInfo a = ClassInfo.getClassInfo(sources.get(0).getClass());
		if (a.isMapOrSetOrCollection() || a.isArrays() || a.isBasicOrArrays())
			return null;

		final ClassInfo b = ClassInfo.getClassInfo(target);
		if (b.isMapOrSetOrCollection() || b.isArrays() || b.isBasicOrArrays())
			return null;
		if (null == a.getFields())
			a.setFields(ReflectionUtil.instance().scanClassFieldInfo4Map(a.getClazz(), true, true, true));
		if (null == b.getFields())
			b.setFields(ReflectionUtil.instance().scanClassFieldInfo4Map(b.getClazz(), true, true, true));

		Set<String> igonre = null == ignores ? null : new HashSet<String>(Arrays.asList(ignores));
		Object value = null;
		T obj;
		List<T> result =new ArrayList<T>();
		for (S item : sources) {
			if (null != process)
				item = process.before(item);
			if (null == item)
				continue;
			obj = target.newInstance();
			for (Entry<String, FieldInfo> en : a.getFields().entrySet()) {
				if (null != igonre && igonre.contains(en.getKey())) {
					continue;
				}
				if ((notTransient && en.getValue().isTransient()) || (null == (value = en.getValue().getField().get(item)) && notNull)) {
					continue;
				}
				FieldInfo fieldInfo = b.getFields().get(en.getKey());
				if (null != fieldInfo) {
					fieldInfo.getField().set(obj, value);
				}
			}
			if (null != process)
				obj = process.after(obj);
			if (null == obj)
				continue;
			result.add(obj);
		}
		return result;
		
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
	
	public static <T> T replaceValue(T t, String oldStr, String newStr, String... ignore) throws Throwable {
		if (null == t) {
			return t;
		}
		ClassInfo classInfo = ClassInfo.getClassInfo(t.getClass());
		if (CollectionUtil.isEmpty(classInfo.getFields()))
			classInfo.setFields(ReflectionUtil.instance().scanClassFieldInfo4Map(classInfo.getClazz(), true, true, true));
		Field field;
		String value;
		Set<String> ignores = null == ignore ? null : new HashSet<>(Arrays.asList(ignore));
		for (Entry<String, FieldInfo> item : classInfo.getFields().entrySet()) {
			if (CollectionUtil.isNotEmpty(ignores) && ignores.contains(item.getKey()))
				continue;
			field = (null == item.getValue()) ? null : item.getValue().getField();
			if (null == field || !String.class.isAssignableFrom(item.getValue().getField().getType())) {
				continue;
			}
			value = (String) field.get(t);
			if (null == value) {
				continue;
			}
			field.set(t, value.replace(oldStr, newStr));
		}

		return t;
	}

	static <T> T castObject(Object obj) {
		return (T) obj;
	}
}
