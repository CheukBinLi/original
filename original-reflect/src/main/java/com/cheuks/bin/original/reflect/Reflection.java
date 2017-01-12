package com.cheuks.bin.original.reflect;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cheuks.bin.original.common.util.Encryption;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

@SuppressWarnings("unchecked")
public class Reflection {

	protected Reflection() {
	}

	public static final Reflection newInstance = new Reflection();

	private final List<?> CollectionType = Arrays.asList(Collection.class, List.class, Set.class, Integer.class, int.class, Long.class, long.class, Float.class, float.class, Byte.class, byte.class, Character.class, char.class, String.class, Boolean.class, boolean.class, Double.class, double.class, Short.class, short.class);

	public static final Reflection newInstance() {
		return newInstance;
	}

	/***
	 * 末不完成，废弃
	 * 
	 * @param collection
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public <T> Class<T> getGenericName(Collection<T> collection) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method collectionToArray = Collection.class.getDeclaredMethod("toArray");
		Object current = collectionToArray.invoke(collection, null);
		System.err.println(current.getClass().getSimpleName());
		ParameterizedType type;
		try {
			Class c = collection.getClass().getComponentType();
			type = (ParameterizedType) collection.getClass().getGenericSuperclass();
			Class cx = collection.getClass();
			Object x = collection.toArray().getClass();
			Object o = cx.toGenericString();
			System.out.println(x);
			System.out.println(cx.getComponentType());
			System.out.println(type.getActualTypeArguments()[0]);
		} catch (Exception e) {
			return null;
		}

		// Method m=collection.iterator().getClass().getGenericSuperclass();

		return null;
	}

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException {
		newInstance.getGenericName(new ArrayList<String>());
		newInstance.getGenericName(Arrays.asList(1));
		System.err.println("INFO_EXHIBITION_TYPE".length());

		Reflection r = newInstance;
		Class<?> test = test.class;
		Method[] a = test.getDeclaredMethods();
		ClassPool pool = ClassPool.getDefault();
		CtClass ctTest = pool.get(test.getName());
		CtMethod[] ctA = ctTest.getDeclaredMethods();
		for (CtMethod m : ctA) {
			if (m.getName().equals("a")) {
				System.out.println(r.genericMethod(m));
			}
		}
		for (Method m : a) {
			if (m.getName().equals("a")) {
				System.out.println(r.genericMethod(m));
			}
		}
	}

	public List<FieldList> isCollection(Field field, boolean isAccessible) throws NoSuchFieldException, SecurityException {
		List<Class<?>> interfaces = Arrays.asList(field.getType().getInterfaces());
		List<FieldList> fieldList = null;
		if (!interfaces.containsAll(CollectionType)) {
			ParameterizedType type;
			try {
				type = (ParameterizedType) field.getGenericType();
			} catch (Exception e) {
				return null;
			}
			fieldList = new ArrayList<FieldList>();
			for (Type t : type.getActualTypeArguments())
				fieldList.addAll(getSettingFieldListList((Class<?>) t, isAccessible));
		}
		return fieldList;
	}

	public List<FieldList> getSettingFieldListList(Class<?> clazz, boolean isAccessible) throws NoSuchFieldException, SecurityException {
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();
		Set<String> settingMethodName = new HashSet<String>();
		List<FieldList> result = new ArrayList<FieldList>();
		List<FieldList> subFieldList;
		for (Method m : methods) {
			if (m.getName().startsWith("set")) {
				settingMethodName.add(m.getName().substring(3).toLowerCase());
			}
		}
		for (Field f : fields) {
			if (settingMethodName.contains(f.getName().toLowerCase())) {
				if (isAccessible)
					f.setAccessible(true);
				subFieldList = isCollection(f, isAccessible);
				result.add(subFieldList == null ? new FieldList(f, getPackageType(f.getType())) : new FieldList(f, subFieldList));
			}
		}
		return result;
	}

	public String getPackageType(Class<?> type) {
		String result = type.getSimpleName();
		if ("int".equals(result)) {
			return "Integer";
		} else if ("double".equals(result)) {
			return "Double";
		} else if ("byte".equals(result)) {
			return "Byte";
		} else if ("float".equals(result)) {
			return "Float";
		} else if ("long".equals(result)) {
			return "Long";
		} else if ("short".equals(result)) {
			return "Long";
		} else if ("boolean".equals(result)) {
			return "Boolean";
		}
		return result;
	}

	public String getElasticsearchMappingType(Class<?> type) {
		String result = getPackageType(type).toLowerCase();
		if ("string".equals(result))
			return "text";
		return result;
	}

	/***
	 * 
	 * @param method
	 * @return void a(string,int)
	 */
	public String genericMethod(Method method) {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getReturnType().getSimpleName()).append(" ");
		sb.append(method.getName());
		sb.append("(");
		Class<?>[] paramsType = method.getParameterTypes();
		for (int i = 0, len = paramsType.length; i < len; i++) {
			sb.append(paramsType[i].getSimpleName()).append(",");
		}
		if (paramsType.length > 0)
			sb.setLength(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}

	/***
	 * 
	 * @param method
	 * @return void a(string,int)
	 * @throws NotFoundException
	 */
	public String genericMethod(CtMethod method) throws NotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getReturnType().getSimpleName()).append(" ");
		sb.append(method.getName());
		sb.append("(");
		CtClass[] paramsType = method.getParameterTypes();
		for (int i = 0, len = paramsType.length; i < len; i++) {
			sb.append(paramsType[i].getSimpleName()).append(",");
		}
		if (paramsType.length > 0)
			sb.setLength(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}

	public String genericRmiMethodMd5Code(String registrationServiceName, String version, final CtMethod currentMethod) throws NotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append(registrationServiceName).append(":");
		sb.append(version).append(":");
		sb.append(genericMethod(currentMethod));
		return Encryption.newInstance().MD5(sb.toString());
	}

	public String genericRmiMethodMd5Code(String registrationServiceName, String version, final Method currentMethod) throws NotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append(registrationServiceName).append(":");
		sb.append(version).append(":");
		sb.append(genericMethod(currentMethod));
		return Encryption.newInstance().MD5(sb.toString());
	}

	/***
	 * 
	 * @author ben
	 * @field 当前字段
	 * @hasSub 是否有子类(字段是否是集合类)
	 * @subFieldList 字类字段集合
	 */
	public static class FieldList implements Serializable {

		private static final long serialVersionUID = 1L;
		private Field field;
		private String packingClassName;
		private List<FieldList> subFieldList;

		public Field getField() {
			return field;
		}

		public FieldList setField(Field field) {
			this.field = field;
			return this;
		}

		public boolean isBasicType() {
			return subFieldList == null;
		}

		public String getPackingClassName() {
			return packingClassName;
		}

		public FieldList setPackingClassName(String packingClassName) {
			this.packingClassName = packingClassName;
			return this;
		}

		public List<FieldList> getSubFieldList() {
			return subFieldList;
		}

		public FieldList setSubFieldList(List<FieldList> subFieldList) {
			this.subFieldList = subFieldList;
			return this;
		}

		public FieldList(Field field) {
			super();
			this.field = field;
		}

		public FieldList() {
			super();
		}

		public FieldList(Field field, List<FieldList> subFieldList) {
			super();
			this.field = field;
			this.subFieldList = subFieldList;
		}

		public FieldList(Field field, String packingClassName) {
			super();
			this.field = field;
			this.packingClassName = packingClassName;
		}

	}

	public static abstract class MethodIteration {
		private Class<?> c;

		public abstract Method doIterator(Method m);

		public List<Method> iterator() {
			List<Method> result = new ArrayList<Method>();
			Method[] methods = c.getDeclaredMethods();
			Method tempMethod;
			for (Method m : methods) {
				if (null != (tempMethod = doIterator(m))) {
					result.add(tempMethod);
				}
			}
			return result;
		}

		public MethodIteration(Class<?> c) {
			super();
			this.c = c;
		}
	}
}
