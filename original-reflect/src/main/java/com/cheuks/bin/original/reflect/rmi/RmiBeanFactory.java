package com.cheuks.bin.original.reflect.rmi;

import java.util.Map;

import com.cheuks.bin.original.reflect.rmi.model.MethodBean;

public interface RmiBeanFactory {

	boolean isActivate();

	<T> T getBean(Class<T> c) throws Throwable;

	<T> T getBean(String serviceName) throws Throwable;

	MethodBean getMethod(String code) throws Throwable;

	void init(Map<String, Object> args);

}