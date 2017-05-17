package com.cheuks.bin.original.common.rmi;

/***
 * 
 * @Title: original-common
 * @Description: 负载工厂
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年4月28日 下午2:21:37
 *
 */
public interface LoadBalanceFactory<T, R> {

	/***
	 * 获取资源
	 * 
	 * @return
	 */
	T getResource(Object... registerInfo);

	/***
	 * 获取资源并登记
	 * 
	 * @param registerInfo
	 * @return
	 */
	T getResourceAndUseRegistration(Object... registerInfo);

	/**
	 * 使用登记
	 * 
	 * @param registerInfo
	 * @return
	 */
	R useRegistration(Object... registerInfo);

	/***
	 * 注销登记
	 * 
	 * @param registerInfo
	 */
	void cancleRegistration(Object... registerInfo);

	/***
	 * 初始化
	 */
	void init();
}
