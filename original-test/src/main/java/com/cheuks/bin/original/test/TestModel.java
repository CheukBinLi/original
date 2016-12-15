package com.cheuks.bin.original.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private boolean ok;
	private float pay;
	private List<String> other;
	private Map<String, String> param;

	public int getId() {
		return id;
	}

	public TestModel setId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public TestModel setName(String name) {
		this.name = name;
		return this;
	}

	public boolean isOk() {
		return ok;
	}

	public TestModel setOk(boolean ok) {
		this.ok = ok;
		return this;
	}

	public float getPay() {
		return pay;
	}

	public TestModel setPay(float pay) {
		this.pay = pay;
		return this;
	}

	public List<String> getOther() {
		return other;
	}

	public TestModel setOther(List<String> other) {
		this.other = other;
		return this;
	}

	public Map<String, String> getParam() {
		return param;
	}

	public TestModel setParam(Map<String, String> param) {
		this.param = param;
		return this;
	}

	public TestModel() {
		super();
		// TODO Auto-generated constructor stub
	}

}
