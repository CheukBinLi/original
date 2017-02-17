package com.cheuks.bin.original.reflect.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.cheuks.bin.original.reflect.rmi.SimpleRmiService;

public class RegistryConfig extends AbstractConfig {

	private static final long serialVersionUID = 1L;
	// private String applicationAddress;
	private int baseSleepTimeMs;
	private int maxRetries;
	private String address;

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		BeanDefinition beanDefinition = new RootBeanDefinition(RegistryConfig.class);
		beanDefinition.getPropertyValues().add("baseSleepTimeMs", Integer.valueOf(element.getAttribute("baseSleepTimeMs")));
		beanDefinition.getPropertyValues().add("maxRetries", Integer.valueOf(element.getAttribute("maxRetries")));
		beanDefinition.getPropertyValues().add("address", element.getAttribute("address"));
		parserContext.getRegistry().registerBeanDefinition("rmiRegistryConfig", beanDefinition);

		BeanDefinition simpleRmiService = new RootBeanDefinition(SimpleRmiService.class);
		parserContext.getRegistry().registerBeanDefinition("simpleRmiService", simpleRmiService);

		return this;
	}

	public int getBaseSleepTimeMs() {
		return baseSleepTimeMs;
	}

	public RegistryConfig setBaseSleepTimeMs(int baseSleepTimeMs) {
		this.baseSleepTimeMs = baseSleepTimeMs;
		return this;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public RegistryConfig setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public RegistryConfig setAddress(String address) {
		this.address = address;
		return this;
	}

}
