package com.cheuks.bin.original.common.util.reflection;

import java.lang.reflect.Field;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

public class FieldInfo {

	private Field field;
	private Alias alias;

	public String getAliasOrFieldName() {
		return null == alias ? field.getName() : alias.value();
	}

	public FieldInfo() {
		super();
	}

	public FieldInfo(Field field) {
		super();
		this.field = field;
		alias = field.getDeclaredAnnotation(Alias.class);
	}

	public Field getField() {
		return field;
	}

	public FieldInfo setField(Field field) {
		this.field = field;
		return this;
	}

	public Alias getAlias() {
		return alias;
	}

	public FieldInfo setAlias(Alias alias) {
		this.alias = alias;
		return this;
	}

}
