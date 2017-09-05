package com.cheuks.bin.original.test.mq;

/***
 * 消息队列-消息者-处理类
 * 
 * @author Administrator
 *
 */
public interface MessageQueueConsumerHandler {

	/***
	 * 处理的队列名
	 * 
	 * @return
	 */
	String getQueueName();

	/***
	 * 
	 * @param value 消息内容
	 * @param originalObject 消息对像
	 */
	void doProcess(String value, final Object originalObject);

}
