package com.cheuks.bin.original.common.util.reflection;

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
					Collection(RandomAccess.class, Collection.class, List.class, Set.class);

	Class<?>[] types;

	static final List<Class<?>> COLLECTION = Arrays.asList(RandomAccess.class, Collection.class, List.class, Set.class);
	static final Set<Class<?>> WRAPPER = new HashSet<Class<?>>(Arrays.asList(String.class, Integer.class, Boolean.class, Character.class, Short.class, Long.class, Float.class, Byte.class));

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

	public static String valueToString4Json(final Object value, final ClassInfo classInfo) throws IllegalArgumentException, IllegalAccessException {
		if (null == value)
			return "null";
		switch (classInfo.getType()) {
		case StringType:
			return "\"" + (String) value + "\"";
		case PrimitiveChar:
			return "\"" + Character.toString((char) value) + "\"";
		case PackageCharacter:
			return "\"" + value.toString() + "\"";
		default:
			return valueToString(value, classInfo);
		}
	}

	public static String valueToString(final Object value, final ClassInfo classInfo) throws IllegalArgumentException, IllegalAccessException {
		if (null == value)
			return "null";
		switch (classInfo.getType()) {
		case StringType:
			return (String) value;
		case PrimitiveInt:
			return Integer.toString((int) value);
		case PrimitiveBoolean:
			return Boolean.toString((boolean) value);
		case PrimitiveChar:
			return Character.toString((char) value);
		case PrimitiveShort:
			return Short.toString((short) value);
		case PrimitiveLong:
			return Long.toString((long) value);
		case PrimitiveFloat:
			return Float.toString((float) value);
		case PrimitiveByte:
			return Byte.toString((byte) value);
		case PrimitiveDouble:
			return Double.toString((double) value);
		case PackageInteger:
			return value.toString();
		case PackageBoolean:
			return value.toString();
		case PackageCharacter:
			return value.toString();
		case PackageShort:
			return value.toString();
		case PackageLong:
			return value.toString();
		case PackageFloat:
			return value.toString();
		case PackageByte:
			return value.toString();
		case PackageDouble:
			return value.toString();
		case Array:
			return Arrays.toString((Object[]) value);
		default:
			return value.toString();
		}

	}

	public static String nullToJson(String name) {
		return "\"" + name + "\":null";
	}

	public static String valueToJson(String name, final Object value, final ClassInfo field) throws IllegalArgumentException, IllegalAccessException {
		name = (null == name ? "" : ("\"" + name + "\":"));
		if (null == value) {
			return "\"" + name + "\":null";
		}
		switch (field.getType()) {
		case StringType:
			return name + "\"" + value.toString().replaceAll("\"", "\\\"") + "\"";
		case PrimitiveInt:
			return name + Integer.toString((int) value);
		case PrimitiveBoolean:
			return name + Boolean.toString((boolean) value);
		case PrimitiveChar:
			return name + "\"" + Character.toString((char) value).replaceAll("\"", "\\\"") + "\"";
		case PrimitiveShort:
			return name + Short.toString((short) value);
		case PrimitiveLong:
			return name + Long.toString((long) value);
		case PrimitiveFloat:
			return name + Float.toString((float) value);
		case PrimitiveByte:
			return name + Byte.toString((byte) value);
		case PrimitiveDouble:
			return name + Double.toString((double) value);
		case PackageInteger:
			return name + value.toString();
		case PackageBoolean:
			return name + value.toString();
		case PackageCharacter:
			return name + "\"" + value.toString().replaceAll("\"", "\\\"") + "\"";
		case PackageShort:
			return name + value.toString();
		case PackageLong:
			return name + value.toString();
		case PackageFloat:
			return name + value.toString();
		case PackageByte:
			return name + value.toString();
		case PackageDouble:
			return name + value.toString();
		case Array:
			if (value instanceof String[] || value instanceof Character[] || value instanceof char[]) {
				Object[] a = (Object[]) value;
				int iMax = a.length - 1;
				if (iMax == -1)
					return name + "[]";

				StringBuilder b = new StringBuilder();
				b.append(name + "[");
				for (int i = 0;; i++) {
					b.append(String.valueOf(a[i]));
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

	public static String valueToJson(final Object value, final ClassInfo field) throws IllegalArgumentException, IllegalAccessException {
		return valueToJson(field.getName(), value, field);
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

	}
}
