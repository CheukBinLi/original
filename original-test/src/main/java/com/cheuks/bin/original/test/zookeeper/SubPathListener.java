package com.cheuks.bin.original.test.zookeeper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class SubPathListener {

	private final static String serverList = "127.0.0.1:2181";

	public static void main(String[] args) throws Exception {

		ExecutorService executor = Executors.newFixedThreadPool(2);

		CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(serverList, new ExponentialBackoffRetry(1000, 10));
		curatorFramework.start();

		if (null == curatorFramework.checkExists().forPath("/config")) {
			curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/config", null);
		}

		final PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, "/config", true);
		pathChildrenCache.start(StartMode.POST_INITIALIZED_EVENT);
		pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {

			public void childEvent(CuratorFramework arg0, PathChildrenCacheEvent arg1) throws Exception {
				switch (arg1.getType()) {
				case CHILD_ADDED:
					System.out.println(arg1.getType() + " : " + arg1.getData().getPath());
					break;
				case CHILD_UPDATED:
					System.out.println(arg1.getType() + " : " + arg1.getData().getPath());
					break;
				case CHILD_REMOVED:
					System.out.println(arg1.getType() + " : " + arg1.getData().getPath());
					break;

				default:
					break;
				}
			}
		}, executor);
		Thread.sleep(5000);
		if (null == curatorFramework.checkExists().forPath("/config/aa"))
			curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/config/aa", "aa".getBytes());
		Thread.sleep(5000);
		System.out.println(curatorFramework.getData().forPath("/config/aa"));
		curatorFramework.setData().forPath("/config/aa", "bb".getBytes());
		Thread.sleep(5000);
		curatorFramework.delete().forPath("/config/aa");
		Thread.sleep(5000);
		if (null != curatorFramework.checkExists().forPath("/config/bb"))
			curatorFramework.delete().forPath("/config/bb");
		curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/config/bb", "bb".getBytes());
		Thread.sleep(5000);
	}
}
