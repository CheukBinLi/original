package com.cheuks.bin.original.common.rmi.net;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.model.ConsumerValueModel;
import com.cheuks.bin.original.common.util.pool.ObjectPoolManager;

/***
 * 
 * @Title: original-rmi
 * @Description:
 * @see
 *      <p>
 *      T Client 的类型(Bootstrap)
 *      <p>
 *      H、I ObjectPoolManager的类型(<NettyClientHandle, InetSocketAddress>)
 *      <p>
 *      S, V LoadBalanceFactory 的类型(<String, Void>)
 *      <p>
 *      C operationComplete 的类型(<Channel>)
 *      <p>
 *      R rmiConfigGroup 配置集合
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年9月12日 上午10:48:04
 *
 *       defaultType : <Bootstrap,NettyClientHandle,InetSocketAddress,String,Void,RmiConfigArg,Boolean,Channel>
 */
public interface NetworkClient<T, H, I, S, V, C, R> {

	/***
	 * 初始化
	 */
	void start();

	ObjectPoolManager<H, I> getObjectPoolManager();

	NetworkClient<T, H, I, S, V, C, R> setObjectPoolManager(ObjectPoolManager<H, I> objectPoolManager);

	T getClient();

	NetworkClient<T, H, I, S, V, C, R> setCacheSerialize(CacheSerialize cacheSerialize);

	LoadBalanceFactory<S, V> getLoadBalanceFactory();

	NetworkClient<T, H, I, S, V, C, R> setLoadBalanceFactory(LoadBalanceFactory<S, V> loadBalanceFactory);

	R getRmiConfigGroup();

	NetworkClient<T, H, I, S, V, C, R> setRmiConfigGroup(R rmiConfigGroup);

	/***
	 * 连接完城
	 * 
	 * @param channel
	 * @throws Throwable
	 */
	void operationComplete(C channel, String serverName, String serviceName, String serverUrl, String consumerName, String consumerUrl) throws Throwable;

	/***
	 * 连接完成，向对象池添加，worker
	 * 
	 * @param nettyClientHandle
	 * @throws Throwable
	 */
	void addWorker(H nettyClientHandle) throws Throwable;

	/***
	 * 掉线更搞服务器
	 * 
	 * @param nettyClientHandle
	 * @throws Throwable
	 */
	void changeServerConnection(H nettyClientHandle) throws Throwable;

	void changeServerConnection(ConsumerValueModel consumerValueModel) throws Throwable;

}