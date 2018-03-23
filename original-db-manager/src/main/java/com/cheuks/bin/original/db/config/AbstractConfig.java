package com.cheuks.bin.original.db.config;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.cheuks.bin.original.db.config.model.DbConfigModel;

public abstract class AbstractConfig implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	public abstract AbstractConfig makeConfig(Element element, ParserContext parserContext);

	public BeanDefinition getDbConfigModel(ParserContext parserContext) {
		BeanDefinition result = getConfig(parserContext, DbConfigModel.DB_CONFIG_MODEL);
		if (null == result)
			return registerBeanDefinition(parserContext, DbConfigModel.class, null, null, null);
		return result;
	}

	protected BeanDefinition registerBeanDefinition(ParserContext parserContext, Class<?> bean, String beanName, Map<String, Object> pagams, String initMethod, Object... constructorArgumentValues) {

		ConstructorArgumentValues constructor = new ConstructorArgumentValues();
		if (null != constructorArgumentValues) {
			for (Object arg : constructorArgumentValues)
				constructor.addGenericArgumentValue(arg);
		}
		MutablePropertyValues mutablePropertyValues = new MutablePropertyValues(pagams);
		RootBeanDefinition beanDefinition = new RootBeanDefinition(bean, constructor, mutablePropertyValues);
		if (null != initMethod && initMethod.length() > 0)
			beanDefinition.setInitMethodName(initMethod);
		// beanDefinition.setScope("singleton");
		// beanDefinition.setPrimary(true);
		parserContext.getRegistry().registerBeanDefinition(beanName, beanDefinition);
		return beanDefinition;
	}

	protected BeanDefinition getConfig(ParserContext parserContext, String beanName) {
		return parserContext.getRegistry().getBeanDefinition(beanName);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
