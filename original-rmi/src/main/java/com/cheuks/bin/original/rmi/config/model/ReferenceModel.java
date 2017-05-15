package com.cheuks.bin.original.rmi.config.model;

import java.io.Serializable;

public class ReferenceModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;// 名字
	private String interfaceName;// 接口
	private String version;

	public String getId() {
		return id;
	}

	public ReferenceModel setId(String id) {
		this.id = id;
		return this;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public ReferenceModel setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public ReferenceModel setVersion(String version) {
		this.version = version;
		return this;
	}

}
