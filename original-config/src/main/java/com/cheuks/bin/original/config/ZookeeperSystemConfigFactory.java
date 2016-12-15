package com.cheuks.bin.original.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperSystemConfigFactory extends AbstractSystemConfigFactory {

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {

			}
		}));
	}

	private String serverList = "127.0.0.1:2181";
	private CuratorFramework curatorFramework;
	private int maxRetries = 10;
	private int retriesInterval = 5000;
	private RetryPolicy retryPolicy;
	private InterProcessReadWriteLock lock;
	private InterProcessMutex readLock;
	private InterProcessMutex writeLock;
	private String configPath = "/config";

	protected Map<String, Object> config = new ConcurrentHashMap<String, Object>();

	public void setConfig(String configName, String value) throws Throwable {

	}

	@Override
	public Map<String, Object> getConfig() {
		return config;
	}

	private void init() throws Exception {
		retryPolicy = new ExponentialBackoffRetry(getRetriesInterval(), getMaxRetries());
		curatorFramework = CuratorFrameworkFactory.newClient(getServerList(), retryPolicy);
		curatorFramework.start();

		lock = new InterProcessReadWriteLock(curatorFramework, configPath + "lock");

		readLock = lock.readLock();
		writeLock = lock.writeLock();
		List<String> paths = curatorFramework.getChildren().forPath(configPath);
		PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, configPath, true);
		// pathChildrenCache.start();
		List<ChildData> list = pathChildrenCache.getCurrentData();
		System.out.println(list);
		System.out.println(paths);
	}

	public String getServerList() {
		return serverList;
	}

	public ZookeeperSystemConfigFactory setServerList(String serverList) {
		this.serverList = serverList;
		return this;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public ZookeeperSystemConfigFactory setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
		return this;
	}

	public int getRetriesInterval() {
		return retriesInterval;
	}

	public ZookeeperSystemConfigFactory setRetriesInterval(int retriesInterval) {
		this.retriesInterval = retriesInterval;
		return this;
	}

	public String getConfigPath() {
		return configPath;
	}

	public ZookeeperSystemConfigFactory setConfigPath(String configPath) {
		this.configPath = configPath;
		return this;
	}

	public static void main(String[] args) throws Exception {
		new ZookeeperSystemConfigFactory().init();
	}

}
