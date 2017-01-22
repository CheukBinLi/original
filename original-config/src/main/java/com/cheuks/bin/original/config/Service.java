package com.cheuks.bin.original.config;

public class Service {

	private String interface_a;
	private String alias;
	private String class_a;
	private String ref;
	private String version;
	private String multiInstance;

	public String getInterface_a() {
		return interface_a;
	}

	public Service setInterface(String interface_a) {
		this.interface_a = interface_a;
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public Service setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	public String getClass_a() {
		return class_a;
	}

	public Service setClass(String class_a) {
		this.class_a = class_a;
		return this;
	}

	public String getRef() {
		return ref;
	}

	public Service setRef(String ref) {
		this.ref = ref;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public Service setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getMultiInstance() {
		return multiInstance;
	}

	public Service setMultiInstance(String multiInstance) {
		this.multiInstance = multiInstance;
		return this;
	}

}
