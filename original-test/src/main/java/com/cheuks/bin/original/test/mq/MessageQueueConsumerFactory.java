package com.cheuks.bin.original.test.mq;

import java.util.List;
import java.util.Map;

public interface MessageQueueConsumerFactory {

	void RegisterHandler(MessageQueueConsumerHandler handler);

	void RegisterHandler(List<MessageQueueConsumerHandler> handlers);

	// void messageListenerEvent();

	/***
	 * 服务初始化
	 */
	MessageQueueConsumerFactory init(Map<String, Object> args);

	/***
	 * 销毁服务
	 */
	void destory();

}
