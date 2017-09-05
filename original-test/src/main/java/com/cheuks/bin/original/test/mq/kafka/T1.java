package com.cheuks.bin.original.test.mq.kafka;

import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.cheuks.bin.original.test.mq.KafkaMessageQueueConsumerFactory;
import com.cheuks.bin.original.test.mq.KafkaMessageQueueProducerFactory;
import com.cheuks.bin.original.test.mq.MessageQueueConsumerFactory;
import com.cheuks.bin.original.test.mq.MessageQueueConsumerHandler;
import com.cheuks.bin.original.test.mq.MessageQueueProducerFactory;

public class T1 {

	private MessageQueueConsumerFactory messageQueueConsumerFactory;

	private MessageQueueProducerFactory messageQueueProducerFactory;

	@Test
	public void consumer() {
		try {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			messageQueueConsumerFactory = new KafkaMessageQueueConsumerFactory("10.73.11.117:9091,10.73.11.117:9092", "T1_TOPIC,T2_TOPIC", "CCTV_1").init(null);
			messageQueueConsumerFactory.RegisterHandler(new MessageQueueConsumerHandler() {

				public String getQueueName() {
					return "T2_TOPIC";
				}

				public void doProcess(String value, Object originalObject) {
					System.out.println(value);
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
			messageQueueProducerFactory = new KafkaMessageQueueProducerFactory("10.17.38.12:9089,10.73.11.117:9091,10.73.11.117:9092").init(null);
			ExecutorService executorService = Executors.newCachedThreadPool();
			executorService.execute(new Runnable() {
				public void run() {
					int count = 10;
					while (--count > 0) {
						messageQueueProducerFactory.makeMessage("T2_TOPIC", String.format("{name:%s,dateTime:%s}", Thread.currentThread().getName(), new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis())), null);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
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
