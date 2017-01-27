package com.cheuks.bin.original.reflect.config.spring.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.cheuks.bin.original.reflect.config.AbstractConfig;

public class RmiBeanDefinitionParser implements BeanDefinitionParser {

	// private final Class<? extends AbstractConfig> abstractConfig;
	private final static Logger LOG = LoggerFactory.getLogger(RmiBeanDefinitionParser.class);
	private final AbstractConfig instance;

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		try {
			AbstractConfig tempConfig = (AbstractConfig) instance.clone();
			tempConfig.makeConfig(element, parserContext);
		} catch (CloneNotSupportedException e) {
			LOG.error("RmiBeanDefinitionParser.class", e);
		}
		return null;
	}

	public RmiBeanDefinitionParser(Class<? extends AbstractConfig> abstractConfig) throws InstantiationException, IllegalAccessException {
		super();
		// this.abstractConfig = abstractConfig;
		instance = abstractConfig.newInstance();
	}

}
