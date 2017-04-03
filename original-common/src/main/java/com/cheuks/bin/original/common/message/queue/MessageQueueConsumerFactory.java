package com.cheuks.bin.original.common.message.queue;

import java.util.List;
import java.util.Map;

/***
 * 
 * @author Administrator
 *
 * @param <M> 消费者：收到的消息对象
 */
public interface MessageQueueConsumerFactory<Q, M> {

	void RegisterHandler(MessageQueueConsumerHandler<Q, M> handler);

	void RegisterHandler(List<MessageQueueConsumerHandler<Q, M>> handlers);

	/***
	 * 服务初始化
	 */
	MessageQueueConsumerFactory<Q, M> init(Map<String, Object> args);

	/***
	 * 销毁服务
	 */
	void destory();

}
