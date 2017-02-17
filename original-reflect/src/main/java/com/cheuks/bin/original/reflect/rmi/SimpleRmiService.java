package com.cheuks.bin.original.reflect.rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.reflect.config.ProtocolConfig;
import com.cheuks.bin.original.reflect.config.RegistryConfig;

public class SimpleRmiService implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(NettyServer.class);
	private CacheSerialize cacheSerialize;
	private int port = 10086;
	private int poolSize = 15;
	private int heartBeatTimeoutSecond = 120;
	private RmiBeanFactory rmiBeanFactory;
	private int maxFrameLength = 5000;
	private int handleThreads = 10;
	private String scanPath;
	private String zookeeperServerList = "127.0.0.1:2181";
	private String applicationName;
	private String applicationUrl;
	private int baseSleepTimeMs = 5000;
	private int maxRetries = 20;
	private boolean isServer;
	private boolean isClient;

	private RegisterServiceServerHandler registerServiceHandler;

	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("init -- NettyServer");

			ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) event.getApplicationContext();
			DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

			ProtocolConfig protocolConfig = applicationContext.getBean(ProtocolConfig.class);
			RegistryConfig registryConfig = applicationContext.getBean(RegistryConfig.class);
			String tempString;
			cacheSerialize = (tempString = protocolConfig.getRefSerialize()).length() > 0 ? (CacheSerialize) applicationContext.getBean(tempString) : null;
			port = protocolConfig.getPort();
			poolSize = protocolConfig.getNetWorkThreads();
			heartBeatTimeoutSecond = protocolConfig.getHeartbeat();

			rmiBeanFactory = (tempString = protocolConfig.getRefSerialize()).length() > 0 ? (RmiBeanFactory) applicationContext.getBean(tempString) : null;
			registerServiceHandler = (tempString = protocolConfig.getRefHandleServiceFactory()).length() > 0 ? (RegisterServiceServerHandler) applicationContext.getBean(tempString) : null;

			maxFrameLength = protocolConfig.getPayload();
			handleThreads = protocolConfig.getHandleThreads();
			scanPath = (tempString = protocolConfig.getScanPackage()).length() > 0 ? tempString : null;
			applicationName = (tempString = protocolConfig.getName()).length() > 0 ? tempString : "default_application_" + System.currentTimeMillis();
			applicationUrl = (tempString = protocolConfig.getHost()).length() > 0 ? tempString : null;
			zookeeperServerList = registryConfig.getAddress();
			baseSleepTimeMs = registryConfig.getBaseSleepTimeMs();
			maxRetries = registryConfig.getMaxRetries();
			tempString = protocolConfig.getServiceType();
			isServer = ("server".equals(tempString) || "both".equals(tempString)) ? true : false;
			isClient = ("client".equals(tempString) || "both".equals(tempString)) ? true : false;

			BeanDefinition nettyRmiServerBean = new RootBeanDefinition(NettyServer.class);
			nettyRmiServerBean.getPropertyValues().add("port", port);
			nettyRmiServerBean.getPropertyValues().add("poolSize", poolSize);
			nettyRmiServerBean.getPropertyValues().add("heartBeatTimeoutSecond", heartBeatTimeoutSecond);
			nettyRmiServerBean.getPropertyValues().add("maxFrameLength", maxFrameLength);
			nettyRmiServerBean.getPropertyValues().add("handleThreads", handleThreads);
			nettyRmiServerBean.getPropertyValues().add("scanPath", scanPath);
			nettyRmiServerBean.getPropertyValues().add("zookeeperServerList", zookeeperServerList);
			nettyRmiServerBean.getPropertyValues().add("applicationName", applicationName);
			// nettyRmiServerBean.getPropertyValues().add("applicationUrl", applicationUrl);
			nettyRmiServerBean.getPropertyValues().add("baseSleepTimeMs", baseSleepTimeMs);
			nettyRmiServerBean.getPropertyValues().add("maxRetries", maxRetries);
			nettyRmiServerBean.getPropertyValues().add("isServer", isServer);
			nettyRmiServerBean.getPropertyValues().add("isClient", isClient);
			nettyRmiServerBean.getPropertyValues().add("cacheSerialize", cacheSerialize);
			nettyRmiServerBean.getPropertyValues().add("rmiBeanFactory", rmiBeanFactory);
			// nettyServerBean.getPropertyValues().add("messageHandle", messageHandle);
			// nettyServerBean.getPropertyValues().add("registrationFactory", registrationFactory);
			nettyRmiServerBean.getPropertyValues().add("registerServiceHandler", registerServiceHandler);
			defaultListableBeanFactory.registerBeanDefinition("nettyRmiServer", nettyRmiServerBean);

			NettyServer nettyServer = (NettyServer) applicationContext.getBean("nettyRmiServer");
			nettyServer.run();
			NettyClient nettyClient;

		} catch (Exception e) {
			LOG.error("NettyServer.class", e);
		}
	}
}
