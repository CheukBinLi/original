package com.cheuks.bin.original.reflect.rmi;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import com.cheuks.bin.original.common.registrationcenter.RegistrationEventListener;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;

public class RegisterServiceClientHandler implements RegisterService {

	private RegistrationFactory<CuratorFramework, PathChildrenCacheEvent, NodeCache> registrationFactory;

	private String applicationName;
	private Object lock = new Object();

	public RegisterServiceClientHandler(String applicationName, String applicationUrl, final RegistrationFactory<CuratorFramework, PathChildrenCacheEvent, NodeCache> registrationFactory) {
		super();
		this.applicationName = applicationName;
		this.registrationFactory = registrationFactory;
	}

	private volatile String result = null;

	public String register() throws Throwable {
		registrationFactory.register(SERVICE_ROOT + SERVICE_CUSTOMER + "/" + applicationName, "", new RegistrationEventListener<NodeCache>() {
			// 监听
			public void nodeChanged(NodeCache params, Object... other) throws Exception {
				byte[] data = params.getCurrentData().getData();
				if (data.length > 5) {
					result = new String(data);
				}
				synchronized (lock) {
					this.notify();
				}
			}
		});

		synchronized (lock) {
			this.wait();
		}
		return result;
	}

}
