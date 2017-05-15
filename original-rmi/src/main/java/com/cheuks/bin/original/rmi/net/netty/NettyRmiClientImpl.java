package com.cheuks.bin.original.rmi.net.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cheuks.bin.original.common.rmi.RmiClient;
import com.cheuks.bin.original.common.rmi.model.TransmissionModel;

/***
 * 远程方法简单实现
 * 
 * @author ben
 *
 */
public class NettyRmiClientImpl implements RmiClient {

	private static final Logger LOG = LoggerFactory.getLogger(NettyRmiClientImpl.class);

	@Autowired
	private NettyClient nettyClientFactory;

	public Object rmiInvoke(String methodName, Object... params) {
		TransmissionModel transmissionModel = new TransmissionModel();
		transmissionModel.setMethodCode(methodName).setParams(params).setServiceType(RMI_SERVICE_TYPE_REQUEST);
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
