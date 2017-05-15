package com.cheuks.bin.original.rmi.config.spring.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.cheuks.bin.original.rmi.config.ReferenceGroupConfig;
import com.cheuks.bin.original.rmi.config.RmiConfig;
import com.cheuks.bin.original.rmi.config.ServiceGroupConfig;

public class RmiNamespaceHandler extends NamespaceHandlerSupport {

	private final static Logger LOG = LoggerFactory.getLogger(RmiNamespaceHandler.class);

	public void init() {
		try {
			registerBeanDefinitionParser("config", new RmiBeanDefinitionParser(RmiConfig.class));
			registerBeanDefinitionParser("service-group", new RmiBeanDefinitionParser(ServiceGroupConfig.class));
			registerBeanDefinitionParser("reference-group", new RmiBeanDefinitionParser(ReferenceGroupConfig.class));
		} catch (Exception e) {
			LOG.error(null, e);
		}
	}

}
