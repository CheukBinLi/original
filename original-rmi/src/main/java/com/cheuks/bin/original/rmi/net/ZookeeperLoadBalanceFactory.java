package com.cheuks.bin.original.rmi.net;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import com.cheuks.bin.original.common.registrationcenter.ElectionCallBack;
import com.cheuks.bin.original.common.registrationcenter.RegistrationEventListener;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;
import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel.ServiceType;
import com.cheuks.bin.original.registration.center.ZookeeperRegistrationFactory;
import com.cheuks.bin.original.rmi.model.ProviderValueModel;

public class ZookeeperLoadBalanceFactory implements LoadBalanceFactory<String, Void> {

	// private static final Logger LOG = LoggerFactory.getLogger(SimpleLdapFactory.class);

	private SimpleLdapFactory simpleLdapFactory = SimpleLdapFactory.instance();

	private RegistrationFactory<CuratorFramework, PathChildrenCacheEvent, NodeCache> registrationFactory;

	private String serverList;

	private volatile boolean isInit;

	public synchronized void init() throws Throwable {
		if (isInit)
			return;
		isInit = true;
		if (null == serverList || serverList.length() < 1) {
			throw new NullPointerException("serverList is null");
		}
		if (null == registrationFactory) {
			registrationFactory = new ZookeeperRegistrationFactory(serverList);
		}
		// 初始化服务
		registrationFactory.start();
		// 初始化目录
		registrationFactory.createService(simpleLdapFactory.getRootDirectory(), null);
		registrationFactory.createService(simpleLdapFactory.getRmiRootDirectory(), null);
		registrationFactory.createService(simpleLdapFactory.getServiceDirectory(), null);
		registrationFactory.createService(simpleLdapFactory.getProviderDirectory(), null);
		registrationFactory.createService(simpleLdapFactory.getConsumerDirectory(), null);
		registrationFactory.createService(simpleLdapFactory.getLoadDirectory(), null);
		// registrationFactory.createService(simpleLdapFactory.getLedderDirectory(), null);

	}
	/***
	 * @param registerInfo[]{applicationCode}
	 *            <p>
	 *            client:返回 服务器名@连接地址
	 *            <p>
	 *            ledder:返回 连接地址
	 *            <p>
	 *            service 返回 list<服务名>
	 *            <p>
	 *            provider 返回所及提供者信息
	 *            <p>
	 *            consumer 返回所有消费者信息
	 * 
	 * @throws Throwable
	 */
	public List<String> getResource(RegisterLoadBalanceModel registerInfo) throws Throwable {
		String directory = null;
		if (ServiceType.client == registerInfo.getType()) {
			// 获取所有服务
			List<String> serverList = registrationFactory.getServiceList(simpleLdapFactory.getServiceDirectory() + "/" + registerInfo.getServiceName());
			if (null == serverList || serverList.isEmpty())
				return null;
			// 当前服务所有提供者(最SB的排序方式)
			List<String> servers = registrationFactory.getClient().getChildren().forPath(simpleLdapFactory.getServiceDirectory() + "/" + registerInfo.getServiceName());
			LinkedList<ProviderValueModel> providerValueModels = new LinkedList<ProviderValueModel>();
			ProviderValueModel providerValueModel = null;
			for (String server : servers) {
				providerValueModels.add(new ProviderValueModel(server, registrationFactory.getValue(simpleLdapFactory.getProviderDirectory(server))));
			}
			Collections.reverse(providerValueModels);
			if (providerValueModels.isEmpty())
				return null;
			return Arrays.asList((providerValueModel = providerValueModels.pollFirst()).getServerName() + "@" + providerValueModel.getUrl());
		}
		if (ServiceType.ledder == registerInfo.getType()) {
			directory = simpleLdapFactory.getLedderDirectory(registerInfo.getServiceName());
		} else if (ServiceType.service == registerInfo.getType()) {
			directory = simpleLdapFactory.getServiceDirectory() + "/" + registerInfo.getServiceName();
		} else if (ServiceType.provider == registerInfo.getType()) {
			directory = simpleLdapFactory.getProviderDirectory();
		} else if (ServiceType.consumer == registerInfo.getType()) {
			directory = simpleLdapFactory.getConsumerDirectory();
		}
		if (null == directory)
			return null;
		return registrationFactory.getServiceList(directory);
	}

	/***
	 * @param registerInfo[]{applicationCode,clientUrl}
	 * @throws Throwable
	 */
	public String getResourceAndUseRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		if (registerInfo.getType() != ServiceType.client)
			return null;
		// 获取所有服务
		List<String> serverList = registrationFactory.getServiceList(simpleLdapFactory.getServiceDirectory() + "/" + registerInfo.getServiceName());
		if (null == serverList || serverList.isEmpty())
			return null;
		// 当前服务所有提供者(最SB的排序方式)
		List<String> servers = registrationFactory.getClient().getChildren().forPath(simpleLdapFactory.getServiceDirectory() + "/" + registerInfo.getServiceName());
		LinkedList<ProviderValueModel> providerValueModels = new LinkedList<ProviderValueModel>();
		ProviderValueModel providerValueModel = null;
		for (String server : servers) {
			providerValueModels.add(new ProviderValueModel(server, Integer.valueOf(registrationFactory.getValue(simpleLdapFactory.getLoadDirectory(server)))));
		}
		Collections.reverse(providerValueModels);
		if (providerValueModels.isEmpty())
			return null;
		try {
			return (providerValueModel = providerValueModels.pollFirst()).getUrl();
		} finally {
			// 统计
			registrationFactory.setValue(simpleLdapFactory.getLoadDirectory(providerValueModel.getServerName()), providerValueModel.connectionsAdd(1).getValue());
			// 更新客户端信息
			registrationFactory.register(simpleLdapFactory.getConsumerDirectory(registerInfo.getServerName()), providerValueModel.getServerName() + "@" + providerValueModel.getUrl(), null);
		}
	}

	/***
	 * @param registerInfo[]{applicationCode,clientUrl}
	 * @throws Throwable
	 */
	public Void useRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		// 统计
		String serverName = new ProviderValueModel(registerInfo.getServerName(), registerInfo.getUrl()).getValue();
		String directory = simpleLdapFactory.getLoadDirectory(serverName);
		String value = registrationFactory.getValue(directory);
		registrationFactory.setValue(simpleLdapFactory.getLoadDirectory(serverName), Integer.toString(Integer.valueOf(value) + 1));
		// 更新客户端信息
		registrationFactory.register(simpleLdapFactory.getConsumerDirectory(registerInfo.getValue()), registerInfo.getDesc(), null);

		return null;
	}

	public Void registration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		if (ServiceType.server == registerInfo.getType()) {
			String serverName = new ProviderValueModel(registerInfo.getServerName(), registerInfo.getUrl()).getValue();
			/***
			 * path: /original/rmi/service/food/serverName@192.168.1.1:2181
			 * <p>
			 * value: 192.168.1.1:10086
			 */
			registrationFactory.createService(simpleLdapFactory.getServiceDirectory() + "/" + registerInfo.getServiceName(), null);
			registrationFactory.register(simpleLdapFactory.joinPath(simpleLdapFactory.getServiceDirectory(), registerInfo.getServiceName(), serverName), registerInfo.getUrl(), null);
			/***
			 * 服务提供者信息
			 * <p>
			 * path: /original/rmi/provider/serverName
			 * <p>
			 * value: 连接数
			 */
			registrationFactory.register(simpleLdapFactory.getLoadDirectory(serverName), "0", null);
			registrationFactory.register(simpleLdapFactory.getProviderDirectory(serverName), registerInfo.getUrl(), null);
		} else if (ServiceType.client == registerInfo.getType()) {
			/***
			 * 客户端统计
			 * <p>
			 * path: /original/rmi/consumer/serverName
			 * <p>
			 * value: ""
			 */
			registrationFactory.register(simpleLdapFactory.getConsumerDirectory(registerInfo.getServerName()), "", null);
		}
		return null;
	}

	/***
	 * @param registerInfo[]{applicationCode,clientUrl}
	 * @throws Throwable
	 */
	public void cancleRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		if (ServiceType.client == registerInfo.getType()) {
			registrationFactory.removeServiceDirectory(simpleLdapFactory.getConsumerDirectory(registerInfo.getServerName()));
		}
	}

	/***
	 * 
	 * @Title: original-rmi
	 * @Description:负载类型
	 * @Company:
	 * @Email: 20796698@qq.com
	 * @author cheuk.bin.li
	 * @date 2017年5月1日 下午9:27:30
	 *
	 */
	public enum LoadType {
		server, client, both
	}

	/***
	 * 
	 * @Title: original-rmi
	 * @Description: ledder回调事件
	 * @Company:
	 * @Email: 20796698@qq.com
	 * @author cheuk.bin.li
	 * @date 2017年5月1日 下午10:05:34
	 *
	 */
	class LedderEventHandle implements ElectionCallBack {

		public void callBack(boolean isLeader) {

		}
	}

	/***
	 * 
	 * @Title: original-rmi
	 * @Description: 侦听事件
	 * @Company:
	 * @Email: 20796698@qq.com
	 * @author cheuk.bin.li
	 * @date 2017年5月1日 下午10:08:13
	 *
	 */
	class EventHandle implements RegistrationEventListener<PathChildrenCacheEvent> {

		public void addNodeEvent(PathChildrenCacheEvent params, Object... other) {
		}
		public void updateNodeEvent(PathChildrenCacheEvent params, Object... other) {
		}
		public void removeNodeEvent(PathChildrenCacheEvent params, Object... other) {
		}

		public void nodeChanged(PathChildrenCacheEvent params, Object... other) throws Exception {
			if (PathChildrenCacheEvent.Type.CHILD_ADDED == params.getType()) {
				addNodeEvent(params, other);
			} else if (PathChildrenCacheEvent.Type.CHILD_UPDATED == params.getType()) {
				updateNodeEvent(params, other);
			} else if (PathChildrenCacheEvent.Type.CHILD_REMOVED == params.getType()) {
				removeNodeEvent(params, other);
			}
		}

	}

	public void setUrl(String url) {
		this.serverList = url;
	}

	public static void main(String[] args) throws Throwable {
		ZookeeperLoadBalanceFactory zlf = new ZookeeperLoadBalanceFactory();
		// zlf.setUrl("10.73.11.148:2181");
		zlf.setUrl("192.168.3.66:2181");
		zlf.init();
		zlf.useRegistration(null);
		synchronized (zlf) {
			zlf.wait();
		}
	}

}
