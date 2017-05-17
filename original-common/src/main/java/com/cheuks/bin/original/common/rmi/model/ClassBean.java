package com.cheuks.bin.original.common.rmi.model;

import java.io.Serializable;

public class ClassBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/***
	 * 全局ID
	 */
	private String id;
	/***
	 * 版本信息
	 */
	private String version;
	/***
	 * 说明
	 */
	private String describe;
	/***
	 * 单例/多例
	 */
	private boolean multiInstance;
	/***
	 * 接口类
	 */
	private Class<?> interfaceClassFile;
	/***
	 * 接口代理实现类
	 */
	private Class<?> proxyClassFile;
	/***
	 * 实例
	 */
	private Object instance;

	public String getId() {
		return id;
	}

	public ClassBean setId(String id) {
		this.id = id;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public ClassBean setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getDescribe() {
		return describe;
	}

	public ClassBean setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	public boolean isMultiInstance() {
		return multiInstance;
	}

	public ClassBean setMultiInstance(boolean multiInstance) {
		this.multiInstance = multiInstance;
		return this;
	}

	public Object getInstance() {
		return instance;
	}

	public ClassBean setInstance(Object instance) {
		this.instance = instance;
		return this;
	}

	public Class<?> getInterfaceClassFile() {
		return interfaceClassFile;
	}

	public ClassBean setInterfaceClassFile(Class<?> interfaceClassFile) {
		this.interfaceClassFile = interfaceClassFile;
		return this;
	}

	public Class<?> getProxyClassFile() {
		return proxyClassFile;
	}

	public ClassBean setProxyClassFile(Class<?> proxyClassFile) {
		this.proxyClassFile = proxyClassFile;
		return this;
	}

}
