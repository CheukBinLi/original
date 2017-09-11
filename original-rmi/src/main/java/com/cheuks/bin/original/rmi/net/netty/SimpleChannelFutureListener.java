package com.cheuks.bin.original.rmi.net.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.rmi.model.ConsumerValueModel;
import com.cheuks.bin.original.rmi.net.netty.client.NettyClient;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/***
 * 
 * @Title: original-rmi
 * @Description: 断线重连
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年5月1日 上午7:51:49
 *
 */
public class SimpleChannelFutureListener implements ChannelFutureListener {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleChannelFutureListener.class);

	ConsumerValueModel consumerValueModel;

	private final NettyClient nettyClient;

	public void operationComplete(ChannelFuture future) throws Exception {
		System.err.println("################");
		if (future.isSuccess()) {
			try {
				// nettyClient.operationComplete(future.channel(), consumerValueModel.getServiceName(), consumerValueModel.getConsumerName());
				// RegisterLoadBalanceModel registerLoadBalanceModel = new RegisterLoadBalanceModel();
				// registerLoadBalanceModel.setType(ServiceType.client);
				// registerLoadBalanceModel.setServerName(consumerValueModel.getServerName());
				// registerLoadBalanceModel.setServiceName(consumerValueModel.getServiceName());
				// registerLoadBalanceModel.setUrl(consumerValueModel.getServerUrl());
				// registerLoadBalanceModel.setValue(consumerValueModel.getConsumerName());
				// registerLoadBalanceModel.setDesc(consumerValueModel.getConsumerUrl());
				nettyClient.operationComplete(future.channel(), consumerValueModel.getServerName(), consumerValueModel.getServiceName(), consumerValueModel.getServerUrl(), consumerValueModel.getConsumerName(), consumerValueModel.getConsumerUrl());
			} catch (Throwable e) {
				throw new Exception(e);
			}
		} else {
			if (LOG.isDebugEnabled())
				LOG.debug("applicationCode:{} ,Reconnect...", consumerValueModel.getConsumerName());
			try {
				nettyClient.changeServerConnection(consumerValueModel);
			} catch (Throwable e) {
				LOG.error(null, e);
			}
		}
	}

	public SimpleChannelFutureListener(NettyClient nettyClient, ConsumerValueModel consumerValueModel) {
		this.consumerValueModel = consumerValueModel;
		this.nettyClient = nettyClient;
	}

}
