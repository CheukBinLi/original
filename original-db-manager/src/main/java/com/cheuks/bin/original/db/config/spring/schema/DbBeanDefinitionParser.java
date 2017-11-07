package com.cheuks.bin.original.db.config.spring.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.cheuks.bin.original.db.config.AbstractConfig;

public class DbBeanDefinitionParser implements BeanDefinitionParser {

	private final static Logger LOG = LoggerFactory.getLogger(DbBeanDefinitionParser.class);
	private final AbstractConfig instance;

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		try {
			AbstractConfig tempConfig = (AbstractConfig) instance.clone();
			tempConfig.makeConfig(element, parserContext);
		} catch (CloneNotSupportedException e) {
			LOG.error(null, e);
		}
		return null;
	}

	public DbBeanDefinitionParser(Class<? extends AbstractConfig> abstractConfig) throws InstantiationException, IllegalAccessException {
		super();
		instance = abstractConfig.newInstance();
	}

}
