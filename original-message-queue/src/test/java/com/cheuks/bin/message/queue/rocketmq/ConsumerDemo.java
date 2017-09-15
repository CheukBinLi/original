package com.cheuks.bin.message.queue.rocketmq;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemo {

	private final static Logger LOG = LoggerFactory.getLogger(ConsumerDemo.class);

	private static DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerGroupName");
	private static int initialState = 0;

	public static DefaultMQPushConsumer getDefaultMQPushConsumer() {
		if (consumer == null) {
			consumer = new DefaultMQPushConsumer("ConsumerGroupName");
		}
		if (initialState == 0) {
			consumer.setNamesrvAddr("192.168.3.27:9876");
			// consumer.setNamesrvAddr("10.73.11.117:9876");
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			initialState = 1;
		}

		return consumer;
	}

	public static void main(String[] args) {
		// 获取消息生产者
		DefaultMQPushConsumer consumer = getDefaultMQPushConsumer();

		// 订阅主体
		try {
			consumer.subscribe("tt1", "*");
			// 批量
			consumer.setConsumeMessageBatchMaxSize(1000);
			consumer.registerMessageListener(new MessageListenerConcurrently() {

				/**
				 * * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
				 */
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

					LOG.info("currentThreadName:{} and Receive New Messages:{}", Thread.currentThread().getName(), msgs);

					MessageExt msg = msgs.get(0);

					if (msg.getTopic().equals("tt1")) {
						// 执行TopicTest1的消费逻辑
						System.out.println(String.format("{tag:%s,msg:%s}", msg.getTags(), new String(msg.getBody())));
						if (msg.getTags() != null && msg.getTags().equals("TagA")) {
							// 执行TagA的消费
							LOG.info("MsgBody:{}", new String(msg.getBody()));
						} else if (msg.getTags() != null && msg.getTags().equals("TagC")) {
							// 执行TagC的消费
						} else if (msg.getTags() != null && msg.getTags().equals("TagD")) {
							// 执行TagD的消费
						}
					} else if (msg.getTopic().equals("TopicTest2")) {
						// 执行TopicTest2的消费逻辑
					}

					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});

			/**
			 * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
			 */
			consumer.start();

			LOG.info("Consumer Started.");
		} catch (MQClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
