package com.cheuks.bin.original.spring.plugin.util.design.factory;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.common.util.design.factory.AbstractHandlerManager;
import com.cheuks.bin.original.common.util.design.factory.Handler;

@SuppressWarnings("unchecked")
public abstract class DefaultHandlerManager<T extends Handler<?>> extends AbstractHandlerManager<T> implements ApplicationContextAware, InitializingBean {

	protected DefaultListableBeanFactory defaultListableBeanFactory;

	@Override
	public T instance(Class<T> clazz) {
		if (concat(clazz)) {
			return getHandler(clazz.getName());
		}
		return registerBean(clazz, StringUtil.toLowerCaseFirstOne(clazz.getName()), null, "init");
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
		this.defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	public T registerBean(Class<T> clazz, String registerName, Map<String, Object> initPropertyValue, String initMethodName) {

		T instance;
		try {
			instance = defaultListableBeanFactory.getBean(clazz);
		} catch (Throwable e) {
		}

		BeanDefinitionBuilder bean = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		if (null != initMethodName) {
			bean.setInitMethodName(initMethodName);
		}
		if (null != initPropertyValue) {
			for (Entry<String, Object> en : initPropertyValue.entrySet()) {
				bean.addPropertyValue(en.getKey(), defaultListableBeanFactory.getBeanDefinition(en.getKey()));
			}
		}
		String beanName = null == registerName ? StringUtil.toLowerCaseFirstOne(clazz.getSimpleName()) : registerName;
		defaultListableBeanFactory.registerBeanDefinition(beanName, bean.getRawBeanDefinition());
		instance = (T) defaultListableBeanFactory.getBean(beanName);
		return instance;
	}
}
