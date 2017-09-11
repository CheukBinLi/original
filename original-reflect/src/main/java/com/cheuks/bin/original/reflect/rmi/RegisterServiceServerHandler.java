package com.cheuks.bin.original.reflect.rmi;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.registrationcenter.ElectionCallBack;
import com.cheuks.bin.original.common.registrationcenter.RegistrationEventListener;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;

/***
 * 
 * @author BenPack
 *
 */
public class RegisterServiceServerHandler implements RegisterService {

	private static final Logger LOG = LoggerFactory.getLogger(RegisterServiceServerHandler.class);

	private volatile boolean isStart;

	private volatile boolean isRegister;
	private volatile boolean isLedder;
	private int eventHandleDelay = 5000;

	private String applicationName = "temp_application";
	private String applicationUrl = "127.0.0.1:10086";

	private final Map<String, ServerLoad> serverList = new ConcurrentHashMap<String, ServerLoad>();
	private final LinkedList<ServerLoad> serverLoadList = new LinkedList<ServerLoad>();

	private RegistrationFactory<CuratorFramework, PathChildrenCacheEvent, NodeCache> registrationFactory;

	public RegisterServiceServerHandler(String applicationName, String applicationUrl, final RegistrationFactory<CuratorFramework, PathChildrenCacheEvent, NodeCache> registrationFactory) {
		super();
		this.applicationName = applicationName;
		this.applicationUrl = applicationUrl;
		this.registrationFactory = registrationFactory;
	}

	private void dstributionService(String path) {
		try {
			String serverName = registrationFactory.getValue(path);
			int count = Integer.valueOf(registrationFactory.getValue(serverName));
			registrationFactory.setValue(serverName, Integer.toString(++count));
		} catch (Throwable e) {
			LOG.error(null, e);
		}
		//		if (serverList.size() > 0) {
		//			Collections.reverse(serverLoadList);
		//			ServerLoad tempServerLoad = serverLoadList.removeLast();
		//			try {
		//				// 负载加1
		//				registrationFactory.setValue(path, tempServerLoad.getServerName() + SEPARATOR + tempServerLoad.getServerUrl());
		//				tempServerLoad.addLoadCount(1);
		//				//				registrationFactory.setValue(SERVICE_ROOT + SERVICE_LOAD + "/" + tempServerLoad.getServerName(), Integer.toString(tempServerLoad.getLoadCount().get()));
		//				serverLoadList.addFirst(tempServerLoad);
		//			} catch (Throwable e) {
		//				LOG.error(null, e);
		//			}
		//		}
	}

	private final RegistrationEventListener<PathChildrenCacheEvent> customerEventListener = new RegistrationEventListener<PathChildrenCacheEvent>() {

		public void nodeChanged(PathChildrenCacheEvent params, Object... other) throws Exception {

			if (PathChildrenCacheEvent.Type.CHILD_ADDED == params.getType()) {
				String path = params.getData().getPath();
				// String serverName = path.substring(path.lastIndexOf("/") + 1);
				// 领导
				if (isLedder) {
					//					Thread.sleep(getEventHandleDelay());
					//负载计算
					dstributionService(path);
				}

			} else if (PathChildrenCacheEvent.Type.CHILD_UPDATED == params.getType()) {
				//				byte[] data = params.getData().getData();
				//				String path = params.getData().getPath();
				//				if (isLedder && data.length < 5) {
				//					dstributionService(path);
				//				}
				System.out.println("节点修改:" + params.getData().getPath());

			} else if (PathChildrenCacheEvent.Type.CHILD_REMOVED == params.getType()) {
				if (serverList.size() > 0) {
					try {
						// String path = params.getData().getPath();
						// String serverName = path.substring(path.lastIndexOf("/") + 1);
						// tempServerLoad.getServerName() + SEPARATOR + tempServerLoad.getServerUrl()

						String[] paths = new String(params.getData().getData()).split(SEPARATOR);
						ServerLoad tempServerLoad = serverList.get(paths[0]);
						tempServerLoad.addLoadCount(-1);
						registrationFactory.setValue(SERVICE_ROOT + SERVICE_LOAD + "/" + tempServerLoad.getServerName(), Integer.toString(tempServerLoad.getLoadCount().get()));
					} catch (Throwable e) {
						LOG.error(null, e);
					}
				}
			}
		}
	};
	private final RegistrationEventListener<PathChildrenCacheEvent> producerEventListener = new RegistrationEventListener<PathChildrenCacheEvent>() {

		public void nodeChanged(PathChildrenCacheEvent params, Object... other) throws Exception {
			if (!isLedder)
				return;
			if (PathChildrenCacheEvent.Type.CHILD_ADDED == params.getType()) {
				try {
					String path = params.getData().getPath();
					String serverName = path.substring(path.lastIndexOf("/") + 1);
					ServerLoad serverLoad;
					serverLoad = new ServerLoad(serverName, registrationFactory.getValue(SERVICE_ROOT + SERVICE_PRODUCER + "/" + serverName), Integer.valueOf(registrationFactory.getValue(SERVICE_ROOT + SERVICE_LOAD + "/" + serverName)));
					serverList.put(serverName, serverLoad);
					serverLoadList.addFirst(serverLoad);
				} catch (Throwable e) {
					LOG.error(null, e);
				}

			} else if (PathChildrenCacheEvent.Type.CHILD_UPDATED == params.getType()) {
				LOG.info("Modify the service,path=" + params.getData().getPath() + " content:" + new String(params.getData().getData()));
			} else if (PathChildrenCacheEvent.Type.CHILD_REMOVED == params.getType()) {
				String path = params.getData().getPath();
				String serverName = path.substring(path.lastIndexOf("/") + 1);
				serverList.remove(params.getData().getPath());
				for (int i = 0; i < serverLoadList.size(); i++) {
					if (serverName.equals(serverLoadList.get(i).getServerName())) {
						serverLoadList.remove(i);
						break;
					}
				}
			}
		}
	};

	protected void ledderService() throws Throwable {
		if (isStart)
			return;
		isStart = true;
		// 运行
		List<String> tempList = registrationFactory.getServiceList(SERVICE_ROOT + SERVICE_PRODUCER);
		for (String str : tempList) {
			final ServerLoad serverLoad = new ServerLoad(str, registrationFactory.getValue(SERVICE_ROOT + SERVICE_PRODUCER + "/" + str), Integer.valueOf(registrationFactory.getValue(SERVICE_ROOT + SERVICE_LOAD + "/" + str)));
			serverList.put(str, serverLoad);
			serverLoadList.add(serverLoad);
		}
		// 分配
		tempList = registrationFactory.getServiceList(SERVICE_ROOT + SERVICE_CUSTOMER);
		String tempValue;
		ServerLoad tempLoad;
		for (String str : tempList) {
			if (null == (tempValue = registrationFactory.getValue(SERVICE_ROOT + SERVICE_CUSTOMER + "/" + str)) || tempValue.length() < 3) {
				tempLoad = serverLoadList.pollLast();
				registrationFactory.setValue(SERVICE_ROOT + SERVICE_CUSTOMER + "/" + str, tempLoad.getServerUrl());
				tempLoad.addLoadCount(1);
				serverLoadList.addFirst(tempLoad);
				Collections.reverse(serverLoadList);
			}
		}
	}
	
	

	public String getRegisterDirectory() throws Throwable {
		LOG.warn("not support");
		return null;
	}


	public void unRegister(String directory) throws Throwable {
		LOG.warn("not support");
	}

	public String register(String directory, String value) throws Throwable {
		if (isRegister)
			return null;
		registrationFactory.start();
		isRegister = true;
		registrationFactory.createService(SERVICE_ROOT, null);
		registrationFactory.createService(SERVICE_ROOT + SERVICE_PRODUCER, producerEventListener);
		registrationFactory.createService(SERVICE_ROOT + SERVICE_CUSTOMER, customerEventListener);
		registrationFactory.createService(SERVICE_ROOT + SERVICE_LOAD, null);

		registrationFactory.register(SERVICE_ROOT + SERVICE_PRODUCER + "/" + applicationName, applicationUrl, null);
		registrationFactory.register(SERVICE_ROOT + SERVICE_LOAD + "/" + applicationName, "0", null);

		// final CountDownLatch countDownLatch = new CountDownLatch(1);
		registrationFactory.election(SERVICE_ROOT + SERVICE_LEDDER, new ElectionCallBack() {

			public void callBack(boolean leader) {
				if (leader) {
					isLedder = true;
					System.out.println("我是领导");
					try {
						ledderService();
						// Collections.reverse(serverLoadList);
						// serverList.get("temp_application").addLoadCount(100);
						// System.out.println("xxx:" + serverLoadList.get(0).getLoadCount().get());
						// System.out.println("我是领导2");
						// registrationFactory.setValue("/orinal_rmi/customer/temp_application", "9123123123");
					} catch (Throwable e) {
						LOG.error(null, e);
					}
				} else {
					// System.out.println("我不是领导");
					isRegister = false;
					isLedder = false;
				}
				// countDownLatch.countDown();
			}
		});
		// countDownLatch.await();
		return null;
	}

	public static void main(String[] args) throws Throwable {

		// RegisterServiceHandler rsh = new RegisterServiceHandler();
		//
		// rsh.registrationFactory = new ZookeeperRegistrationFactory();
		// rsh.registrationFactory.init();
		// rsh.register();
		// rsh.registrationFactory.setValue("/orinal_rmi/customer/temp_application", "993");
		// temp_application

		// SortedMap<String, Integer> a = new TreeMap<String, Integer>();
		// SortedMap<String, Integer> b = new TreeMap<String, Integer>();
		//
		// a.put("d", 4);
		// a.put("c", 3);
		// a.put("b", 2);
		// a.put("a", 1);
		//
		// Iterator<Entry<String, Integer>> it = a.entrySet().iterator();
		// while (it.hasNext()) {
		// Entry<String, Integer> en = it.next();
		// System.err.println(en.getKey() + ":" + en.getValue());
		// }

		List<Integer> al = Arrays.asList(9, 5, 3, 2, 1, 4, 5);
		Collections.sort(al);
		System.err.println(al);

	}

	public int getEventHandleDelay() {
		return eventHandleDelay;
	}

	public RegisterServiceServerHandler setEventHandleDelay(int eventHandleDelay) {
		this.eventHandleDelay = eventHandleDelay;
		return this;
	}

}
