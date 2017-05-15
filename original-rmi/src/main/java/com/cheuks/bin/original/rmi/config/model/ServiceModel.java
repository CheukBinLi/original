package com.cheuks.bin.original.rmi.config.model;

import java.io.Serializable;

public class ServiceModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;// 实例注册名
	private String interfaceName;// 接口
	private String ref;// 实现引用
	private String refClass;// 用引类class
	private String version;
	private boolean multiInstance;
	private String describe;//接口描述

	public String getId() {
		return id;
	}

	public ServiceModel setId(String id) {
		this.id = id;
		return this;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public ServiceModel setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		return this;
	}

	public String getRef() {
		return ref;
	}

	public ServiceModel setRef(String ref) {
		this.ref = ref;
		return this;
	}

	public String getRefClass() {
		return refClass;
	}

	public ServiceModel setRefClass(String refClass) {
		this.refClass = refClass;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public ServiceModel setVersion(String version) {
		this.version = version;
		return this;
	}

	public boolean isMultiInstance() {
		return multiInstance;
	}

	public ServiceModel setMultiInstance(boolean multiInstance) {
		this.multiInstance = multiInstance;
		return this;
	}

	public String getDescribe() {
		return describe;
	}

	public ServiceModel setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

}
