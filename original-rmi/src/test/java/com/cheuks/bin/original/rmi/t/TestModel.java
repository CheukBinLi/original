package com.cheuks.bin.original.rmi.t;

import java.io.Serializable;
import java.util.List;

public class TestModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String desc;
	private String remark;
	private List<String> ids;

	public TestModel(int id, String name, String desc, String remark, List<String> ids) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.remark = remark;
		this.ids = ids;
	}
	public TestModel() {
		super();
	}
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
	public String getDesc() {
		return desc;
	}
	public TestModel setDesc(String desc) {
		this.desc = desc;
		return this;
	}
	public String getRemark() {
		return remark;
	}
	public TestModel setRemark(String remark) {
		this.remark = remark;
		return this;
	}
	public List<String> getIds() {
		return ids;
	}
	public TestModel setIds(List<String> ids) {
		this.ids = ids;
		return this;
	}

}
