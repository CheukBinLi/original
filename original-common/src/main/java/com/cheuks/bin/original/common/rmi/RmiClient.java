package com.cheuks.bin.original.common.rmi;

/***
 * 
 * @Title: original-common
 * @Description:
 *               <p>
 *               T :LoadBalanceFactory 方法带resource返回类型
 *               <p>
 *               R:LoadBalanceFactory 方法带registration返回类型
 *               <p>
 *               C 连接客户端类型
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

	RmiClient<T, R, C, CONFIG> setRmiConfigArg(CONFIG rmiConfigArg);

	LoadBalanceFactory<T, R> getLoadBalanceFactory();

	RmiClient<T, R, C, CONFIG> setLoadBalanceFactory(LoadBalanceFactory<T, R> loadBalanceFactory);

	RmiClient<T, R, C, CONFIG> setServiceName(String serviceName);

	RmiClient<T, R, C, CONFIG> setNettyClient(C client);

}
