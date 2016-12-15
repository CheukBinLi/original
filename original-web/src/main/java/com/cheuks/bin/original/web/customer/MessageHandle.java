package com.cheuks.bin.original.web.customer;

public interface MessageHandle<Consumer extends DefaultMessageInbound, Message extends MessagePackage> extends MessageOption {

	/***
	 * 消息调度
	 * 
	 * @param message
	 */
	public void dispatcher(String message) throws Throwable;

	/***
	 * 没有找到客服/客服全下线
	 * 
	 * @param message
	 * @throws Throwable
	 */
	public void notFoundCustomerService(Message message) throws Throwable;

	/***
	 * 发送给客服
	 * 
	 * @param c
	 * @param v
	 */
	public void sendToConsumerService(Message message) throws Throwable;

	/***
	 * 客服回复
	 * 
	 * @param c
	 * @param message
	 * @param v
	 */
	public void sendToSystem(Message message) throws Throwable;

	/***
	 * 添加连接
	 * 
	 * @param c
	 * @return
	 */
	public MessageHandle<Consumer, MessagePackage> addConnection(Consumer c) throws Throwable;

	/***
	 * 销毁对象
	 * 
	 * @param c
	 */
	public void destory(Consumer c) throws Throwable;

	/***
	 * 销毁对象
	 * 
	 * @param containerType
	 *            容器类型
	 * @param o
	 *            销毁对象
	 * @throws Throwable
	 */
	public void destory(ContainerType containerType, Object o) throws Throwable;

	/***
	 * 在线数量
	 * 
	 * @return
	 */
	public int activityNumber();

}
