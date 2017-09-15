package com.cheuks.bin.message.queue.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class ProducerDemo {
	
	private static DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
	private static int initialState = 0;

	public static DefaultMQProducer getDefaultMQProducer() {
		if (producer == null) {
			producer = new DefaultMQProducer("ProducerGroupName");
		}

		if (initialState == 0) {
			producer.setNamesrvAddr("192.168.3.27:9876");
//			producer.setNamesrvAddr("10.73.11.117:9876");
			try {
				producer.start();
			} catch (MQClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

			initialState = 1;
		}
		return producer;
	}

	public static void main(String[] args) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
//		getDefaultMQProducer().send(new Message("tt1","A", "哇哈哈".getBytes()));
		getDefaultMQProducer().send(new Message("tt1","A", "哇哈哈".getBytes()));
	}

}
