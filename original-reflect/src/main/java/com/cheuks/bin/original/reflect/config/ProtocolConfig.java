package com.cheuks.bin.original.reflect.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ProtocolConfig extends AbstractConfig {

	private static final long serialVersionUID = 1L;

	private String name;// 服务协议()
	private int port;// 端口
	private String scanPackage;// 扫描目录
	private int netWorkThreads;// 网络你监听线程数
	private int handleThreads;// 处理线程数
	private String charset;// 编码
	private int heartbeat;// 心跳间隔
	private int payload;// 最大请求数据长度
	private String refSerialize;// 序列化接口
	private String refRmiBeanFactory;// 远程调用接口

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		// this.name = element.getAttribute("name");
		// this.port = Integer.valueOf(element.getAttribute("port"));
		// this.scanPackage = element.getAttribute("scanPackage");
		// this.netWorkThreads = Integer.valueOf(element.getAttribute("netWorkThreads"));
		// this.handleThreads = Integer.valueOf(element.getAttribute("handleThreads"));
		// this.charset = element.getAttribute("charset");
		// this.heartbeat = Integer.valueOf(element.getAttribute("heartbeat"));
		// this.payload = Integer.valueOf(element.getAttribute("payload"));
		// this.refSerialize = element.getAttribute("refSerialize");
		// this.refRmiBeanFactory = element.getAttribute("refRmiBeanFactory");
		// parserContext.getRegistry().registerBeanDefinition("rmiProtocolConfig", BeanDefinitionBuilder.);
		BeanDefinition beanDefinition = new RootBeanDefinition(ProtocolConfig.class);
		beanDefinition.getPropertyValues().add("name", element.getAttribute("name"));
		beanDefinition.getPropertyValues().add("port", Integer.valueOf(element.getAttribute("port")));
		beanDefinition.getPropertyValues().add("scanPackage", element.getAttribute("scanPackage"));
		beanDefinition.getPropertyValues().add("netWorkThreads", Integer.valueOf(element.getAttribute("netWorkThreads")));
		beanDefinition.getPropertyValues().add("handleThreads", Integer.valueOf(element.getAttribute("handleThreads")));
		beanDefinition.getPropertyValues().add("charset", element.getAttribute("charset"));
		beanDefinition.getPropertyValues().add("heartbeat", Integer.valueOf(element.getAttribute("heartbeat")));
		beanDefinition.getPropertyValues().add("payload", Integer.valueOf(element.getAttribute("payload")));
		beanDefinition.getPropertyValues().add("refSerialize", element.getAttribute("refSerialize"));
		beanDefinition.getPropertyValues().add("refRmiBeanFactory", element.getAttribute("refRmiBeanFactory"));
		parserContext.getRegistry().registerBeanDefinition("rmiProtocolConfig", beanDefinition);
		return this;
	}

	public String getName() {
		return name;
	}

	public ProtocolConfig setName(String name) {
		this.name = name;
		return this;
	}

	public int getPort() {
		return port;
	}

	public ProtocolConfig setPort(int port) {
		this.port = port;
		return this;
	}

	public String getScanPackage() {
		return scanPackage;
	}

	public ProtocolConfig setScanPackage(String scanPackage) {
		this.scanPackage = scanPackage;
		return this;
	}

	public int getNetWorkThreads() {
		return netWorkThreads;
	}

	public ProtocolConfig setNetWorkThreads(int netWorkThreads) {
		this.netWorkThreads = netWorkThreads;
		return this;
	}

	public int getHandleThreads() {
		return handleThreads;
	}

	public ProtocolConfig setHandleThreads(int handleThreads) {
		this.handleThreads = handleThreads;
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public ProtocolConfig setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public int getHeartbeat() {
		return heartbeat;
	}

	public ProtocolConfig setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
		return this;
	}

	public int getPayload() {
		return payload;
	}

	public ProtocolConfig setPayload(int payload) {
		this.payload = payload;
		return this;
	}

	public String getRefSerialize() {
		return refSerialize;
	}

	public ProtocolConfig setRefSerialize(String refSerialize) {
		this.refSerialize = refSerialize;
		return this;
	}

	public String getRefRmiBeanFactory() {
		return refRmiBeanFactory;
	}

	public ProtocolConfig setRefRmiBeanFactory(String refRmiBeanFactory) {
		this.refRmiBeanFactory = refRmiBeanFactory;
		return this;
	}

}
