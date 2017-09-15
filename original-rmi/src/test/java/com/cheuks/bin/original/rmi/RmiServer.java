package com.cheuks.bin.original.rmi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.rmi.t.test2I;

@Component
public class RmiServer {

	@Autowired
	private test2I test2i;

	public static void main(String[] args) throws InterruptedException {

		// PropertyConfigurator.configure("log4j.properties");
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("rmi/application-config.xml");
		applicationContext.start();

		// SimpleRmiService simpleRmiService = applicationContext.getBean(SimpleRmiService.class);
		//
		// SimpleRmiBeanFactory factory = applicationContext.getBean(SimpleRmiBeanFactory.class);
		//
		// Thread.sleep(5000);
		// // test2I t = (test2I) applicationContext.getBean("CCTV2");
		// // System.err.println(t.a3("xxx", 10));
		//

		final RmiServer rmiServer = applicationContext.getBean(RmiServer.class);

		// System.err.println(rmiServer.test2i.a4("xxxxxxxxxxxxx"));
		// NettyRmiInvokeClientImpl r=applicationContext.getBean(NettyRmiInvokeClientImpl.class);
		// System.err.println(r);

		// Thread.sleep(10000);

		// System.err.println(rmiServer.test2i.a4("开始"));
		System.err.println("准备");
		for (int i = 0; i < 3; i++) {
			System.err.println(i);
			Thread.sleep(1000);
		}
		System.err.println("开始");
		final ExecutorService e = Executors.newCachedThreadPool();

		int count = 100000;
		int weight = 8;
		if (null != args && args.length > 0) {
			weight = Integer.valueOf(args[0]);
			count = Integer.valueOf(args[1]);
		}
		final CountDownLatch countDownLatch = new CountDownLatch(weight);
		Long now = System.currentTimeMillis();
		for (int i = 0; i < weight; i++) {
			rmiServer.runX(e, countDownLatch, count, rmiServer);
		}
		countDownLatch.await();
		System.out.println((System.currentTimeMillis() - now) + "ms");
	}

	private void runX(ExecutorService e, final CountDownLatch countDownLatch, final int count, final RmiServer rmiServer) {
		e.execute(new Runnable() {
			public void run() {
				for (int i = 0; i < count; i++) {
					if (null == rmiServer.test2i.a4(
							"叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............叼拿星啦！..............")) {
						System.err.println("null");
					}
				}
				countDownLatch.countDown();
			}
		});
	}

}
