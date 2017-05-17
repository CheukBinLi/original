package com.cheuks.bin.original.common.rmi;

import com.cheuks.bin.original.common.rmi.model.ClassBean;
import com.cheuks.bin.original.common.rmi.model.MethodBean;

public interface RmiBeanFactory<ARG> {

	ClassBean getBean(Class<?> c) throws RmiException;

	ClassBean getBean(String serviceName) throws RmiException;

	MethodBean getMethod(String code) throws RmiException;

	void start(ARG arg);

}