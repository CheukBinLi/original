package com.cheuks.bin.original.rmi.config.model;

import java.io.Serializable;

public class RegistryModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private int maxRetries;
	private String address;

	public int getMaxRetries() {
		return maxRetries;
	}

	public RegistryModel setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public RegistryModel setAddress(String address) {
		this.address = address;
		return this;
	}

}
