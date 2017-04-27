package com.cheuks.bin.original.reflect.rmi;

public interface RegisterService {

	final String SERVICE_ROOT = "/orinal_rmi";
	final String SERVICE_CUSTOMER = "/customer";
	final String SERVICE_PRODUCER = "/producer";
	final String SERVICE_LOAD = "/load";
	final String SERVICE_LEDDER = "/ledder";
	final String SEPARATOR = "@";

	/***
	 * 获取注册目录路径
	 * 
	 * @return
	 * @throws Throwable
	 */
	String getRegisterDirectory() throws Throwable;

	/***
	 * 注册信息
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Throwable
	 */
	String register(String directory, String value) throws Throwable;

	/***
	 * 注消
	 * 
	 * @param key
	 * @return
	 * @throws Throwable
	 */
	void unRegister(String directory) throws Throwable;
}
