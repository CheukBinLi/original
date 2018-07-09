package com.cheuks.bin.original.common.util.conver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Filter {
	private Class<?> clazz;
	private List<String> excepts;

	public Filter() {
		super();
	}

	public Filter(Class<?> clazz, String... excepts) {
		super();
		this.clazz = clazz;
		this.excepts = new LinkedList<String>(Arrays.asList(excepts));
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public Filter setClazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

	public List<String> getExcepts() {
		return excepts;
	}

	public Filter setExcepts(List<String> excepts) {
		this.excepts = excepts;
		return this;
	}

}