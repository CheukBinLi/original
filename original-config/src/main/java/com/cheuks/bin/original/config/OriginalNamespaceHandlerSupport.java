package com.cheuks.bin.original.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class OriginalNamespaceHandlerSupport extends NamespaceHandlerSupport {

	public void init() {
		// registerBeanDefinitionDecorator("service", new OriginalPraser(Original.class));
		registerBeanDefinitionParser("service", new OriginalBeanDefinitionParser());
		// registerBeanDefinitionParser("AAAA", new OriginalBeanDefinitionParser());
	}

}
