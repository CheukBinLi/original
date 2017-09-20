package com.cheuks.bin.original.rmi.config.model;

import java.io.Serializable;

public class ScanModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String packagePath;
	private String serviceName;
	private String version;
	private Boolean multiInstance;

	public String getPackagePath() {
		return packagePath;
	}

	public String getServiceName() {
		return serviceName;
	}

	public ScanModel setServiceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public ScanModel setVersion(String version) {
		this.version = version;
		return this;
	}

	public Boolean getMultiInstance() {
		return multiInstance;
	}

	public ScanModel setMultiInstance(Boolean multiInstance) {
		this.multiInstance = multiInstance;
		return this;
	}

	public ScanModel setPackagePath(String packagePath) {
		this.packagePath = packagePath;
		return this;
	}

}
