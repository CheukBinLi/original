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
 */
public interface RmiBeanFactory<T extends MethodBean> {

	/***
	 * 缓存池对象名
	 */
	String POOL_OBJECT_FIELD_NAME = "METHOD_POOL";

	<V> V getBean(Class<?> c) throws RmiException;

	<V> V getBean(String serviceName) throws RmiException;

	MethodBean getMethod(String code) throws RmiException;

	void putMethod(String code, T methodBean) throws RmiException;
}
