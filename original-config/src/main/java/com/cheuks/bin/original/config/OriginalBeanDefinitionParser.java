package com.cheuks.bin.original.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class OriginalBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);
		// System.out.println(element);
		parserContext.getRegistry().registerBeanDefinition("abc", BeanDefinitionBuilder.genericBeanDefinition(Original.class).getRawBeanDefinition());
		Object o = parserContext.getRegistry().getBeanDefinitionNames();
		parserContext.getRegistry().removeBeanDefinition("abc");
		// builder.addPropertyValue("id", element.getAttribute("id"));
		element.setAttribute("id", element.getAttribute("interface"));
		builder.addPropertyValue("interface", element.getAttribute("interface"));
		builder.addPropertyValue("alias", element.getAttribute("alias"));
		builder.addPropertyValue("class", element.getAttribute("class"));
		builder.addPropertyValue("ref", element.getAttribute("ref"));
		builder.addPropertyValue("version", element.getAttribute("version"));
		builder.addPropertyValue("multiInstance", element.getAttribute("multiInstance"));
		System.out.println(element);
		// builder.addPropertyValue("alias", Integer.parseInt(element.getAttribute("alias")));

	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		// Class<?> o = super.getBeanClass(element);
		return Service.class;
	}

}
