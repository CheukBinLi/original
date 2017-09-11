package com.cheuks.bin.original.rmi.config.model;

import java.io.Serializable;

public class ScanModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String packagePath;

	public String getPackagePath() {
		return packagePath;
	}

	public ScanModel setPackagePath(String packagePath) {
		this.packagePath = packagePath;
		return this;
	}

}
