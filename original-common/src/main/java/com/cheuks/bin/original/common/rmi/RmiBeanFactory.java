package com.cheuks.bin.original.common.rmi;

import com.cheuks.bin.original.common.rmi.model.MethodBean;

/***
 * 
 * @Title: original-common
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年9月7日 下午4:41:35
 * @param ARG
 *            启动加载参加
 * @param E
 *            扩展参数
 */
public interface RmiBeanFactory<ARG, E> {

	<T> T getBean(Class<?> c) throws RmiException;

	<T> T getBean(String serviceName) throws RmiException;

	MethodBean getMethod(String code) throws RmiException;

	/***
	 * 
	 * @param arg
	 *            启动加载参加
	 * @param extend
	 *            扩展参数
	 */
	void start(ARG arg, E extend);

}
