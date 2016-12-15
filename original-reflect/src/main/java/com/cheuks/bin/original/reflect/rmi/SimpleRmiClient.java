package com.cheuks.bin.original.reflect.rmi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.reflect.test2I;
import com.cheuks.bin.original.reflect.rmi.model.TransmissionModel;
import com.cheuks.bin.original.reflect.rmi.net.HandleType;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyClientHandle;

/***
 * 远程方法简单实现
 * 
 * @author ben
 *
 */
public class SimpleRmiClient implements RmiClient {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleRmiClient.class);

	private NettyClient nettyClientFactory = NettyClientAdapter.newInstance();

	public Object rmiInvoke(String methodName, Object... params) {
		TransmissionModel transmissionModel = new TransmissionModel();
		transmissionModel.setMethodCode(methodName).setParams(params).setServiceType(HandleType.RMI_REQUEST);
		NettyClientHandle nettyClientHandle = null;
		try {
			try {
				nettyClientHandle = nettyClientFactory.borrowObject();
				nettyClientHandle.getObject().getChannelHandlerContext().writeAndFlush(transmissionModel);
				transmissionModel = nettyClientHandle.callBack();
				// return transmissionModel.getResult();
				if (null != transmissionModel.getError()) {
					LOG.error(null, transmissionModel.getError());
					return null;
				}
				return transmissionModel.getResult();
			} catch (Exception e) {
				LOG.error(null, e);
			}
		} finally {
			try {
				nettyClientFactory.returnObject(nettyClientHandle);
			} catch (Exception e) {
			}
		}
		return null;
	}
}
