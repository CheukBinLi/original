package com.cheuks.bin.original.rmi.unit;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cheuks.bin.original.common.util.Encryption;
import com.cheuks.bin.original.common.util.ReflectionUtil;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class Reflection extends ReflectionUtil {

	protected Reflection() {
	}

	protected static Reflection newInstance;

	public static final Reflection newInstance() {
		if (null == newInstance) {
			synchronized (Reflection.class) {
				if (null == newInstance) {
					newInstance = new Reflection();
				}
			}
		}
		return newInstance;
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

	public String genericRmiMethodMd5Code(String registrationServiceName, String className, String version, final CtMethod currentMethod) throws NotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append(registrationServiceName).append(":");
		sb.append(className).append(":");
		sb.append(version).append(":");
		sb.append(genericMethod(currentMethod));
		return Encryption.newInstance().MD5(sb.toString());
	}

	public String genericRmiMethodMd5Code(String registrationServiceName, String className, String version, final Method currentMethod) throws NotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append(registrationServiceName).append(":");
		sb.append(className).append(":");
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
