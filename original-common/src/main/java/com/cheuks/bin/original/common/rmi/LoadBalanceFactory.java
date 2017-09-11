package com.cheuks.bin.original.common.rmi;

import java.util.List;

import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel;

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
	 * 服务协议地址
	 * 
	 * @param url
	 */
	void setUrl(String url);

	/***
	 * 获取资源 ServiceType {client, server, service, provider, consumer, ledder, load} *
	 * <p>
	 * client:返回 服务器名@连接地址
	 * <p>
	 * ledder:返回 连接地址
	 * <p>
	 * service 返回 list<服务名>
	 * <p>
	 * provider 返回所及提供者信息
	 * <p>
	 * consumer 返回所有消费者信息
	 * 
	 * @return
	 */
	List<T> getResource(RegisterLoadBalanceModel registerInfo) throws Throwable;

	/***
	 * 获取资源并登记
	 * 
	 * @param registerInfo
	 * @return
	 */
	T getResourceAndUseRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable;

	/**
	 * 使用登记
	 * 
	 * @param registerInfo
	 *            server: 提供者名
	 *            <p>
	 *            service:服务名
	 *            <p>
	 *            url: 提供者连接地址
	 *            <p>
	 *            value: 消费者名
	 *            <p>
	 *            desc: 消费者连接地址
	 * @return
	 */
	R useRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable;

	/***
	 * 登记
	 * 
	 * @param registerInfo
	 * @return
	 */
	R registration(RegisterLoadBalanceModel registerInfo) throws Throwable;

	/***
	 * 注销登记
	 * 
	 * @param registerInfo
	 */
	void cancleRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable;

	/***
	 * 初始化
	 */
	void init() throws Throwable;
}
