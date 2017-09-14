package com.cheuks.bin.original.rmi;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.rmi.config.ReferenceGroupConfig.ReferenceGroupModel;
import com.cheuks.bin.original.rmi.net.netty.client.NettyClientPool;
import com.cheuks.bin.original.rmi.net.netty.client.NettyNetworkClient;
import com.cheuks.bin.original.rmi.net.netty.server.NettyServer;

public class SimpleRmiService implements RmiContant, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleRmiService.class);

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContext ac = applicationContext;
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("rmi server init.");
			if (null == (applicationContext = ac.getParent())) {
				applicationContext = ac;
			}
			// // 加载参数
			if (applicationContext.containsBean(BEAN_RMI_NETWORK_SERVER)) {
				NettyServer nettyServer = (NettyServer) applicationContext.getBean(BEAN_RMI_NETWORK_SERVER);
				nettyServer.start();
			}
			if (applicationContext.containsBean(BEAN_RMI_NETWORK_CLIENT)) {
				// 初始化客户端
				NettyNetworkClient nettyClient = (NettyNetworkClient) applicationContext.getBean(BEAN_RMI_NETWORK_CLIENT);
				nettyClient.start();
				// 根据服务初始各服务线程池
				NettyClientPool pool;
				for (Entry<String, ReferenceGroupModel> en : nettyClient.getRmiConfigGroup().getReferenceGroup().getReferenceGroup().entrySet()) {
					pool = new NettyClientPool(nettyClient, en.getKey());
					pool.start();
				}

			}
			if (LOG.isDebugEnabled())
				LOG.debug("rmi server complete.");
		} catch (Throwable e) {
			LOG.error(SimpleRmiService.class.getName(), e);
		}
	}
}
