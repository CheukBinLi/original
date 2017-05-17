package com.cheuks.bin.original.common.rmi.model;

import java.io.Serializable;
import java.lang.reflect.Method;

public class MethodBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ClassBean classBean;
	private Method currentMethod;
	private String id;

	public MethodBean(ClassBean classBean, Method currentMethod, String id) {
		super();
		this.classBean = classBean;
		this.currentMethod = currentMethod;
		this.id = id;
	}

	public MethodBean(ClassBean classBean, Method currentMethod) {
		super();
		this.classBean = classBean;
		this.currentMethod = currentMethod;
	}

	public MethodBean(ClassBean classBean) {
		super();
		this.classBean = classBean;
	}

	public Method getCurrentMethod() {
		return currentMethod;
	}

	public MethodBean setCurrentMethod(Method currentMethod) {
		this.currentMethod = currentMethod;
		return this;
	}

	public String getId() {
		return id;
	}

	public MethodBean setId(String id) {
		this.id = id;
		return this;
	}

	public ClassBean getClassBean() {
		return classBean;
	}

}
