package com.cheuks.bin.original.common.message.queue;

import java.util.Map;

public interface MessageQueueProducerFactory<R, C> {

	/***
	 * 消息生产
	 * 
	 * @param queueName 队列名/主题名
	 * @param message 消息内容
	 * @param additional 附加对象，扩展用,特定的偶合
	 * @return
	 * @throws MessageQueueException
	 */
	R makeMessage(String queueName, String message, Object additional) throws MessageQueueException;

	/***
	 * 
	 * @param queueName 队列名/主题名
	 * @param message 消息内容
	 * @param additional 附加对象，扩展用,特定的偶合
	 * @param callBack
	 * @return
	 * @throws MessageQueueException 回调
	 */
	R makeMessage(String queueName, String message, Object additional, MessageQueueCallBack<C> callBack) throws MessageQueueException;

	/***
	 * 服务初始化
	 */
	MessageQueueProducerFactory<R, C> init(Map<String, Object> args);

	/***
	 * 销毁服务
	 */
	void destory();
}
