package com.cheuks.bin.original.db.config;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public abstract class AbstractConfig implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	protected final static Map<String, String> PROPERTIES = new ConcurrentSkipListMap<String, String>();

	public abstract AbstractConfig makeConfig(Element element, ParserContext parserContext);

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

	public InetAddress checkInterface() throws Exception {

		int count = 0;
		InetAddress result = null;
		InetAddress[] addresses = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
		for (InetAddress address : addresses) {
			if (address instanceof Inet4Address) {
				if (++count > 1)
					throw new Exception("NetWorkInterface is more than 1.you music setting server ipaddress or domain name.");
				result = address;
			}
		}
		return result;
	}
}
