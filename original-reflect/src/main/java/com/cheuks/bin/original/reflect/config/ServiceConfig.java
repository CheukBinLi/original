package com.cheuks.bin.original.reflect.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ServiceConfig extends AbstractConfig {

	private static final long serialVersionUID = 1L;

	private String id;// 实例注册名
	private String interfaceName;// 接口
	private String ref;// 实现引用
	private String refClass;// 用引类class
	private String version;
	private boolean multiInstance;
	protected final static transient Set<ServiceConfig> beans = new HashSet<ServiceConfig>();

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		// ServiceConfig serviceConfig = new ServiceConfig();
		// serviceConfig.setId(element.getAttribute("id")).setInterfaceName(element.getAttribute("interface"));
		// serviceConfig.setRef(element.getAttribute("ref")).setRefClass(element.getAttribute("refClass"));
		// serviceConfig.setVersion(element.getAttribute("version")).setMultiInstance(Boolean.valueOf(element.getAttribute("multiInstance")));
		this.id = element.getAttribute("id");
		this.interfaceName = element.getAttribute("interface");
		this.ref = element.getAttribute("ref");
		this.refClass = element.getAttribute("refClass");
		this.version = element.getAttribute("version");
		this.multiInstance = Boolean.valueOf(element.getAttribute("multiInstance"));
		beans.add(this);
		return this;
		// beans.add(serviceConfig);
		// return serviceConfig;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ServiceConfig) {
			return this.id.equals(((ServiceConfig) obj).id);
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public ServiceConfig setId(String id) {
		this.id = id;
		return this;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public ServiceConfig setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		return this;
	}

	public String getRef() {
		return ref;
	}

	public ServiceConfig setRef(String ref) {
		this.ref = ref;
		return this;
	}

	public String getRefClass() {
		return refClass;
	}

	public ServiceConfig setRefClass(String refClass) {
		this.refClass = refClass;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public boolean isMultiInstance() {
		return multiInstance;
	}

	public ServiceConfig setMultiInstance(boolean multiInstance) {
		this.multiInstance = multiInstance;
		return this;
	}

	public ServiceConfig setVersion(String version) {
		this.version = version;
		return this;
	}

	public static Set<ServiceConfig> getBeans() {
		return beans;
	}

}
