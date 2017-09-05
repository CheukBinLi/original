package com.cheuks.bin.original.common.rmi;

import com.cheuks.bin.original.common.rmi.model.MethodBean;

public interface RmiBeanFactory {

    <T> T getBean(Class<?> c) throws RmiException;

    <T> T getBean(String serviceName) throws RmiException;

    MethodBean getMethod(String code) throws RmiException;

    void start(Object arg);

}
