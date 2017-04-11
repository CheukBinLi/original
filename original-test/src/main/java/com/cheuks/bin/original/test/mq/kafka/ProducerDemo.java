package com.cheuks.bin.original.test.mq.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerDemo {

	void a() {
		Properties props = new Properties();
//		props.put("bootstrap.servers", "10.17.38.12:9089");
		props.put("bootstrap.servers", "10.73.11.117:9092");
		props.put("acks", "all");// 记录完整提交，最慢的但是最大可能的持久化
		props.put("retries", 10);// 请求失败重试的次数
		props.put("batch.size", 16384);// batch的大小
        props.put("linger.ms", 1);// 默认情况即使缓冲区有剩余的空间，也会立即发送请求，设置一段时间用来等待从而将缓冲区填的更多，单位为毫秒，producer发送数据会延迟1ms，可以减少发送到kafka服务器的请求数据
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		for (int i = 10000; i < 10010; i++)
			producer.send(new ProducerRecord<String, String>("my-topic", Integer.toString(i), Integer.toString(i)));
		producer.close();
		org.apache.kafka.common.serialization.StringSerializer a;
	}

	public static void main(String[] args) {
		new ProducerDemo().a();
	}
}
