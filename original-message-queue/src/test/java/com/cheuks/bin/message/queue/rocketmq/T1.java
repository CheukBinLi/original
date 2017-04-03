package com.cheuks.bin.message.queue.rocketmq;

import java.util.concurrent.CountDownLatch;

import org.apache.rocketmq.client.producer.SendResult;
import org.junit.Test;

import com.cheuks.bin.original.common.message.queue.MessageQueueConsumerFactory;
import com.cheuks.bin.original.common.message.queue.MessageQueueConsumerHandler;
import com.cheuks.bin.original.common.message.queue.MessageQueueProducerFactory;

public class T1 {

	@Test
	public void consumer() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		MessageQueueConsumerFactory<String, RocketMqConsumerRecord> consumerFactory = new RocketMqMessageQueueConsumerFactory().setTopicList("rocketMq_Topic").setServerList("192.168.3.27:9876").setConsumerGroup("TEST_GROUP_ID").init(null);
		consumerFactory.RegisterHandler(new MessageQueueConsumerHandler<String, RocketMqConsumerRecord>() {

			public String getQueueInfo() {
				return "A1,A2,A3";
			}

			public void doProcess(String value, RocketMqConsumerRecord message) {
				System.out.println("tags:{A1,A2,A3}" + value);
			}
		});
		consumerFactory.RegisterHandler(new MessageQueueConsumerHandler<String, RocketMqConsumerRecord>() {

			public String getQueueInfo() {
				return "B1";
			}

			public void doProcess(String value, RocketMqConsumerRecord message) {
				System.out.println("tags:{B2}" + value);
			}
		});
		consumerFactory.RegisterHandler(new MessageQueueConsumerHandler<String, RocketMqConsumerRecord>() {

			public String getQueueInfo() {
				return "*";
			}

			public void doProcess(String value, RocketMqConsumerRecord message) {
				System.out.println("tags:{*}" + value);
			}
		});
		countDownLatch.await();
	}

	@Test
	public void producer() {

		MessageQueueProducerFactory<SendResult, SendResult> producerFactory = new RocketMqMessageQueueProducerFactory().setServerList("192.168.3.27:9876").setProducerGroupName("TEST").init(null);
//		producerFactory.makeMessage("rocketMq_Topic", "B1_MESSAGE", "B1");
		producerFactory.makeMessage("rocketMq_Topic", "A1_MESSAGE", "A1");
	}

}
