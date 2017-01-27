package com.cheuks.bin.original.reflect.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Element;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.reflect.rmi.RmiBeanFactory;
import com.cheuks.bin.original.reflect.rmi.net.MessageHandle;

public class ReferenceConfig extends AbstractConfig {

	private static final long serialVersionUID = 1L;

	private String id;// 名字
	private String interfaceName;// 接口
	private String version;
	private boolean multiInstance;

	protected final static transient Set<ReferenceConfig> beans = new HashSet<ReferenceConfig>();

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		// ReferenceConfig referenceConfig = new ReferenceConfig(element.getAttribute("id"), element.getAttribute("interface"), element.getAttribute("version"), Boolean.valueOf(element.getAttribute("multiInstance")));
		this.id = element.getAttribute("id");
		this.interfaceName = element.getAttribute("interface");
		this.version = element.getAttribute("version");
		this.multiInstance = Boolean.valueOf(element.getAttribute("multiInstance"));
		beans.add(this);
		return this;
		// Set<ReferenceConfig> tempBeans=parserContext.getRegistry().getBeanDefinition("").getPropertyValues().get("beans")
		// beans.add(referenceConfig);
		// return referenceConfig;
	}

	// 注解NettyService
	private void registerService() {
		// ApplicationContext applicationContext = event.getApplicationContext();
		// String temp;
		// ProtocolConfig protocolConfig = applicationContext.getBean(ProtocolConfig.class);
		// this.port = protocolConfig.getPort();
		// this.poolSize = protocolConfig.getNetWorkThreads();
		// this.heartBeatTimeoutSecond = protocolConfig.getHeartbeat();
		// this.maxFrameLength = protocolConfig.getPayload();
		// this.handleThreads = protocolConfig.getHandleThreads();
		// this.scanPath = protocolConfig.getScanPackage();
		// this.cacheSerialize = (temp = protocolConfig.getRefSerialize()).length() > 0 ? null : (CacheSerialize) applicationContext.getBean(temp);
		// this.rmiBeanFactory = (temp = protocolConfig.getRefRmiBeanFactory()).length() > 0 ? null : (RmiBeanFactory) applicationContext.getBean(temp);
		// this.messageHandle = (temp = protocolConfig.getRefRmiBeanFactory()).length() > 0 ? null : (MessageHandle) applicationContext.getBean(temp);

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
		if (obj instanceof ReferenceConfig) {
			return this.id.equals(((ReferenceConfig) obj).id);
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public ReferenceConfig setId(String id) {
		this.id = id;
		return this;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public ReferenceConfig setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public boolean isMultiInstance() {
		return multiInstance;
	}

	public ReferenceConfig setMultiInstance(boolean multiInstance) {
		this.multiInstance = multiInstance;
		return this;
	}

	public ReferenceConfig setVersion(String version) {
		this.version = version;
		return this;
	}

	public static Set<ReferenceConfig> getBeans() {
		return beans;
	}

	public ReferenceConfig(String id, String interfaceName, String version, boolean multiInstance) {
		super();
		this.id = id;
		this.interfaceName = interfaceName;
		this.version = version;
		this.multiInstance = multiInstance;
	}

	public ReferenceConfig() {
		super();
	}

}
