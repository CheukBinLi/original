package com.cheuks.bin.original.test.zookeeper;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class LeaderElectionTest {

	private Object lock = new Object();

	private LeaderLatch leaderLatch;

	private LeaderSelectorListener leaderSelectorListener;

	private volatile boolean isLeader;

	private String name;

	private String leaderPath = "/config";

	private CuratorFramework client;

	private RetryPolicy retryPolicy;

	private String serverList = "127.0.0.1:2181";

	private Executor executor;

	private void init() {

		LeaderLatchListener listener = new LeaderLatchListener() {

			public void notLeader() {
				System.out.println(name + ":不是Leader");
			}

			public void isLeader() {
				System.out.println(name + ":是Leader");
			}
		};

		executor = Executors.newCachedThreadPool();
		retryPolicy = new ExponentialBackoffRetry(1000, 10);
		client = CuratorFrameworkFactory.newClient(serverList, retryPolicy);
		client.start();
		leaderLatch = new LeaderLatch(client, leaderPath + "_election");
		leaderLatch.addListener(listener, executor);
	}

	public void selectorLeader() throws Exception {
		synchronized (this) {
			if (null == leaderLatch)
				init();
		}
		if (null == client.checkExists().forPath(leaderPath + "_election"))
			client.create().withMode(CreateMode.PERSISTENT).forPath(leaderPath + "_election");
		// leaderLatch.start();
		// // leaderLatch.await();
		// System.out.println(leaderLatch.getLeader());
	}

	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.submit(new Runnable() {

			public void run() {
				try {
					LeaderElectionTest let = new LeaderElectionTest();
					let.name = "A1";
					let.selectorLeader();
					let.leaderLatch.start();
					System.out.println("A1开始竞争");
					let.leaderLatch.await();
					System.err.println(let.leaderLatch.hasLeadership());
					System.out.println("A1完成");
					Thread.sleep(Integer.MAX_VALUE);
					let.leaderLatch.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		executor.submit(new Runnable() {

			public void run() {
				try {
					LeaderElectionTest let = new LeaderElectionTest();
					let.name = "A2";
					let.selectorLeader();
					let.leaderLatch.start();
					System.out.println("A2开始竞争");
					let.leaderLatch.await();
					System.err.println(let.leaderLatch.hasLeadership());
					System.out.println("A2完成");
					Thread.sleep(10000);
					let.leaderLatch.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
