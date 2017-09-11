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

	@Test
	public void producerToTest() {
		try {
			String message="{\"basicInfo\":{\"orderNo\":\"HHT20170822000350\",\"vendorOrderNo\":\"H0117082116253767038\",\"hotelCode\":\"110002179\",\"hotelName\":\"镜像第六酒店\",\"roomCode\":null,\"roomTypeName\":\"双人房\",\"checkInDate\":\"2017-08-27 00:00:00\",\"checkOutDate\":\"2017-08-31 00:00:00\",\"payType\":null,\"roomNum\":3,\"passengerNum\":3,\"firstArriveTime\":null,\"lastArriveTime\":\"20171116180000\",\"currency\":null,\"totalAmount\":600.00,\"bookType\":1,\"vendorNumber\":500,\"vendorName\":\"华为慧通\",\"createTime\":\"2017-08-21 16:25:37\",\"lastUpdateTime\":null,\"mipAccount\":\"11333014\",\"unitPrice\":null,\"orderStatus\":3,\"supplierCode\":\"500\",\"ratePlanId\":null,\"ecNo\":null,\"cityCnName\":\"南京\",\"cityEnName\":null,\"cityCode\":null,\"payTypeCallback\":null,\"roomNumCallback\":null,\"nightNumCallback\":null,\"roomTypeCallback\":null,\"checkInDateCallback\":null,\"checkOutDateCallback\":null,\"cityId\":null,\"employeeNumber\":\"11333014\",\"tenantId\":143616744097841152,\"sendToAccount\":0,\"checkStatus\":1,\"createdName\":null,\"contactInfoList\":[{\"contactId\":\"29bc4c45faed43669c6d26ed5fff7189\",\"orderNo\":\"HHT20170822000350\",\"orderType\":2,\"contactName\":\"肖楚意\",\"mipAccount\":null,\"mobile\":\"13690520525\",\"phone\":null,\"email\":\"chuyi.xiao@meicloud.com\"}],\"customerList\":[{\"id\":\"1618a081269644f48357605139f15786\",\"passengerId\":null,\"orderNo\":\"HHT20170822000350\",\"mipAccount\":null,\"cnName\":\"肖楚意\",\"enName\":null,\"certType\":null,\"certNumber\":null,\"gender\":null,\"mobile\":null,\"email\":null,\"deptCode\":null,\"deptName\":null,\"passengerType\":null},{\"id\":\"63948827ffb94659a3351c00387bf5d4\",\"passengerId\":null,\"orderNo\":\"HHT20170822000350\",\"mipAccount\":null,\"cnName\":\"测试5\",\"enName\":null,\"certType\":null,\"certNumber\":null,\"gender\":null,\"mobile\":null,\"email\":null,\"deptCode\":null,\"deptName\":null,\"passengerType\":null},{\"id\":\"d4c194b68fd3482ea5aaae2e65209a48\",\"passengerId\":null,\"orderNo\":\"HHT20170822000350\",\"mipAccount\":null,\"cnName\":\"测试4\",\"enName\":null,\"certType\":null,\"certNumber\":null,\"gender\":null,\"mobile\":null,\"email\":null,\"deptCode\":null,\"deptName\":null,\"passengerType\":null}],\"hotelRoomList\":[{\"id\":\"81c174f22c0545d5a7c45822b255ca65\",\"roomCode\":\"0\",\"roomName\":null,\"fooler\":null,\"acre\":null,\"bedType\":null,\"intent\":null,\"status\":null,\"extra\":null,\"facilities\":null,\"roomDescr\":null,\"remark\":null,\"imgUrls\":null,\"hotelCode\":null,\"orderNo\":\"HHT20170822000350\",\"customerName\":\"肖楚意\",\"hotelDailyPriceList\":[{\"id\":\"2de2294128454a529708e31948f3a072\",\"hotelPriceId\":null,\"roomCode\":\"0\",\"priceRebate\":null,\"sellDate\":\"2017-11-17 00:00:00\",\"price\":100,\"orderNo\":\"HHT20170822000350\",\"roomPriceCallback\":100.00,\"assureAmountCallback\":null,\"serviceAmountCallback\":null,\"totalCallback\":null,\"checkStatus\":0},{\"id\":\"c0b26aab91cd4cedba9481af543f8875\",\"hotelPriceId\":null,\"roomCode\":\"0\",\"priceRebate\":null,\"sellDate\":\"2017-11-16 00:00:00\",\"price\":100,\"orderNo\":\"HHT20170822000350\",\"roomPriceCallback\":100.00,\"assureAmountCallback\":null,\"serviceAmountCallback\":null,\"totalCallback\":null,\"checkStatus\":1}]},{\"id\":\"cf593273f4e3403099124860bd2cf1da\",\"roomCode\":\"1\",\"roomName\":null,\"fooler\":null,\"acre\":null,\"bedType\":null,\"intent\":null,\"status\":null,\"extra\":null,\"facilities\":null,\"roomDescr\":null,\"remark\":null,\"imgUrls\":null,\"hotelCode\":null,\"orderNo\":\"HHT20170822000350\",\"customerName\":\"测试4\",\"hotelDailyPriceList\":[{\"id\":\"0d4dc39db4344f589f2a26c9828c9c8d\",\"hotelPriceId\":null,\"roomCode\":\"1\",\"priceRebate\":null,\"sellDate\":\"2017-11-16 00:00:00\",\"price\":100,\"orderNo\":\"HHT20170822000350\",\"roomPriceCallback\":100.00,\"assureAmountCallback\":null,\"serviceAmountCallback\":null,\"totalCallback\":null,\"checkStatus\":0},{\"id\":\"ae27e68da8d94ecc89c52d03bed1affe\",\"hotelPriceId\":null,\"roomCode\":\"1\",\"priceRebate\":null,\"sellDate\":\"2017-11-17 00:00:00\",\"price\":100,\"orderNo\":\"HHT20170822000350\",\"roomPriceCallback\":100.00,\"assureAmountCallback\":null,\"serviceAmountCallback\":null,\"totalCallback\":null,\"checkStatus\":0}]},{\"id\":\"f7dd4e96b4cd4334af37b866d1c144e3\",\"roomCode\":\"2\",\"roomName\":null,\"fooler\":null,\"acre\":null,\"bedType\":null,\"intent\":null,\"status\":null,\"extra\":null,\"facilities\":null,\"roomDescr\":null,\"remark\":null,\"imgUrls\":null,\"hotelCode\":null,\"orderNo\":\"HHT20170822000350\",\"customerName\":\"测试5\",\"hotelDailyPriceList\":[{\"id\":\"537bcbc7a65f41a18421999ea0dfff98\",\"hotelPriceId\":null,\"roomCode\":\"2\",\"priceRebate\":null,\"sellDate\":\"2017-11-17 00:00:00\",\"price\":100,\"orderNo\":\"HHT20170822000350\",\"roomPriceCallback\":100.00,\"assureAmountCallback\":null,\"serviceAmountCallback\":null,\"totalCallback\":null,\"checkStatus\":1},{\"id\":\"5bee6dfe64744ead96220b23178ac8d1\",\"hotelPriceId\":null,\"roomCode\":\"2\",\"priceRebate\":null,\"sellDate\":\"2017-11-16 00:00:00\",\"price\":100,\"orderNo\":\"HHT20170822000350\",\"roomPriceCallback\":100.00,\"assureAmountCallback\":null,\"serviceAmountCallback\":null,\"totalCallback\":null,\"checkStatus\":1}]}],\"paymentRecordList\":[{\"paymentNo\":\"P20170901000012\",\"orderNo\":\"HHT20170822000350\",\"orderType\":2,\"runningNo\":\"52a5802d-5a52-41bd-baad-cf988ed522bd\",\"amount\":600,\"payType\":1,\"result\":1},{\"paymentNo\":\"P20170904000013\",\"orderNo\":\"HHT20170822000350\",\"orderType\":2,\"runningNo\":\"a8e50145-8aae-4089-9576-59b1bad06b19\",\"amount\":600,\"payType\":1,\"result\":0}]}}";
			messageQueueProducerFactory = new KafkaMessageQueueProducerFactory("10.17.38.13:9089").init(null);
			messageQueueProducerFactory.makeMessage("hotelorders", message, null);
			messageQueueProducerFactory.destory();
			System.out.println("结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
