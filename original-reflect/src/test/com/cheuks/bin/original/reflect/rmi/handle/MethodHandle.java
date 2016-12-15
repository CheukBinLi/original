package com.cheuks.bin.original.reflect.rmi.handle;

import java.util.Map;

import javassist.CtClass;
import javassist.CtMethod;

public interface MethodHandle {

	/***
	 * 
	 * @param clazz
	 * @param method
	 * @param configuration
	 * @return methodBody:方法体
	 */
	String decorationmethod(final CtClass clazz, final CtMethod method, final Map<String, Object> configuration);

}
