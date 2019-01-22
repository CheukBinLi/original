package com.cheuks.bin.original.common.util.design.factory;

public interface HandlerManager<T extends Handler, Type> {

	/***
	 * handler 类型基类
	 * 
	 * @return
	 */
	Class<T> getSuperHandler();

	/***
	 * handler类型
	 * 
	 * @param type
	 * @return
	 */
	T getHandler(Type type);

	boolean concat(Type type);

	boolean concat(Class<T> c);

	T instance(Class<T> clazz);

	void addHandler(T t);

	void remove(Type type);

	void init();

}
