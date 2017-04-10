package com.cheuks.bin.message.queue.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.cheuks.bin.original.common.message.queue.MessageQueueConsumerHandler;

public class SimpleKafkaMessageQueueConsumerHandler implements MessageQueueConsumerHandler<String, ConsumerRecord<String, String>> {

	public String getQueueInfo() {
		return "simpleKafka";
	}

	public void doProcess(String value, ConsumerRecord<String, String> message) {
		System.out.println(value);
	}

}
