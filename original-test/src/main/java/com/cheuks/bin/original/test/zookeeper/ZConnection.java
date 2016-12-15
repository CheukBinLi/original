package com.cheuks.bin.original.test.zookeeper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class ZConnection {
	// 192.168.168.150:2181,192.168.168.119:2181,192.168.168.124:2181,192.168.1.30:2181
	private final static String serverList = "127.0.0.1:2181";

	public static void main(String[] args) throws Exception {

		ExecutorService pool = Executors.newFixedThreadPool(2);

		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
		CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(serverList, retryPolicy);
		curatorFramework.start();
		System.out.println(curatorFramework.getState());
		if (null == curatorFramework.checkExists().forPath("/a")) {
			curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/a", "default".getBytes());
		}
		System.out.println(new String(curatorFramework.getData().forPath("/a")));
		// curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/aa",
		// "c".getBytes());

		final NodeCache nodeCache = new NodeCache(curatorFramework, "/a", false);
		nodeCache.start(true);
		nodeCache.getListenable().addListener(new NodeCacheListener() {

			public void nodeChanged() throws Exception {
				if (null == nodeCache.getCurrentData())
					return;
				System.out.println("Node data is changed, new data: " + new String(nodeCache.getCurrentData().getData()));
			}
		}, pool);

		curatorFramework.setData().forPath("/a", "bb".getBytes());
		Thread.sleep(5000);
		curatorFramework.setData().forPath("/a", "cc".getBytes());
		Thread.sleep(5000);
		curatorFramework.delete().forPath("/a");
		Thread.sleep(5000);
		if (null == curatorFramework.checkExists().forPath("/a")) {
			curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/a", "default".getBytes());
		}
	}

}
