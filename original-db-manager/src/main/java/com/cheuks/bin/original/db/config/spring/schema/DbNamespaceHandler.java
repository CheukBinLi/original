package com.cheuks.bin.original.db.config.spring.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class DbNamespaceHandler extends NamespaceHandlerSupport {

	private final static Logger LOG = LoggerFactory.getLogger(DbNamespaceHandler.class);

	public void init() {
		try {
//			registerBeanDefinitionParser("config", new DbBeanDefinitionParser(RmiConfig.class));
//			registerBeanDefinitionParser("service-group", new DbBeanDefinitionParser(ServiceGroupConfig.class));
//			registerBeanDefinitionParser("reference-group", new DbBeanDefinitionParser(ReferenceGroupConfig.class));
//			registerBeanDefinitionParser("annotation-driven", new DbBeanDefinitionParser(AnnotationDrivenConfig.class));
		} catch (Exception e) {
			LOG.error(null, e);
		}
	}

}
