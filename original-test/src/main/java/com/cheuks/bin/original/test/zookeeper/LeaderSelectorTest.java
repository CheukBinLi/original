package com.cheuks.bin.original.test.zookeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class LeaderSelectorTest {

	private Object lock = new Object();

	private LeaderSelector leaderSelector;

	private LeaderSelectorListener leaderSelectorListener;

	private volatile boolean isLeader;

	private String leaderPath = "/config";

	private CuratorFramework client;

	private RetryPolicy retryPolicy;

	private String serverList = "127.0.0.1:2181";

	private final static CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(10);

	private void init() {
		leaderSelectorListener = new LeaderSelectorListenerAdapter() {

			public void takeLeadership(CuratorFramework client) throws Exception {
				isLeader = true;
				System.out.println(Thread.currentThread().getName() + "做了leader");
				while (isLeader)
					synchronized (lock) {
						lock.wait();
					}
				System.out.println(Thread.currentThread().getName() + "释放leader");
			}

			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				System.out.println("......");
				// super.stateChanged(client, newState);
				if ((newState == ConnectionState.SUSPENDED) || (newState == ConnectionState.LOST)) {
					isLeader = false;
					synchronized (lock) {
						lock.notifyAll();
					}
				}
			}
		};
		retryPolicy = new ExponentialBackoffRetry(1000, 10);
		client = CuratorFrameworkFactory.newClient(serverList, retryPolicy);
		client.start();
		leaderSelector = new LeaderSelector(client, leaderPath + "_election", leaderSelectorListener);
		leaderSelector.autoRequeue();
	}

	public void selectorLeader() throws Exception {
		synchronized (this) {
			if (null == leaderSelector)
				init();
		}
		if (null == client.checkExists().forPath(leaderPath + "_election"))
			client.create().withMode(CreateMode.EPHEMERAL).forPath(leaderPath + "_election");
		COUNT_DOWN_LATCH.countDown();
		COUNT_DOWN_LATCH.await();
		leaderSelector.start();
		System.out.println(Thread.currentThread().getName() + leaderSelector.getLeader());
	}

	public static void main(String[] args) throws Exception {
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0, len = (int) COUNT_DOWN_LATCH.getCount(); i < len; i++)
			executorService.submit(new Runnable() {

				public void run() {
					try {
						new LeaderSelectorTest().selectorLeader();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		Thread.sleep(Integer.MAX_VALUE);
	}

}
