package com.cheuks.bin.original.rmi;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cheuks.bin.original.common.rmi.RmiBeanFactory;
import com.cheuks.bin.original.common.rmi.RmiException;
import com.cheuks.bin.original.rmi.model.MethodBean;

@SuppressWarnings("unchecked")
public class DefaultRmiBeanFactory implements RmiBeanFactory<MethodBean>, ApplicationContextAware {

	private Map<String, MethodBean> METHOD_POOL;

	private ApplicationContext applicationContext;

	public <T> T getBean(Class<?> c) throws RmiException {
		return (T) applicationContext.getBean(c);
	}

	public <T> T getBean(String serviceName) throws RmiException {
		Object result = applicationContext.getBean(serviceName);
		return null == result ? null : (T) result;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public MethodBean getMethod(String code) throws RmiException {
		// 缺多例实现
		return METHOD_POOL.get(code);
	}

	public void putMethod(String code, MethodBean methodBean) throws RmiException {
		METHOD_POOL.put(code, methodBean);
	}

	public Map<String, MethodBean> getMETHOD_POOL() {
		return METHOD_POOL;
	}

	public void setMETHOD_POOL(Map<String, MethodBean> mETHOD_POOL) {
		METHOD_POOL = mETHOD_POOL;
	}

}
