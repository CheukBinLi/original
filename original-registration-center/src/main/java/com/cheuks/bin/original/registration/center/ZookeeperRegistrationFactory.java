package com.cheuks.bin.original.registration.center;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.cheuks.bin.original.common.registrationcenter.ElectionCallBack;
import com.cheuks.bin.original.common.registrationcenter.RegistrationEventListener;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;

@SuppressWarnings("resource")
public class ZookeeperRegistrationFactory implements RegistrationFactory<PathChildrenCacheEvent, NodeCache> {

	private String serverList = "127.0.0.2:2181,127.0.0.1:2181,";

	private CuratorFramework curatorFramework;

	private RetryPolicy retryPolicy;

	private int baseSleepTimeMs = 5000;

	private int maxRetries = 20;

	private ExecutorService pool;

	private LeaderSelector leaderSelector;

	private String LeaderDirectory = "/LeaderElection";

	private volatile boolean isLeader;
	private Object leaderLock = new Object();

	public String createService(String directory, final RegistrationEventListener<PathChildrenCacheEvent> eventListener) throws Throwable {
		if (null == directory || !directory.startsWith("/"))
			throw new Throwable("the directory must be start with /");
		if (null == curatorFramework.checkExists().forPath(directory)) {
			curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(directory);
		}
		if (null != eventListener) {
			final PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, directory, true);
			pathChildrenCache.start(StartMode.POST_INITIALIZED_EVENT);
			pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {

				public void childEvent(CuratorFramework arg0, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
					eventListener.nodeChanged(pathChildrenCacheEvent);
				}
			}, pool);
		}
		return directory;
	}

	public String register(String serviceDirectory, String key, String value, final RegistrationEventListener<NodeCache> eventListener) throws Throwable {
		if (null == serviceDirectory || !serviceDirectory.startsWith("/") || null == key || !key.startsWith("/"))
			throw new Throwable("the serviceDirectory must be start with /");
		if (null == curatorFramework.checkExists().forPath(serviceDirectory))
			throw new Throwable("can't found " + serviceDirectory + " service.");

		if (null == curatorFramework.checkExists().forPath(serviceDirectory + key)) {
			curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(serviceDirectory + key, value.getBytes());
		}

		if (null != eventListener) {
			final NodeCache nodeCache = new NodeCache(curatorFramework, serviceDirectory + key, false);
			nodeCache.start(true);

			nodeCache.getListenable().addListener(new NodeCacheListener() {

				public void nodeChanged() throws Exception {
					eventListener.nodeChanged(nodeCache);
				}
			}, pool);
		}

		return serviceDirectory;
	}

	public String setValue(String serviceDirectory, String key, String value) throws Throwable {
		if (null == serviceDirectory || !serviceDirectory.startsWith("/") || null == key || !key.startsWith("/"))
			throw new Throwable("the serviceDirectory must be start with /");
		Stat result = null;
		if (null != curatorFramework.checkExists().forPath(serviceDirectory + key)) {
			result = curatorFramework.setData().forPath(serviceDirectory + key, value.getBytes());
		}
		return null == result ? null : serviceDirectory;
	}

	public boolean isRegister(String directory, String value) throws Throwable {
		return false;
	}

	public void election(final ElectionCallBack electionCallBack) throws Throwable {
		final LeaderSelectorListener leaderSelectorListener = new LeaderSelectorListenerAdapter() {

			public void takeLeadership(CuratorFramework client) throws Exception {
				isLeader = true;
				// System.out.println(Thread.currentThread().getName() + "做了leader");
				while (isLeader)
					synchronized (leaderLock) {
						electionCallBack.callBack(isLeader);
						leaderLock.wait();
						isLeader = false;
						// client.close();
					}
				// System.out.println(Thread.currentThread().getName() + "释放leader");
			}

			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				// System.out.println("重新加入选举");
				if ((newState == ConnectionState.SUSPENDED) || (newState == ConnectionState.LOST)) {
					isLeader = false;
					synchronized (leaderLock) {
						leaderLock.notify();
					}
				}
			}
		};

		leaderSelector = new LeaderSelector(curatorFramework, getLeaderDirectory(), leaderSelectorListener);
		leaderSelector.autoRequeue();
		leaderSelector.start();
	}

	public void reelect() throws Throwable {
		synchronized (leaderLock) {
			if (isLeader) {
				isLeader = false;
				leaderLock.notify();
			}
		}
	}

	public void init() throws Throwable {
		pool = Executors.newCachedThreadPool();
		retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
		curatorFramework = CuratorFrameworkFactory.newClient(serverList, retryPolicy);
		curatorFramework.start();
	}

	public String getServerList() {
		return serverList;
	}

	public ZookeeperRegistrationFactory setServerList(String serverList) {
		this.serverList = serverList;
		return this;
	}

	public int getBaseSleepTimeMs() {
		return baseSleepTimeMs;
	}

	public ZookeeperRegistrationFactory setBaseSleepTimeMs(int baseSleepTimeMs) {
		this.baseSleepTimeMs = baseSleepTimeMs;
		return this;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public ZookeeperRegistrationFactory setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
		return this;
	}

	public String getLeaderDirectory() {
		return LeaderDirectory;
	}

	public ZookeeperRegistrationFactory setLeaderDirectory(String leaderDirectory) {
		LeaderDirectory = leaderDirectory;
		return this;
	}

	public boolean isLeader() {
		return isLeader;
	}

	public ZookeeperRegistrationFactory(String serverList, int baseSleepTimeMs, int maxRetries) {
		super();
		this.serverList = serverList;
		this.baseSleepTimeMs = baseSleepTimeMs;
		this.maxRetries = maxRetries;
	}

	public ZookeeperRegistrationFactory() {
		super();
	}

	public static void main(String[] args) {
		ZookeeperRegistrationFactory zrf = new ZookeeperRegistrationFactory();
		try {
			zrf.init();
			String directory = zrf.createService("/service", new RegistrationEventListener<PathChildrenCacheEvent>() {

				public void nodeChanged(PathChildrenCacheEvent params) throws Exception {
					System.err.println("createService:" + new String(params.getData().getData()));
				}
			});

			zrf.register(directory, "/mmx_1", "朋友小红是我的！", new RegistrationEventListener<NodeCache>() {

				public void nodeChanged(NodeCache params) throws Exception {
					System.err.println("register:" + new String(params.getCurrentData().getData()));
				}
			});
			zrf.register(directory, "/mmx_4", "我是mmx_4", null);

			zrf.setValue(directory, "/mmx_1", "441");

			ElectionCallBack electionCallBack = new ElectionCallBack() {

				public void callBack(boolean isLeader) {
					while (isLeader) {
						try {
							Thread.sleep(5000);
							System.out.println(Thread.currentThread().getName());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			};

			zrf.election(electionCallBack);

			zrf.reelect();

			CountDownLatch cdl = new CountDownLatch(1);
			cdl.await();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
