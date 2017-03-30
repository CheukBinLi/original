package com.cheuks.bin.original.test.mq;

import java.util.List;

public interface MessageQueueConsumerFactory {

	void RegisterHandler(MessageQueueConsumerHandler handler);

	void RegisterHandler(List<MessageQueueConsumerHandler> handlers);

	void messageListenerEvent();

}
