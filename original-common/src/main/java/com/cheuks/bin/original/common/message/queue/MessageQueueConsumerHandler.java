package com.cheuks.bin.original.common.message.queue;

/***
 * 消息队列-消息者-处理类
 * 
 * @author Administrator
 *
 */

/***
 * 
 * @author Administrator
 *
 * @param <M> 收到的消息对象
 */
public interface MessageQueueConsumerHandler<Q, M> {

	/***
	 * 处理的队列名信息
	 * 
	 * @return
	 */
	Q getQueueInfo();

	/***
	 * 
	 * @param value 消息内容
	 * @param message 消息对像
	 */
	void doProcess(String value, final M message);

}
