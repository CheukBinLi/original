package com.cheuks.bin.original.common.util.reflection;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ClassInfo {

	private String name;
	private Type type;
	private Class<?> clazz;
	private List<Field> fields;
	private Map<String, ClassInfo> classInfos;
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

	public Map<String, ClassInfo> getClassInfos() {
		return classInfos;
	}

	public void addClassInfo(ClassInfo classInfo) {
		if (null == classInfos) {
			classInfos = new ConcurrentSkipListMap<String, ClassInfo>();
		}
		if (!classInfo.isMapOrCollection() && !classInfo.isBasicOrArrays())
			classInfos.put(classInfo.getClazz().getName(), classInfo);
	}

	public ClassInfo getClassInfo(Class<?> clazz) {
		if (null == clazz) {
			return null;
		}
		ClassInfo result = null;
		if (null != classInfos) {
			result = classInfos.get(clazz.getName());
		}
		if (null == result) {
			addClassInfo(result = new ClassInfo(clazz));
		}
		return result;
	}

}
