package com.cheuks.bin.original.reflect.rmi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.cheuks.bin.original.common.registrationcenter.RegistrationEventListener;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;

public class RegisterServiceClientHandler implements RegisterService, ApplicationListener<ApplicationEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(RegisterServiceClientHandler.class);

	private RegistrationFactory<CuratorFramework, PathChildrenCacheEvent, NodeCache> registrationFactory;

	private String applicationName;
	private final LinkedList<ServerLoad> serverList = new LinkedList<ServerLoad>();
	private volatile boolean isInit;

	public RegisterServiceClientHandler(String applicationName, final RegistrationFactory<CuratorFramework, PathChildrenCacheEvent, NodeCache> registrationFactory) {
		super();
		this.applicationName = applicationName;
		this.registrationFactory = registrationFactory;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (isInit)
			return;
		try {
			//初始化服务列表
			resetServerList();
			registrationFactory.createService(SERVICE_ROOT + SERVICE_LOAD, new RegistrationEventListener<PathChildrenCacheEvent>() {

				public void nodeChanged(PathChildrenCacheEvent params, Object... other) throws Exception {
					PathChildrenCacheEvent.Type eventType = params.getType();
					if (PathChildrenCacheEvent.Type.CHILD_ADDED == eventType) {
						try {
							resetServerList();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (PathChildrenCacheEvent.Type.CHILD_UPDATED == eventType) {
						try {
							resetServerList();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else if (PathChildrenCacheEvent.Type.CHILD_REMOVED == eventType) {
						try {
							resetServerList();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

	//重置
	private void resetServerList() throws Throwable {
		serverList.clear();
		List<String> tempList = registrationFactory.getServiceList(SERVICE_ROOT + SERVICE_LOAD);
		for (String str : tempList) {
			final ServerLoad serverLoad = new ServerLoad(str, registrationFactory.getValue(SERVICE_ROOT + SERVICE_PRODUCER + "/" + str), Integer.valueOf(registrationFactory.getValue(SERVICE_ROOT + SERVICE_LOAD + "/" + str)));
			serverList.add(serverLoad);
		}
		Collections.reverse(serverList);
	}

	protected ServerLoad getServerLoad() {
		ServerLoad result = serverList.pollFirst();
		try {
			return result;
		} finally {
			serverList.add(result);
		}
	}

	public synchronized String getRegisterDirectory() throws Throwable {
		if (!isInit) {
			onApplicationEvent(null);
			isInit = true;
		}
		ServerLoad server = null;
		String[] address;
		InetSocketAddress inetSocketAddress = null;
		int count = 10;
		while (count-- > -1) {
			System.out.println(Thread.currentThread().getName() + ":" + count);
			server = getServerLoad();
			address = server.getServerUrl().split(":");
			inetSocketAddress = new InetSocketAddress(address[0], Integer.valueOf(address[1]));
			if (ping(inetSocketAddress))
				break;
			Thread.sleep(5000);
		}
		if (count < 0)
			return null;
		//		String servicePath = SERVICE_ROOT + SERVICE_CUSTOMER + "/" + applicationName + "_" + System.currentTimeMillis() + "_" + server.getServerUrl();
		//		registrationFactory.register(servicePath, server.getServerName(), null);
		return server.getServerUrl();
	}

	public synchronized String register(String applicationId, String value) throws Throwable {
		String servicePath = SERVICE_ROOT + SERVICE_CUSTOMER + "/" + applicationName + "_" + System.currentTimeMillis() + "_"+ applicationId;
		registrationFactory.register(servicePath, value, null);
		return servicePath;
	}

	public void unRegister(String directory) throws Throwable {
		registrationFactory.removeServiceDirectory(directory);
	}

	protected boolean ping(InetSocketAddress ipAddress) {
		Socket socket = new Socket();
		try {
			socket.connect(ipAddress);
		} catch (IOException e) {
			return false;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		return true;
	}

	public static void main(String[] args) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("10.73.11.77", 1000));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}
