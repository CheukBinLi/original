package com.cheuks.bin.original.reflect.rmi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cheuks.bin.original.reflect.test2I;

public class NettyClientAdapter extends NettyClient {

	private NettyClientAdapter() {
		super();
	}

	private NettyClientAdapter(int poolSize) {
		super(poolSize);
	}

	private static NettyClient INSTANCE;

	public static final NettyClient newInstance(int poolSize) {
		if (null == INSTANCE) {
			synchronized (NettyClientAdapter.class) {
				if (null == INSTANCE) {
					INSTANCE = new NettyClient(poolSize);
				}
			}
		}
		return INSTANCE;
	}

	public static final NettyClient newInstance() {
		return newInstance(-1);
	}

	public static void main(String[] args) throws Throwable {

		// 通信池
		NettyClient ncop = NettyClientAdapter.newInstance(15);
		ncop.setAddress("localhost:10087").setMaxFrameLength(5000);
		ncop.start();
		System.out.println("线程数：" + ncop.getNumIdle());

		// 处理
		RmiBeanFactory rmiBeanFactory = new SimpleRmiBeanFactory();
		Map<String, Object> args1 = new HashMap<String, Object>();
		args1.put("scan", "com.cheuks.bin.original.reflect");
		rmiBeanFactory.init(args1);
		final test2I t = rmiBeanFactory.getBean(test2I.class);

		long now = System.currentTimeMillis();
		ExecutorService executorService = Executors.newCachedThreadPool();
		final CountDownLatch countDownLatch = new CountDownLatch(1000000);
		for (int i = 0; i < 10; i++)
			executorService.submit(new Runnable() {
				public void run() {
					for (int i = 0; i < 100000; i++) {
						t.a4("{'uid':1,'name':'test','age':25,'desc':'abcdssssssssssssssssssdddddddddddddfffffff'}");
						countDownLatch.countDown();
					}
				}
			});
		countDownLatch.await();
		System.err.println(System.currentTimeMillis() - now);
		ncop.shutdown();
	}

}
