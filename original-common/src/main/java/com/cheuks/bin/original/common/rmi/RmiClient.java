package com.cheuks.bin.original.common.rmi;

/***
 * 
 * @Title: original-common
 * @Description:客户端接口
 *                    <p>
 *                    T :LoadBalanceFactory 方法带resource返回类型
 *                    <p>
 *                    R:LoadBalanceFactory 方法带registration返回类型
 *                    <p>
 *                    C 连接客户端类型
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年9月10日 下午10:20:32
 * @see 泛型对照
 * @see <T, R, C, CONFIG>
 * @see <String, Void, NettyClient, RmiConfigArg>
 * 
 * @see T,R :LoadBalanceFactory 泛型对象
 * @see C setNettyClient 泛型对象
 * @see CONFIG 参数集
 *
 */
public interface RmiClient<T, R, C, CONFIG> {

	/***
	 * 设置参数集合
	 * 
	 * @param rmiConfigArg
	 * @return
	 */
	// RmiClient<T, R, C, CONFIG> setRmiConfigArg(CONFIG rmiConfigArg);

	/***
	 * 负载工厂
	 * 
	 * @return
	 */
	LoadBalanceFactory<T, R> getLoadBalanceFactory();

	RmiClient<T, R, C, CONFIG> setLoadBalanceFactory(LoadBalanceFactory<T, R> loadBalanceFactory);

	/***
	 * 服务名
	 * 
	 * @param serviceName
	 * @return
	 */
	RmiClient<T, R, C, CONFIG> setServiceName(String serviceName);

	/***
	 * 客户端这主控对象
	 * 
	 * @param client
	 * @return
	 */
	RmiClient<T, R, C, CONFIG> setNettyClient(C client);

}
