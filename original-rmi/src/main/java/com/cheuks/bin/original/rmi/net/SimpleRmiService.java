package com.cheuks.bin.original.rmi.net;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.rmi.RmiBeanFactory;
import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.rmi.SimpleRmiBeanFactory;
import com.cheuks.bin.original.rmi.config.ReferenceGroupConfig.ReferenceGroup;
import com.cheuks.bin.original.rmi.config.ReferenceGroupConfig.ReferenceGroupModel;
import com.cheuks.bin.original.rmi.config.RmiConfigArg;
import com.cheuks.bin.original.rmi.config.ServiceGroupConfig.ServiceGroup;
import com.cheuks.bin.original.rmi.config.model.ProtocolModel;
import com.cheuks.bin.original.rmi.config.model.RegistryModel;
import com.cheuks.bin.original.rmi.config.model.ScanModel;
import com.cheuks.bin.original.rmi.net.netty.NettyRmiInvokeClientImpl;
import com.cheuks.bin.original.rmi.net.netty.client.NettyClient;
import com.cheuks.bin.original.rmi.net.netty.client.NettyClientPool;
import com.cheuks.bin.original.rmi.net.netty.server.NettyServer;

public class SimpleRmiService implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleRmiService.class);
	private CacheSerialize cacheSerialize;
	// private int poolSize = 15;
	private RmiBeanFactory<RmiConfigArg, Boolean> rmiBeanFactory;
	private RmiConfigArg rmiConfigArg;

	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("init -- NettyServer");

			ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) event.getApplicationContext();
			DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

			// ProtocolConfig protocolConfig = applicationContext.getBean(ProtocolConfig.class);
			// RegistryConfig registryConfig = applicationContext.getBean(RegistryConfig.class);

			// 加载参数
			RegistryModel registryConfig = applicationContext.getBean(RegistryModel.class);
			ProtocolModel protocolConfig = applicationContext.getBean(ProtocolModel.class);
			ScanModel scanConfig = null;
			if (applicationContext.containsBean(RmiContant.RMI_CONFIG_BEAN_SCAN)) {
				scanConfig = applicationContext.getBean(ScanModel.class);
			}
			ServiceGroup serviceGroupConfig = applicationContext.getBean(ServiceGroup.class);
			ReferenceGroup referenceGroupConfig = applicationContext.containsBean(RmiContant.RMI_CONFIG_BEAN_REFERENCE_GROUP) ? applicationContext.getBean(ReferenceGroup.class) : null;
			rmiConfigArg = new RmiConfigArg(registryConfig, protocolConfig, scanConfig, serviceGroupConfig, referenceGroupConfig);

			String tempString;
			cacheSerialize = (tempString = protocolConfig.getRefSerialize()).length() > 0 ? (CacheSerialize) applicationContext.getBean(tempString) : null;

			rmiBeanFactory = (tempString = protocolConfig.getRefSerialize()).length() > 0 ? (RmiBeanFactory) applicationContext.getBean(tempString) : null;
			if (null == rmiBeanFactory) {
				BeanDefinition rmiBeanFactoryBean = new RootBeanDefinition(SimpleRmiBeanFactory.class);
				defaultListableBeanFactory.registerBeanDefinition("rmiBeanFactory", rmiBeanFactoryBean);
				rmiBeanFactory = (RmiBeanFactory) applicationContext.getBean("rmiBeanFactory");
			}

			if (null != serviceGroupConfig && !serviceGroupConfig.getServiceGroupConfig().isEmpty()) {
				BeanDefinition nettyRmiServerBean = new RootBeanDefinition(NettyServer.class);
				nettyRmiServerBean.getPropertyValues().add("cacheSerialize", cacheSerialize);
				nettyRmiServerBean.getPropertyValues().add("rmiBeanFactory", rmiBeanFactory);
				// nettyServerBean.getPropertyValues().add("registrationFactory", registrationFactory);
				// nettyRmiServerBean.getPropertyValues().add("messageHandle", messageHandle);
				nettyRmiServerBean.getPropertyValues().add("rmiConfigArg", rmiConfigArg);
				defaultListableBeanFactory.registerBeanDefinition("nettyRmiServer", nettyRmiServerBean);

				NettyServer nettyServer = (NettyServer) applicationContext.getBean("nettyRmiServer");
				nettyServer.run();
			}
			if (null != referenceGroupConfig && !referenceGroupConfig.getReferenceGroup().isEmpty()) {

				// 初始化客户端
				ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();

				MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
				BeanDefinition nettyRmiClientBean = new RootBeanDefinition(NettyClient.class, constructorArgumentValues, mutablePropertyValues);
//				nettyRmiClientBean.getPropertyValues().add("rmiBeanFactory", rmiBeanFactory);
				nettyRmiClientBean.getPropertyValues().add("cacheSerialize", cacheSerialize);
				nettyRmiClientBean.getPropertyValues().add("rmiConfigArg", rmiConfigArg);
				defaultListableBeanFactory.registerBeanDefinition("nettyRmiClient", nettyRmiClientBean);

				BeanDefinition rmiClientBean = new RootBeanDefinition(NettyRmiInvokeClientImpl.class);
				defaultListableBeanFactory.registerBeanDefinition("rmiClientBean", rmiClientBean);
				 NettyClient nettyClient = (NettyClient) applicationContext.getBean("nettyRmiClient");
//				NettyClient nettyClient = applicationContext.getBean(NettyClient.class);
				nettyClient.start();
				// 根据服务初始各服务线程池
				// referenceGroup
				NettyClientPool pool;
				for (Entry<String, ReferenceGroupModel> en : referenceGroupConfig.getReferenceGroup().entrySet()) {
					pool = new NettyClientPool(nettyClient, null, rmiConfigArg, -1, en.getKey());
					pool.start();
				}

			}
			System.err.println("aaaaa");
		} catch (Throwable e) {
			LOG.error("NettyServer.class", e);
		}
	}
}
