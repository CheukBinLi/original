package com.cheuks.bin.original.common.util.reflection;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ClassInfo implements Cloneable {

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
		BASIC_TYPE_CLASS_INFO.put(java.util.Date.class.getName(), new ClassInfo(java.util.Date.class));
		BASIC_TYPE_CLASS_INFO.put(java.sql.Timestamp.class.getName(), new ClassInfo(java.sql.Timestamp.class));
		BASIC_TYPE_CLASS_INFO.put(java.sql.Time.class.getName(), new ClassInfo(java.sql.Time.class));
	}

	private String name;
	private Type type;
	private Class<?> clazz;
	//	private List<FieldInfo> fields;
	private Map<String, FieldInfo> fields;
	private boolean isBasic;//基础类型+封装类
	private boolean isArrays;
	private boolean isMap;
	private boolean isSet;
	private boolean isDate;
	private boolean isAbstract = false;
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
		this.isDate = isArrays ? false : Type.isDate(clazz);
		this.isSet = isArrays ? false : isDate ? false : Type.isSetByClass(clazz);
		this.isMap = isArrays ? false : isDate ? false : isSet ? false : Type.isMapByClass(clazz);
		this.isCollection = isDate ? false : isMap ? false : Type.isCollectionByClass(clazz);
		this.type = isArrays ? Type.Array : isMap ? Type.Map : isCollection ? Type.Collection : Type.getTypeByClass(clazz);
		this.isBasic = isDate ? false : isMap ? false : isCollection ? false : isArrays ? false : (clazz.isPrimitive() | Type.isWrapper(clazz));
		this.isAbstract = isBasic ? false : isArrays ? false : isSet ? false : isMap ? false : isDate ? false : this.clazz.isInterface() ? true : Modifier.isAbstract(this.clazz.getModifiers());
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

	public boolean isMapOrSetOrCollection() {
		return (isMap || isCollection || isSet);
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

	public boolean isSet() {
		return isSet;
	}

	public Map<String, FieldInfo> getFields() {
		return fields;
	}

	public void setFields(Map<String, FieldInfo> fields) {
		this.fields = fields;
	}

	public final static void addClassInfo(ClassInfo classInfo) {
		if (!classInfo.isMapOrSetOrCollection() && !classInfo.isBasicOrArrays())
			CLASS_INFOS.put(classInfo.getClazz().getName(), classInfo);
	}

	public boolean isDate() {
		return isDate;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public final synchronized static ClassInfo getClassInfo(final Class<?> clazz) {
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
