package com.cheuks.bin.original.test.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DistributedLock {

	private final static String serverList = "127.0.0.1:2181";
	private static volatile boolean isRun = false;

	// 互斥锁
	public static class InterPorcessMutexExample {

		private final InterProcessMutex lock;
		private final Runnable action;
		private final String name;

		public InterPorcessMutexExample(final CuratorFramework client, String path, Runnable action, String name) {
			super();
			this.lock = new InterProcessMutex(client, path);
			this.action = action;
			this.name = name;
		}

		public void doWork(long time) throws Exception {
			while (true) {
				if (!isRun) {
					Thread.sleep(100);
					continue;
				}
				if (lock.acquire(time, TimeUnit.SECONDS)) {
					try {
						System.out.println(name + ":获得互斥锁");
						Thread.sleep(5000);
						action.run();
						return;
					} finally {
						lock.release();
						System.out.println(name + ":释放互斥锁");
					}
				}
				System.out.println(name + ":不能获得互斥锁");
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(200);
		final List<CuratorFramework> curatorFrameworks = new ArrayList<CuratorFramework>();
		CuratorFramework curatorFramework;
		final CountDownLatch countDownLatch = new CountDownLatch(50);
		for (int i = 0; i < 50; i++) {
			curatorFramework = CuratorFrameworkFactory.newClient(serverList, new ExponentialBackoffRetry(10000, 10));
			curatorFramework.start();
			curatorFrameworks.add(curatorFramework);
		}

		for (int i = 0, len = curatorFrameworks.size() / 10; i < len; i++) {
			final int index = i;
			executor.submit(new Runnable() {
				public void run() {
					for (int j = 0, len = 10; j <= len; j++) {
						final int index2 = index * 10 + j;
						System.out.println(index2);
						countDownLatch.countDown();
						executor.submit(new Runnable() {
							public void run() {
								try {
									final InterPorcessMutexExample example = new InterPorcessMutexExample(curatorFrameworks.get(index2), "/mutex", new Runnable() {

										public void run() {
											System.out.println("运行doWork");
										}
									}, index2 + "");
									System.out.println(countDownLatch.getCount());
									executor.submit(new Runnable() {

										public void run() {
											try {
												example.doWork(100);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									});
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			});
		}
		countDownLatch.await();
		Thread.sleep(10000);
		isRun = true;
	}

}
