package com.cheuks.bin.original.reflect.config.spring.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.cheuks.bin.original.reflect.config.ProtocolConfig;
import com.cheuks.bin.original.reflect.config.ReferenceConfig;
import com.cheuks.bin.original.reflect.config.RegistryConfig;
import com.cheuks.bin.original.reflect.config.ServiceConfig;

public class RmiNamespaceHandler extends NamespaceHandlerSupport {

	private final static Logger LOG = LoggerFactory.getLogger(RmiNamespaceHandler.class);

	public void init() {
		try {
			registerBeanDefinitionParser("service", new RmiBeanDefinitionParser(ServiceConfig.class));
			registerBeanDefinitionParser("reference", new RmiBeanDefinitionParser(ReferenceConfig.class));
			registerBeanDefinitionParser("protocol", new RmiBeanDefinitionParser(ProtocolConfig.class));
			registerBeanDefinitionParser("registry", new RmiBeanDefinitionParser(RegistryConfig.class));
		} catch (Exception e) {
			LOG.error("RmiNamespaceHandler.class", e);
		}
	}

}
