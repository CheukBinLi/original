package com.cheuks.bin.original.test.mq;

public interface MessageQueueProducerFactory {

	/***
	 * 消息生产
	 * 
	 * @param topic 主题
	 * @param message 消息内容
	 * @param additional 附加对象，扩展用
	 * @return
	 */
	Object makeMessage(String topic, String message, Object additional);
}
