package com.cheuks.bin.original.rmi.config.model;

import java.io.Serializable;

public class RegistryModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String serverAddress;
	private int maxRetries;

	public String getServerAddress() {
		return serverAddress;
	}

	public RegistryModel setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
		return this;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public RegistryModel setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
		return this;
	}

}
