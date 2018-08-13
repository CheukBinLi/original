package com.cheuks.bin.original.common.util.reflection;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

public enum Type {
					StringType(String.class),
					PrimitiveInt(int.class),
					PrimitiveBoolean(boolean.class),
					PrimitiveChar(char.class),
					PrimitiveShort(short.class),
					PrimitiveLong(long.class),
					PrimitiveFloat(float.class),
					PrimitiveByte(byte.class),
					PrimitiveDouble(double.class),
					PackageInteger(Integer.class),
					PackageBoolean(Boolean.class),
					PackageCharacter(Character.class),
					PackageShort(Short.class),
					PackageLong(Long.class),
					PackageFloat(Float.class),
					PackageByte(Byte.class),
					PackageDouble(Double.class),
					Array(Arrays.class),
					Map(Map.class),
					Set(Set.class),
					Date(java.util.Date.class, java.sql.Date.class, java.sql.Timestamp.class, java.sql.Time.class),
					Collection(RandomAccess.class, Collection.class, List.class, Set.class);

	Class<?>[] types;

	static final List<Class<?>> COLLECTION = Arrays.asList(RandomAccess.class, Collection.class, List.class, Set.class);
	static final List<Class<?>> DATE = Arrays.asList(java.util.Date.class, java.sql.Date.class, java.sql.Timestamp.class, java.sql.Time.class);
	static final Set<Class<?>> WRAPPER = new HashSet<Class<?>>(Arrays.asList(String.class, Integer.class, Boolean.class, Character.class, Short.class, Long.class, Float.class, Byte.class, Double.class));

	Type(Class<?>... clazz) {

		this.types = clazz;
	}

	public Class<?> getType() {
		return this.types[0];
	}

	public static Type getTypeByClass(final Class<?> clazz) {
		List<Class<?>> interfaces = Arrays.asList(clazz.getInterfaces());
		for (Type type : Type.values()) {
			for (Class<?> dataType : type.types) {
				if (interfaces.contains(dataType) || dataType.equals(clazz))
					return type;
			}
		}
		return null;
	}

	public static boolean isDate(final Class<?> clazz) {
		List<Class<?>> interfaces = Arrays.asList(clazz.getInterfaces());
		if (DATE.contains(clazz))
			return true;
		for (Class<?> c : interfaces) {
			if (DATE.contains(c))
				return true;
		}
		return false;
	}

	public static boolean isWrapper(final Class<?> clazz) {
		List<Class<?>> interfaces = Arrays.asList(clazz.getInterfaces());
		if (WRAPPER.contains(clazz))
			return true;
		for (Class<?> c : interfaces) {
			if (WRAPPER.contains(c))
				return true;
		}
		return false;
	}

	public static Type getTypeByValue(final Object value) {
		return getTypeByClass(value.getClass());
	}

	public static boolean isMapByClass(Class<?> clazz) {
		List<Class<?>> interfaces = Arrays.asList(clazz.getInterfaces());
		return interfaces.contains(Map.class);
	}

	public static boolean isSetByClass(Class<?> clazz) {
		List<Class<?>> interfaces = Arrays.asList(clazz.getInterfaces());
		return interfaces.contains(Set.class);
	}

	public static boolean isMapByValue(Object value) {
		return isMapByClass(value.getClass());
	}

	public static boolean isCollectionByClass(Class<?> clazz) {
		List<Class<?>> interfaces = Arrays.asList(clazz.getInterfaces());
		if (COLLECTION.contains(clazz))
			return true;
		for (Class<?> c : interfaces) {
			if (COLLECTION.contains(c))
				return true;
		}
		return false;
	}

	public static boolean isCollectionByValue(Object value) {
		return isCollectionByClass(value.getClass());
	}

	public static String valueToString4Json(final Object value, final ClassInfo classInfo, boolean filterSpecialCharacters) throws IllegalArgumentException, IllegalAccessException {
		return valueToString4Json(value, null, classInfo, filterSpecialCharacters);
	}

	public static String valueToString4Json(final Object value, final String format, final ClassInfo classInfo, boolean filterSpecialCharacters) throws IllegalArgumentException, IllegalAccessException {
		if (null == value)
			return "null";
		switch (classInfo.getType()) {
		case StringType:
			return "\"" + stringFormat(format, (String) value) + "\"";
		case PrimitiveChar:
			return "\"" + stringFormat(format, Character.toString((char) value)) + "\"";
		case PackageCharacter:
			return "\"" + stringFormat(format, value.toString()) + "\"";
		default:
			return valueToString(value, format, classInfo, filterSpecialCharacters);
		}
	}

	public static String valueToString(final Object value, final ClassInfo classInfo, boolean filterSpecialCharacters) throws IllegalArgumentException, IllegalAccessException {
		return valueToString(value, null, classInfo, filterSpecialCharacters);
	}

	public static String valueToString(final Object value, final String format, final ClassInfo classInfo, boolean filterSpecialCharacters) throws IllegalArgumentException, IllegalAccessException {
		if (null == value)
			return "null";
		switch (classInfo.getType()) {
		case StringType:
			return stringFormat(format, valueConver((String) value, filterSpecialCharacters));
		case PrimitiveInt:
			return stringFormat(format, Integer.toString((int) value));
		case PrimitiveBoolean:
			return stringFormat(format, Boolean.toString((boolean) value));
		case PrimitiveChar:
			return stringFormat(format, valueConver(Character.toString((char) value), filterSpecialCharacters));
		case PrimitiveShort:
			return stringFormat(format, Short.toString((short) value));
		case PrimitiveLong:
			return stringFormat(format, Long.toString((long) value));
		case PrimitiveFloat:
			return stringFormat(format, Float.toString((float) value));
		case PrimitiveByte:
			return stringFormat(format, Byte.toString((byte) value));
		case PrimitiveDouble:
			return stringFormat(format, Double.toString((double) value));
		case PackageInteger:
			return stringFormat(format, value.toString());
		case PackageBoolean:
			return stringFormat(format, value.toString());
		case PackageCharacter:
			return stringFormat(format, value.toString());
		case PackageShort:
			return stringFormat(format, value.toString());
		case PackageLong:
			return stringFormat(format, value.toString());
		case PackageFloat:
			return stringFormat(format, value.toString());
		case PackageByte:
			return stringFormat(format, value.toString());
		case PackageDouble:
			return stringFormat(format, value.toString());
		case Array:
			return Arrays.toString((Object[]) value);
		default:
			return value.toString();
		}

	}

	public static String nullToJson(String name) {
		return "\"" + name + "\":null";
	}

	static String valueConver(char... values) {
		StringBuilder result = new StringBuilder();
		for (char item : values) {
			switch (item) {
			case '\"':
				result.append("\\\"");
				break;
			case '\r':
				result.append("\\r");
				break;
			case '\n':
				result.append("\\n");
			case '\t':
				result.append("\\t");
				break;
			default:
				result.append(item);
			}
		}
		return result.toString();
	}

	static String valueConver(String values, boolean filterSpecialCharacters) {
		if (!filterSpecialCharacters || null == values || values.length() < 1)
			return values;
		return valueConver(values.toCharArray());
	}

	static String stringFormat(String format, String value) {
		return (null == format || null == value) ? value : String.format(format, value);
	}

	public static String valueToJson(String name, final Object value, final ClassInfo field, boolean filterSpecialCharacters) throws IllegalArgumentException, IllegalAccessException {
		return valueToJson(name, value, null, field, filterSpecialCharacters);
	}

	public static String valueToJson(String name, final Object value, final String format, final ClassInfo field, boolean filterSpecialCharacters) throws IllegalArgumentException, IllegalAccessException {
		name = (null == name ? "" : ("\"" + name + "\":"));
		if (null == value) {
			return "\"" + name + "\":null";
		}
		switch (field.getType()) {
		case StringType:
			//			return name + "\"" + value.toString().replaceAll("\"", "\\\"") + "\"";
			return name + "\"" + stringFormat(format, valueConver(value.toString(), filterSpecialCharacters)) + "\"";
		case PrimitiveInt:
			return name + stringFormat(format, Integer.toString((int) value));
		case PrimitiveBoolean:
			return name + stringFormat(format, Boolean.toString((boolean) value));
		case PrimitiveChar:
			//			return name + "\"" + Character.toString((char) value).replaceAll("\"", "\\\"") + "\"";
			return name + "\"" + stringFormat(format, valueConver(Character.toString((char) value), filterSpecialCharacters)) + "\"";
		case PrimitiveShort:
			return name + stringFormat(format, Short.toString((short) value));
		case PrimitiveLong:
			return name + stringFormat(format, stringFormat(format, Long.toString((long) value)));
		case PrimitiveFloat:
			return name + stringFormat(format, Float.toString((float) value));
		case PrimitiveByte:
			return name + stringFormat(format, Byte.toString((byte) value));
		case PrimitiveDouble:
			return name + stringFormat(format, Double.toString((double) value));
		case PackageInteger:
			return name + stringFormat(format, value.toString());
		case PackageBoolean:
			return name + stringFormat(format, value.toString());
		case PackageCharacter:
			//			return name + "\"" + value.toString().replaceAll("\"", "\\\"") + "\"";
			return name + "\"" + stringFormat(format, valueConver(((Character) value).toString(), filterSpecialCharacters)) + "\"";
		case PackageShort:
			return name + stringFormat(format, value.toString());
		case PackageLong:
			return name + stringFormat(format, value.toString());
		case PackageFloat:
			return name + stringFormat(format, value.toString());
		case PackageByte:
			return name + stringFormat(format, value.toString());
		case PackageDouble:
			return name + stringFormat(format, value.toString());
		case Array:
			if (value instanceof String[] || value instanceof Character[] || value instanceof char[]) {
				Object[] a = (Object[]) value;
				int iMax = a.length - 1;
				if (iMax == -1)
					return name + "[]";

				StringBuilder b = new StringBuilder();
				b.append(name + "[");
				for (int i = 0;; i++) {
					b.append("\"").append(valueConver(String.valueOf(a[i]), filterSpecialCharacters)).append("\"");
					if (i == iMax)
						return b.append(']').toString();
					b.append(", ");
				}
			} else {
				return Arrays.toString((Object[]) value);
			}
		default:
			return value.toString();
		}
	}

	/***
	 * 不理会空格
	 * 
	 * @param c
	 * @param data
	 * @param dateFormat
	 * @return
	 */
	public Object getValue(Class<?> c, String data, DateFormat dateFormat) {
		ClassInfo classInfo = ClassInfo.getClassInfo(c);
		switch (classInfo.getType()) {
		case StringType:
			return data;
		case PrimitiveInt:
			return Integer.valueOf(data);
		case PrimitiveBoolean:
			return Boolean.valueOf(data);
		case PrimitiveChar:
			return null == data ? null : data.length() > 0 ? data.charAt(0) : null;
		case PrimitiveShort:
			return Short.valueOf(data);
		case PrimitiveLong:
			return Long.valueOf(data);
		case PrimitiveFloat:
			return Float.valueOf(data);
		case PrimitiveByte:
			return Byte.valueOf(data);
		case PrimitiveDouble:
			return Double.valueOf(data);
		case PackageInteger:
			return Integer.valueOf(data);
		case PackageBoolean:
			return Boolean.valueOf(data);
		case PackageCharacter:
			return null == data ? null : data.length() > 0 ? data.charAt(0) : null;
		case PackageShort:
			return Short.valueOf(data);
		case PackageLong:
			return Long.valueOf(data);
		case PackageFloat:
			return Float.valueOf(data);
		case PackageByte:
			return Byte.valueOf(data);
		case PackageDouble:
			return Double.valueOf(data);
		case Date:
			System.err.println("日期末实现");//子过滤
			return null;
		case Array:
			System.err.println("数组末实现");//再增加返回数组方法
			return null;
		default:
			return data;
		}

	}

	public static String valueToJson(final Object value, final ClassInfo field) throws IllegalArgumentException, IllegalAccessException {
		return valueToJson(field.getName(), value, field, true);
	}

	public static void main(String[] args) {

		Integer[] a = new Integer[] { 1, 2, 3, 5, 6 };
		int[] c = new int[] { 1, 2, 3, 5, 6 };
		String[] b = new String[] { "1", "2", "3", "5", "6" };
		System.out.println(a instanceof Integer[]);
		System.out.println(c instanceof int[]);
		System.out.println(Arrays.toString((Object[]) a));
		System.out.println(Arrays.toString((String[]) b));

		System.out.println(isWrapper(String.class));

		System.out.println("a\ra   \\ra\\rb");
	}
}
