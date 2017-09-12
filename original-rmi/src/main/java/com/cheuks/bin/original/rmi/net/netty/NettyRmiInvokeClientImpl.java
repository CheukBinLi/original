package com.cheuks.bin.original.rmi.net.netty;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.rmi.RmiInvokeClient;
import com.cheuks.bin.original.common.rmi.model.TransmissionModel;
import com.cheuks.bin.original.common.rmi.net.NetworkClient;
import com.cheuks.bin.original.rmi.config.RmiConfigArg;
import com.cheuks.bin.original.rmi.net.netty.client.NettyClientHandle;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

/***
 * 远程方法简单实现
 * 
 * @author ben
 *
 */
public class NettyRmiInvokeClientImpl implements RmiInvokeClient {

	private static final Logger LOG = LoggerFactory.getLogger(NettyRmiInvokeClientImpl.class);

	private NetworkClient<Bootstrap, NettyClientHandle, InetSocketAddress, String, Void, RmiConfigArg, Boolean, Channel> nettyClient;

	public Object rmiInvoke(String applicationName, String methodName, Object... params) {
		TransmissionModel transmissionModel = new TransmissionModel();
		transmissionModel.setMethodCode(methodName).setParams(params).setServiceType(RMI_SERVICE_TYPE_REQUEST);
		NettyClientHandle nettyClientHandle = null;
		try {
			try {
				// nettyClientHandle = nettyClientFactory.borrowObject();
				nettyClientHandle = nettyClient.getObjectPoolManager().borrowObject(applicationName);
				nettyClientHandle.getObject().getChannelHandlerContext().writeAndFlush(transmissionModel);
				transmissionModel = nettyClientHandle.callBack();
				// return transmissionModel.getResult();
				if (null != transmissionModel.getError()) {
					LOG.error(null, transmissionModel.getError());
					return null;
				}
				return transmissionModel.getResult();
			} catch (Throwable e) {
				LOG.error(null, e);
			}
		} finally {
			try {
				nettyClient.getObjectPoolManager().returnObject(applicationName, nettyClientHandle);
			} catch (Throwable e) {
				LOG.error(null, e);
			}
		}
		return null;
	}

	public NetworkClient<Bootstrap, NettyClientHandle, InetSocketAddress, String, Void, RmiConfigArg, Boolean, Channel> getNettyClient() {
		return nettyClient;
	}

	public void setNettyClient(NetworkClient<Bootstrap, NettyClientHandle, InetSocketAddress, String, Void, RmiConfigArg, Boolean, Channel> nettyClient) {
		this.nettyClient = nettyClient;
	}

}
