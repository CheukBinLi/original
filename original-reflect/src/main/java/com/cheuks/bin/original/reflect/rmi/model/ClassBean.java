package com.cheuks.bin.original.reflect.rmi.model;

import java.io.Serializable;

public class ClassBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Class<?> originalClassFile;
	private Class<?> proxyClassFile;
	private String registrationServiceName;
	private String version;
	private Object instance;
	private boolean multiInstance;

	public Class<?> getOriginalClassFile() {
		return originalClassFile;
	}

	public ClassBean setOriginalClassFile(Class<?> originalClassFile) {
		this.originalClassFile = originalClassFile;
		return this;
	}

	public Class<?> getProxyClassFile() {
		return proxyClassFile;
	}

	public ClassBean setProxyClassFile(Class<?> proxyClassFile) {
		this.proxyClassFile = proxyClassFile;
		return this;
	}

	public String getRegistrationServiceName() {
		return registrationServiceName;
	}

	public ClassBean setRegistrationServiceName(String registrationServiceName) {
		this.registrationServiceName = registrationServiceName;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public ClassBean setVersion(String version) {
		this.version = version;
		return this;
	}

	public Object getInstance() throws InstantiationException, IllegalAccessException {
		if (this.isMultiInstance())
			return this.proxyClassFile.newInstance();
		else if (null == this.instance) {
			synchronized (this) {
				if (null == this.instance)
					setInstance(this.proxyClassFile.newInstance());
			}
		}
		return instance;
	}

	public ClassBean setInstance(Object instance) {
		this.instance = instance;
		return this;
	}

	public boolean isMultiInstance() {
		return multiInstance;
	}

	public ClassBean setMultiInstance(boolean multiInstance) {
		this.multiInstance = multiInstance;
		return this;
	}

	public ClassBean(Class<?> originalClassFile, String registrationServiceName, String version, boolean multiInstance) {
		super();
		this.originalClassFile = originalClassFile;
		this.registrationServiceName = registrationServiceName;
		this.version = version;
		this.multiInstance = multiInstance;
	}

	// public ClassBean() {
	// super();
	// }

}
