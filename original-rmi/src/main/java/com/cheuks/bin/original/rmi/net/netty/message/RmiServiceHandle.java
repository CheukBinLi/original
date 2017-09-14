package com.cheuks.bin.original.rmi.net.netty.message;

import com.cheuks.bin.original.common.rmi.RmiBeanFactory;
import com.cheuks.bin.original.common.rmi.model.MethodBean;
import com.cheuks.bin.original.common.rmi.model.TransmissionModel;
import com.cheuks.bin.original.common.rmi.net.MessageHandle;

import io.netty.channel.ChannelHandlerContext;

@SuppressWarnings("rawtypes")
public class RmiServiceHandle implements MessageHandle<ChannelHandlerContext, TransmissionModel> {

	private RmiBeanFactory rmiBeanFactory;

	public RmiServiceHandle(RmiBeanFactory rmiBeanFactory) {
		super();
		this.rmiBeanFactory = rmiBeanFactory;
	}

	public void invoke(ChannelHandlerContext i, TransmissionModel v) {

	}

	public RmiBeanFactory getRmiBeanFactory() {
		return rmiBeanFactory;
	}

	public RmiServiceHandle setRmiBeanFactory(RmiBeanFactory rmiBeanFactory) {
		this.rmiBeanFactory = rmiBeanFactory;
		return this;
	}

	public int serverType() {
		return RMI_SERVICE_TYPE_REQUEST;
	}

	public void doHandle(ChannelHandlerContext i, TransmissionModel v) {
		try {
			MethodBean methodBean = rmiBeanFactory.getMethod(v.getMethodCode());
			if (null != methodBean) {
				Object a = methodBean.getClassBean().getInstance(rmiBeanFactory);
				Object result = methodBean.getCurrentMethod().invoke(a, v.getParams());
				v.setResult(result);
			} else {
				v.setError(new NullPointerException("can't found " + v.getMethodCode()));
			}
		} catch (Throwable e) {
			v.setError(e);
		} finally {
			v.setServiceType(RMI_SERVICE_TYPE_RESPONSE);
			i.writeAndFlush(v);
		}
	}

}
