package com.cheuks.bin.original.rmi.net;

import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.RmiBeanFactory;

public interface RmiClient<T, R> {

	RmiClient<T, R> setHeartBeatTimeoutSecond(int heartBeatTimeoutSecond);

	RmiClient<T, R> setRmiBeanFactory(RmiBeanFactory rmiBeanFactory);

	/***
	 * 设置数据块大小
	 * 
	 * @return
	 */
	RmiClient<T, R> setMaxFrameLength(int maxFrameLength);

	/** 消息处理线数 */
	RmiClient<T, R> setHandleThreads(int handleThreads);

	/** 注解扫描路径 */
	RmiClient<T, R> setScanPath(String path);

	/***
	 * 服务器列表<br>
	 * zookeeper://192.168.1.1:2181,192.168.1.2:2181
	 * 
	 * @return
	 */
//	RmiClient setServerList(String serverList);
//
//	/** 应用名称 */
//	RmiClient setApplicationName(String applicationName);

	RmiClient setLoadBalanceFactory(LoadBalanceFactory<T, R> loadBalanceFactory);

}
