package com.cheuks.bin.original.test.springconfig;

import java.util.List;
import java.util.Properties;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;
import com.cheuks.bin.original.registration.center.ZookeeperRegistrationFactory;

public class ZookeeperConfig  extends PropertyPlaceholderConfigurer{
//	public class ZookeeperConfig extends PropertiesFactoryBean {

	private RegistrationFactory<CuratorFramework, PathChildrenCacheEvent, NodeCache> client = new ZookeeperRegistrationFactory("192.168.2.4:2181");

//	{
//		try {
//			client.start();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//	}
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {

		try {
			List<String> list = client.getServiceList("/config");
			String temp;
			for (String str : list) {
				System.err.println(str+":"+(temp = client.getValue("/config/" + str)));
				props.put(str, temp);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		super.processProperties(beanFactoryToProcess, props);
	}

}
