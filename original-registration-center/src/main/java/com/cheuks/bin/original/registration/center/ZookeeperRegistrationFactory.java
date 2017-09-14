package com.cheuks.bin.original.registration.center;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
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
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;

import com.cheuks.bin.original.common.registrationcenter.ElectionCallBack;
import com.cheuks.bin.original.common.registrationcenter.RegistrationEventListener;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;

@SuppressWarnings("resource")
public class ZookeeperRegistrationFactory implements RegistrationFactory<CuratorFramework, PathChildrenCacheEvent, NodeCache> {

	private String serverList = "127.0.0.1:2181";// 127.0.0.1:2181,127.0.0.2:2181,192.168.3.12:2181

	private CuratorFramework curatorFramework;

	private RetryPolicy retryPolicy;

	private int baseSleepTimeMs = 5000;

	private int maxRetries = 20;

	private ExecutorService pool;

	private LeaderSelector leaderSelector;

	// private String LeaderDirectory = "/LeaderElection";

	private volatile boolean isLeader;
	private volatile boolean isInit;
	private Object leaderLock = new Object();

	public CuratorFramework getClient() {
		return curatorFramework;
	}

	public void setUrl(String url) {
		serverList = url;
	}

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

	public String register(String serviceDirectory, String value, final RegistrationEventListener<NodeCache> eventListener) throws Throwable {
		byte[] valueByte = (null == value ? new byte[0] : value.getBytes());
		if (null == serviceDirectory || !serviceDirectory.startsWith("/"))
			throw new Throwable("the serviceDirectory must be start with /  ,serviceDirectory:" + serviceDirectory);
		// if (null == curatorFramework.checkExists().forPath(serviceDirectory))
		// throw new Throwable("can't found " + serviceDirectory + " service.");

		if (null == curatorFramework.checkExists().forPath(serviceDirectory)) {
			curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(serviceDirectory, valueByte);
		} else {
			curatorFramework.setData().forPath(serviceDirectory, valueByte);
		}

		if (null != eventListener) {
			final NodeCache nodeCache = new NodeCache(curatorFramework, serviceDirectory, false);
			nodeCache.start(true);

			nodeCache.getListenable().addListener(new NodeCacheListener() {

				public void nodeChanged() throws Exception {
					eventListener.nodeChanged(nodeCache);
				}
			}, pool);
		}
		// 过期
		addReconnectionWatcher(serviceDirectory, new CuratorWatcher() {
			public void process(WatchedEvent event) throws Exception {
				eventListener.nodeChanged(null, event);
			}
		});

		return serviceDirectory;
	}

	public String setValue(String serviceDirectory, String value) throws Throwable {
		if (null == serviceDirectory || !serviceDirectory.startsWith("/"))
			throw new Throwable("the serviceDirectory must be start with /");
		Stat result = null;
		if (null != curatorFramework.checkExists().forPath(serviceDirectory)) {
			result = curatorFramework.setData().forPath(serviceDirectory, value.getBytes());
		}
		return null == result ? null : serviceDirectory;
	}

	public String getValue(String serviceDirectory) throws Throwable {
		if (null == serviceDirectory || !serviceDirectory.startsWith("/"))
			throw new Throwable("the serviceDirectory must be start with /");
		byte[] result;
		result = curatorFramework.getData().forPath(serviceDirectory);
		return null == result ? null : new String(result);
	}

	public List<String> getServiceList(String serviceDirectory) throws Throwable {
		if (null == serviceDirectory || !serviceDirectory.startsWith("/"))
			throw new Throwable("the serviceDirectory must be start with /");
		List<String> result;
		result = curatorFramework.getChildren().forPath(serviceDirectory);
		return null == result ? null : result;
	}

	public void removeServiceDirectory(String serviceDirectory) throws Throwable {
		if (null == serviceDirectory || !serviceDirectory.startsWith("/"))
			throw new Throwable("the serviceDirectory must be start with /");
		curatorFramework.delete().forPath(serviceDirectory);
	}

	// public boolean isRegister(String directory, String value) throws Throwable {
	// boolean result;
	// if (result = isRegister(directory)) {
	// curatorFramework.setData().forPath(directory, null == value ? new byte[0] : value.getBytes());
	// }
	// return result;
	// }

	public boolean isRegister(String directory) throws Throwable {
		return null != curatorFramework.checkExists().forPath(directory);
	}

	public void election(String ledderDirectory, final ElectionCallBack electionCallBack) throws Throwable {
		final LeaderSelectorListener leaderSelectorListener = new LeaderSelectorListenerAdapter() {

			public void takeLeadership(CuratorFramework client) throws Exception {
				isLeader = true;
				// System.out.println(Thread.currentThread().getName() + "做了leader");
				while (isLeader)
					synchronized (leaderLock) {
						electionCallBack.callBack(isLeader);
						leaderLock.wait();
						isLeader = false;
						// System.out.println(Thread.currentThread().getName() + "释放leader");
						// client.close();
					}
				// System.out.println(Thread.currentThread().getName() + "释放leader");
			}

			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				// System.out.println("重新加入选举");
				if ((newState == ConnectionState.SUSPENDED) || (newState == ConnectionState.LOST)) {
					isLeader = false;
					electionCallBack.callBack(isLeader);
					synchronized (leaderLock) {
						leaderLock.notify();
					}
				}
			}

		};

		leaderSelector = new LeaderSelector(curatorFramework, ledderDirectory, leaderSelectorListener);
		leaderSelector.autoRequeue();
		leaderSelector.start();
	}

	public enum ZookeeperWatcherType {

		GET_DATA, GET_CHILDREN, EXITS, CREATE_ON_NO_EXITS

	}

	private Set<String> watchers = new ConcurrentSkipListSet<String>();

	private void addReconnectionWatcher(final String path, final CuratorWatcher watcher) throws Throwable {
		String tempWatcher;
		if (!watchers.contains(tempWatcher = watcher.toString())) {
			watchers.add(tempWatcher);
			curatorFramework.getConnectionStateListenable().addListener(new ConnectionStateListener() {

				public void stateChanged(CuratorFramework client, ConnectionState newState) {
					if (newState == ConnectionState.LOST) {
						try {
							curatorFramework.getData().usingWatcher(watcher).forPath(path);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

	public void reelect() throws Throwable {
		synchronized (leaderLock) {
			if (isLeader) {
				isLeader = false;
				leaderLock.notify();
			}
		}
	}

	public void start() throws Throwable {
		if (isInit)
			return;
		isInit = true;
		pool = Executors.newCachedThreadPool();
		retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
		curatorFramework = CuratorFrameworkFactory.newClient(serverList, retryPolicy);
		curatorFramework.getConnectionStateListenable().addListener(new ConnectionStateListener() {

			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				if (newState == ConnectionState.LOST) {
					while (true) {
						try {
							if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
								// 操作
								break;
							}
						} catch (InterruptedException e) {
							break;
						} catch (Exception e) {
						}
					}
				}
			}
		});
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

	// public String getLeaderDirectory() {
	// return LeaderDirectory;
	// }
	//
	// public ZookeeperRegistrationFactory setLeaderDirectory(String leaderDirectory) {
	// LeaderDirectory = leaderDirectory;
	// return this;
	// }

	public boolean isLeader() {
		return isLeader;
	}

	public ZookeeperRegistrationFactory(String serverList, int baseSleepTimeMs, int maxRetries) {
		super();
		this.serverList = serverList;
		this.baseSleepTimeMs = baseSleepTimeMs;
		this.maxRetries = maxRetries;
	}

	public ZookeeperRegistrationFactory(String serverList) {
		super();
		this.serverList = serverList;
	}

	public ZookeeperRegistrationFactory() {
		super();
	}

	public static void main(String[] args) {
		ZookeeperRegistrationFactory zrf = new ZookeeperRegistrationFactory();
		zrf.serverList = "192.168.3.12:2181";
		try {
			zrf.start();
			String directory = zrf.createService("/service", new RegistrationEventListener<PathChildrenCacheEvent>() {

				public void nodeChanged(PathChildrenCacheEvent params, Object... obj) throws Exception {
					System.err.println("createService:" + new String(params.getData().getData()));
				}
			});

			zrf.register(directory + "/mmx_1", "朋友小红是我的！", new RegistrationEventListener<NodeCache>() {

				public void nodeChanged(NodeCache params, Object... obj) throws Exception {
					System.err.println("register:" + new String(params.getCurrentData().getData()));
				}
			});
			zrf.register(directory + "/mmx_4", "我是mmx_4", null);
			// zrf.register(directory + "/mmx_4/temp_node", "我是mmx_4的临时节点字节点", null);

			zrf.setValue(directory + "/mmx_1", "441");

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

			zrf.election("/LeaderElection", electionCallBack);

			zrf.reelect();

			CountDownLatch cdl = new CountDownLatch(1);
			cdl.await();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
