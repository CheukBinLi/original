package com.cheuks.bin.original.test.mq.kafka;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class ComsunerDemo {

	volatile boolean RUNNING = true;

	void a() {

		Properties props = new Properties();
		props.put("bootstrap.servers", "10.73.11.117:9092");// 该地址是集群的子集，用来探测集群。
		props.put("group.id", "test_MBA_AAAAA_1");// 不同ID 可以同时订阅消息
		props.put("enable.auto.commit", "false");// 自动提交offsets
		props.put("auto.commit.interval.ms", "1000");// 每隔1s，自动提交offsets
		props.put("session.timeout.ms", "30000");// Consumer向集群发送自己的心跳，超时则认为Consumer已经死了，kafka会把它的分区分配给其他进程
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList("foo", "bar", "my-topic"));// 订阅TOPIC
		try {
			while (RUNNING) {// 轮询
				ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
				for (TopicPartition partition : records.partitions()) {
					List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
					for (ConsumerRecord<String, String> record : partitionRecords) {
						// 可以自定义Handler,处理对应的TOPIC消息(partitionRecords.key())
						System.out.println(record.offset() + ": " + record.value());
					}
					consumer.commitSync();// 同步
				}
			}
		} finally {
			consumer.close();
		}
	}

	public static void main(String[] args) {
		new ComsunerDemo().a();
	}

}
