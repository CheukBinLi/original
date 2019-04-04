package com.cheuks.bin.message.queue.kafka;

import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.cheuks.bin.original.common.message.queue.MessageQueueConsumerFactory;
import com.cheuks.bin.original.common.message.queue.MessageQueueConsumerHandler;
import com.cheuks.bin.original.common.message.queue.MessageQueueProducerFactory;

public class T1 {

	private MessageQueueConsumerFactory messageQueueConsumerFactory;

	private MessageQueueProducerFactory messageQueueProducerFactory;

	@Test
	public void consumer() {
		try {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			//			messageQueueConsumerFactory = new KafkaMessageQueueConsumerFactory("10.73.11.117:9091,10.73.11.117:9092", "T1_TOPIC,T2_TOPIC", "CCTV_1").init(null);
			//			messageQueueConsumerFactory = new KafkaMessageQueueConsumerFactory("192.168.3.27:9092", "T1_TOPIC,T2_TOPIC", "CCTV_1").init(null);
//			messageQueueConsumerFactory = new KafkaMessageQueueConsumerFactory("10.16.90.163:9089,10.16.90.164:9090,10.16.90.165:9091", "flightorders,*,afterSale,jdorders", "Y911").init(null);
//			messageQueueConsumerFactory = new KafkaMessageQueueConsumerFactory("10.17.149.11:9092,10.17.149.63:9092,10.17.149.64:9092", "flightorders,*,afterSale,jdorders", "Y911").init(null);
			messageQueueConsumerFactory = new KafkaMessageQueueConsumerFactory("127.0.0.1:1092", "flightorders,afterSale,jdorders,test", "XXXMMX111").init(null);
			//all
			messageQueueConsumerFactory.setMessageQueueConsumer(new MessageQueueConsumerHandler() {

				public void doProcess(String value, Object originalObject) {
					System.out.println("topic:jdorders : " + value);
				}

				public Object getQueueInfo() {
					return "flightorders";
				}
			});
			//all
			messageQueueConsumerFactory.setMessageQueueConsumer(new MessageQueueConsumerHandler() {

				public void doProcess(String value, Object originalObject) {
					System.out.println("topic:afterSale : " + value);
				}

				public Object getQueueInfo() {
					return "afterSale";
				}
			});
			messageQueueConsumerFactory.setMessageQueueConsumer(new MessageQueueConsumerHandler() {
				
				public void doProcess(String value, Object originalObject) {
					System.out.println("topic:jdorders : " + value);
				}
				
				public Object getQueueInfo() {
					return "jdorders";
				}
			});
			//topic_1
			messageQueueConsumerFactory.setMessageQueueConsumer(new MessageQueueConsumerHandler() {

				public void doProcess(String value, Object originalObject) {
					System.out.println("topic:T2_TOPIC ," + value);
				}

				public Object getQueueInfo() {
					return "T2_TOPIC";
				}
			});
			//topic_2
			messageQueueConsumerFactory.setMessageQueueConsumer(new MessageQueueConsumerHandler() {

				public void doProcess(String value, Object originalObject) {
					System.err.println("topic:jdorders ," + value);
				}

				public Object getQueueInfo() {
					return "topic_2";
				}
			});
			//topic_3
			messageQueueConsumerFactory.setMessageQueueConsumer(new MessageQueueConsumerHandler() {

				public void doProcess(String value, Object originalObject) {
					System.err.println("topic:* ," + value);
				}

				public Object getQueueInfo() {
					return "topic_3";
				}
			});
			messageQueueConsumerFactory.setMessageQueueConsumer(new MessageQueueConsumerHandler() {
				
				public void doProcess(String value, Object originalObject) {
					System.err.println("topic:* ," + value);
				}
				
				public Object getQueueInfo() {
					return "test";
				}
			});
			countDownLatch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void producer() {
		try {
			final CountDownLatch countDownLatch = new CountDownLatch(1);
			//			messageQueueProducerFactory = new KafkaMessageQueueProducerFactory("192.168.3.27:9092,10.17.38.12:9089,10.73.11.117:9091,10.73.11.117:9092").init(null);
			//			messageQueueProducerFactory = new KafkaMessageQueueProducerFactory("192.168.3.27:9092").init(null);
			messageQueueProducerFactory = new KafkaMessageQueueProducerFactory("127.0.0.1:9092").init(null);
			ExecutorService executorService = Executors.newCachedThreadPool();
			executorService.execute(new Runnable() {
				public void run() {
					int count = 10;
					while (--count > 0) {
						messageQueueProducerFactory.makeMessage("flightorders", String.format("{name:%s,dateTime:%s}", Thread.currentThread().getName(), new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis())), null);
						messageQueueProducerFactory.makeMessage("jdorders", String.format("{name:%s,dateTime:%s}", Thread.currentThread().getName(), new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis())), null);
						messageQueueProducerFactory.makeMessage("T2_TOPIC", String.format("{name:%s,dateTime:%s}", Thread.currentThread().getName(), new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis())), null);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					messageQueueProducerFactory.makeMessage("T1_TOPIC", String.format("{name:%s,dateTime:%s}", Thread.currentThread().getName(), new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis())), null);
					messageQueueProducerFactory.makeMessage("flightorders", String.format("{name:%s,dateTime:%s}", Thread.currentThread().getName(), new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis())), null);
					countDownLatch.countDown();
				}
			});
			countDownLatch.await();
			messageQueueProducerFactory.destory();
			System.out.println("结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
