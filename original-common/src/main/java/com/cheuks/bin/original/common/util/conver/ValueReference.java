package com.cheuks.bin.original.common.util.conver;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import com.cheuks.bin.original.common.util.reflection.ClassInfo;
import com.cheuks.bin.original.common.util.reflection.ReflectionUtil;

public class ValueReference {
	private ClassInfo classInfo;//当前对象信息
	private Map<String, String> names;//引用的字段名
	private String triggerField;//触发字段名
	private ValueReference next;//触发字段的类信息

	public static ValueReference build(Class<?> clazz) {
		return new ValueReference(clazz);
	}

	public ValueReference addNext(String triggerField, Class<?> clazz) {
		this.triggerField = triggerField;
		this.setNext(new ValueReference(clazz));
		return this.next;
	}

	public ValueReference addNames(String fieldName, String newFieldName) {
		if (null == names)
			names = new ConcurrentSkipListMap<String, String>();
		names.put(fieldName, newFieldName);
		return this;
	}

	public ValueReference(Class<?> clazz) {
		this.classInfo = ClassInfo.getClassInfo(clazz);
		if (null == this.classInfo || !(this.classInfo.isBasicOrArrays() && this.classInfo.isMapOrSetOrCollection())) {
			try {
				this.classInfo.setFields(ReflectionUtil.instance().scanClassFieldInfo4Map(clazz, true, true, true));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
	}

	public ValueReference addReference(String fieldName, String newName) {
		names.put(fieldName, newName);
		return this;
	}

	public ClassInfo getClassInfo() {
		return classInfo;
	}

	public Map<String, String> getNames() {
		return names;
	}

	public ValueReference setNames(Map<String, String> names) {
		this.names = names;
		return this;
	}

	public ValueReference getNext() {
		return next;
	}

	public ValueReference setNext(ValueReference next) {
		this.next = next;
		return this;
	}

	public String getTriggerField() {
		return triggerField;
	}

	public ValueReference setTriggerField(String triggerField) {
		this.triggerField = triggerField;
		return this;
	}

}
