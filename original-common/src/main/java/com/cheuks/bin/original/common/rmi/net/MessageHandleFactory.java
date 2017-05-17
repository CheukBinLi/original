package com.cheuks.bin.original.common.rmi.net;

import com.cheuks.bin.original.common.rmi.RmiContent;
import com.cheuks.bin.original.common.rmi.RmiException;

/***
 * 
 * @Title: original-rmi
 * @Description: 消息处理工厂
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年4月28日 下午3:35:22
 *
 */
public interface MessageHandleFactory<Input, Value, Args> extends RmiContent {

	/***
	 * 
	 * @param in
	 * @param v
	 * @param serviceType
	 * @throws Exception
	 */
	void messageHandle(final Input in, final Value v, final int serviceType) throws RmiException;

	/***
	 * 
	 * @param serviceType 服务类型:request,response,ping等等
	 * @param messageHandle
	 */
	void registrationMessageHandle(int serviceType, MessageHandle<?, ?> messageHandle);

	/***
	 * 服务是否存在
	 * 
	 * @param serviceType 服务类型:request,response,ping等等
	 * @return
	 */
	boolean serviceTypeContains(int serviceType);

	/***
	 * 运行消息处理工厂
	 * 
	 * @param args
	 */
	void start(Args args);
}
