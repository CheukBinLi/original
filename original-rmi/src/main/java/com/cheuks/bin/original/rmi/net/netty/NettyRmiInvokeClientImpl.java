package com.cheuks.bin.original.rmi.net.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cheuks.bin.original.common.rmi.RmiInvokeClient;
import com.cheuks.bin.original.common.rmi.model.TransmissionModel;
import com.cheuks.bin.original.common.util.ObjectPoolManager;

/***
 * 远程方法简单实现
 * 
 * @author ben
 *
 */
public class NettyRmiInvokeClientImpl implements RmiInvokeClient {

	private static final Logger LOG = LoggerFactory.getLogger(NettyRmiInvokeClientImpl.class);

	@Autowired
	private ObjectPoolManager<NettyClientHandle, TransmissionModel> manager;

	public Object rmiInvoke(String applicationName, String methodName, Object... params) {
		TransmissionModel transmissionModel = new TransmissionModel();
		transmissionModel.setMethodCode(methodName).setParams(params).setServiceType(RMI_SERVICE_TYPE_REQUEST);
		NettyClientHandle nettyClientHandle = null;
		try {
			try {
				// nettyClientHandle = nettyClientFactory.borrowObject();
				nettyClientHandle = manager.borrowObject(applicationName);
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
				manager.returnObject(applicationName, nettyClientHandle);
			} catch (Throwable e) {
			}
		}
		return null;
	}
}
