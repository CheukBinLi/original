package com.cheuks.bin.original.common.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

public class FieldInfo {

	private Field field;
	private boolean isAlias;
	private Alias alias;
	private boolean isTransient;

	public String getAliasOrFieldName(boolean isAlias) {
		return (null == alias || !isAlias) ? field.getName() : alias.value();
	}

	public String getAliasOrFieldName() {
		return null == alias ? field.getName() : alias.value();
	}

	public FieldInfo() {
		super();
	}

	public FieldInfo(Field field) {
		super();
		this.field = field;
		isTransient = Modifier.isTransient(field.getModifiers());
		this.alias = field.getDeclaredAnnotation(Alias.class);
	}

	public FieldInfo(Field field, boolean isAlias) {
		this(field);
		this.isAlias = isAlias;
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

	public boolean isAlias() {
		return isAlias;
	}

	public void setAlias(boolean isAlias) {
		this.isAlias = isAlias;
	}

	public boolean isTransient() {
		return isTransient;
	}

	public FieldInfo setTransient(boolean isTransient) {
		this.isTransient = isTransient;
		return this;
	}

}
