package com.cheuks.bin.original.common.util.reflection;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ClassInfo {

	protected final static Map<String, ClassInfo> CLASS_INFOS = new ConcurrentSkipListMap<String, ClassInfo>();
	protected final static ClassInfo ARRAYS_CLASS_INFO_TYPE = new ClassInfo().setType(Type.Array);
	protected final static Map<String, ClassInfo> BASIC_TYPE_CLASS_INFO = new ConcurrentSkipListMap<String, ClassInfo>();
	static {
		ARRAYS_CLASS_INFO_TYPE.isArrays = true;

		BASIC_TYPE_CLASS_INFO.put(String.class.getName(), new ClassInfo(String.class));
		BASIC_TYPE_CLASS_INFO.put(int.class.getName(), new ClassInfo(int.class));
		BASIC_TYPE_CLASS_INFO.put(boolean.class.getName(), new ClassInfo());
		BASIC_TYPE_CLASS_INFO.put(char.class.getName(), new ClassInfo(char.class));
		BASIC_TYPE_CLASS_INFO.put(short.class.getName(), new ClassInfo(short.class));
		BASIC_TYPE_CLASS_INFO.put(long.class.getName(), new ClassInfo(long.class));
		BASIC_TYPE_CLASS_INFO.put(float.class.getName(), new ClassInfo(float.class));
		BASIC_TYPE_CLASS_INFO.put(byte.class.getName(), new ClassInfo(byte.class));
		BASIC_TYPE_CLASS_INFO.put(double.class.getName(), new ClassInfo(double.class));
		BASIC_TYPE_CLASS_INFO.put(Integer.class.getName(), new ClassInfo(Integer.class));
		BASIC_TYPE_CLASS_INFO.put(Boolean.class.getName(), new ClassInfo(Boolean.class));
		BASIC_TYPE_CLASS_INFO.put(Short.class.getName(), new ClassInfo(Short.class));
		BASIC_TYPE_CLASS_INFO.put(Character.class.getName(), new ClassInfo(Character.class));
		BASIC_TYPE_CLASS_INFO.put(Short.class.getName(), new ClassInfo(Short.class));
		BASIC_TYPE_CLASS_INFO.put(Long.class.getName(), new ClassInfo(Long.class));
		BASIC_TYPE_CLASS_INFO.put(Float.class.getName(), new ClassInfo(Float.class));
		BASIC_TYPE_CLASS_INFO.put(Byte.class.getName(), new ClassInfo(Byte.class));
		BASIC_TYPE_CLASS_INFO.put(Double.class.getName(), new ClassInfo(Double.class));
	}

	private String name;
	private Type type;
	private Class<?> clazz;
	private List<Field> fields;
	private boolean isBasic;//基础类型+封装类
	private boolean isArrays;
	private boolean isMap;
	private boolean isCollection = false;

	public ClassInfo(Class<?> clazz) {
		super();
		this.clazz = clazz;
		init();
	}

	public void init() {
		if (null == this.clazz)
			return;
		this.isArrays = clazz.isArray();
		this.isMap = Type.isMapByClass(clazz);
		this.isCollection = isMap ? false : Type.isCollectionByClass(clazz);
		this.type = isArrays ? Type.Array : isMap ? Type.Map : isCollection ? Type.Collection : Type.getTypeByClass(clazz);
		this.isBasic = isMap ? false : isCollection ? false : isArrays ? false : (clazz.isPrimitive() | Type.isWrapper(clazz));
	}

	public ClassInfo() {
		super();
	}

	public String getName() {
		return name;
	}

	public ClassInfo setName(String name) {
		this.name = name;
		return this;
	}

	public Type getType() {
		return type;
	}

	public ClassInfo setType(Type type) {
		this.type = type;
		return this;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public boolean isMap() {
		return isMap;
	}

	public boolean isCollection() {
		return isCollection;
	}

	public boolean isMapOrCollection() {
		return (isMap || isCollection);
	}

	public boolean isBasicOrArrays() {
		return (isBasic || isArrays);
	}

	public boolean isBasic() {
		return isBasic;
	}

	public boolean isArrays() {
		return isArrays;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public final static void addClassInfo(ClassInfo classInfo) {
		if (!classInfo.isMapOrCollection() && !classInfo.isBasicOrArrays())
			CLASS_INFOS.put(classInfo.getClazz().getName(), classInfo);
	}

	public final static ClassInfo getClassInfo(Class<?> clazz) {
		if (null == clazz) {
			return null;
		}
		if (clazz.isArray())
			return ARRAYS_CLASS_INFO_TYPE;
		ClassInfo result = BASIC_TYPE_CLASS_INFO.get(clazz.getName());
		if (null != result)
			return result;
		result = CLASS_INFOS.get(clazz.getName());
		if (null == result) {
			addClassInfo(result = new ClassInfo(clazz));
		}
		return result;
	}

}
