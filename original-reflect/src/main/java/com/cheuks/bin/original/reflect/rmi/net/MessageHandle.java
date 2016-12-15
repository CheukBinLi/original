package com.cheuks.bin.original.reflect.rmi.net;

/***
 * 消息处理接口
 * 
 * @author ben
 *
 * @param <Input>
 * @param <Value>
 * @param <Args>
 */
public interface MessageHandle<Input, Value, Args> extends HandleType {

	Input getObject();

	// 服务处理:service.invoke(this,value)...
	/***
	 * 
	 * @param in
	 * @param v
	 * @param serviceType
	 * @throws Exception
	 */
	void messageHandle(final Input in, final Value v, final int serviceType) throws Exception;

	void addHandle(int serviceType, ServiceHandle<?, ?> ServiceHandle);

	boolean handleContains(int serviceType);

	void start(Args args);
}
