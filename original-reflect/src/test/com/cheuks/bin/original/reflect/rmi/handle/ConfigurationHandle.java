package com.cheuks.bin.original.reflect.rmi.handle;

import java.util.Map;

import javassist.CtClass;

/***
 * xml/注解
 * 
 * @author ben
 *
 */
public interface ConfigurationHandle {

	/***
	 * 注解类
	 * 
	 * @return
	 */
	Class<?> getAnnotationClass();

	/***
	 * 没XML就检查注解
	 * 
	 * @param ctClass
	 * @param xmlConfiguration
	 * @return
	 */
	Map<String, Object> getConfiguration(final CtClass ctClass, final Map<String, Object> xmlConfiguration);

}
