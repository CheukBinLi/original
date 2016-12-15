package com.cheuks.bin.original.test.zookeeper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class QueueTest {
	private final static String serverList = "127.0.0.1:2181";

	public static void main(String[] args) throws Exception {

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.submit(new Runnable() {

			public void run() {
				try {
					CuratorFramework curator = CuratorFrameworkFactory.newClient(serverList, new ExponentialBackoffRetry(2000, 10));
					curator.start();

					DistributedQueue<String> queue = null;
					QueueSerializer<String> serizlizer = new QueueSerializer<String>() {

						public byte[] serialize(String item) {
							return item.getBytes();
						}

						public String deserialize(byte[] bytes) {
							return new String(bytes);
						}
					};
					QueueConsumer<String> consumer = new QueueConsumer<String>() {

						public void stateChanged(CuratorFramework client, ConnectionState newState) {
							System.out.println("new state: " + newState);
						}

						public void consumeMessage(String message) throws Exception {
							// 线程等待5秒，模拟数据处理，以达到数据抢夺的目的
							Thread.sleep(5000);
							// 打印出是哪个线程处理了哪些数据
							System.out.println(Thread.currentThread().getId() + " consume: " + message);
						}
					};

					queue = QueueBuilder.builder(curator, consumer, serizlizer, "/queue").buildQueue();
					queue.start();

					// 没用途
					curator.create().forPath("/queue/170", "170".getBytes());

					queue.put("110");
					queue.put("120");
					queue.put("130");
					queue.put("140");
					queue.put("150");
					queue.put("160");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}
}
