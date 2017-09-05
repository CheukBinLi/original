package com.cheuks.bin.original.rmi.net;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.registrationcenter.ElectionCallBack;
import com.cheuks.bin.original.common.registrationcenter.RegistrationEventListener;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;
import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;

public class SimpleLoadBalanceFactory implements LoadBalanceFactory<String, Void> {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleLdapFactory.class);

	private RegistrationFactory<CuratorFramework, PathChildrenCacheEvent, NodeCache> registrationFactory;

	private SimpleLdapFactory simpleLdapFactory = SimpleLdapFactory.instance();

	private volatile boolean isInit;

	private String serverUrl;

	private String[] applicationCode;

	private LoadType loadType;

	/***
	 * @param registerInfo[]{applicationCode}
	 */
	public String getResource(Object... registerInfo) {
		return null;
	}

	/***
	 * @param registerInfo[]{applicationCode,clientUrl}
	 */
	public String getResourceAndUseRegistration(Object... registerInfo) {
		return null;
	}

	/***
	 * @param registerInfo[]{applicationCode,clientUrl}
	 */
	public Void useRegistration(Object... registerInfo) {
		return null;
	}

	/***
	 * @param registerInfo[]{applicationCode,clientUrl}
	 */
	public void cancleRegistration(Object... registerInfo) {
		try {
			registrationFactory.removeServiceDirectory(simpleLdapFactory.getLoadDirectory(registerInfo[0].toString()) + registerInfo[1].toString());
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

	public void init() {
		if (isInit)
			return;
		isInit = true;
		//初始化节点
		//所有服务器列表
		try {
			if (loadType == LoadType.server || loadType == LoadType.both)
				registrationFactory.register(simpleLdapFactory.getServersDirectory() + serverUrl, serverUrl, null);
			else
				registrationFactory.register(simpleLdapFactory.getConsumerDirectory() + serverUrl, serverUrl, null);
			for (String code : applicationCode) {
				if (loadType == LoadType.server || loadType == LoadType.both) {
					registrationFactory.register(simpleLdapFactory.getProviderDirectory(code) + serverUrl, serverUrl, null);
					//application节点-->ledder选举
					registrationFactory.election(simpleLdapFactory.getLedderDirectory(code), new LedderEventHandle());
					//-------application节点-->负载节点侦听
					registrationFactory.createService(simpleLdapFactory.getLoadDirectory(code), new LoadEventHandle());
				} else {
					registrationFactory.register(simpleLdapFactory.getConsumerDirectory(code) + serverUrl, serverUrl, null);
				}
			}
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

	public SimpleLoadBalanceFactory(String serverUrl, String[] applicationCode, LoadType loadType) {
		super();
		this.serverUrl = serverUrl;
		this.applicationCode = applicationCode;
		this.loadType = loadType;
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
	 * @Description: 负载侦听事件
	 * @Company:
	 * @Email: 20796698@qq.com
	 * @author cheuk.bin.li
	 * @date 2017年5月1日 下午10:08:13
	 *
	 */
	class LoadEventHandle implements RegistrationEventListener<PathChildrenCacheEvent> {

		public void nodeChanged(PathChildrenCacheEvent params, Object... other) throws Exception {

		}

	}
}
