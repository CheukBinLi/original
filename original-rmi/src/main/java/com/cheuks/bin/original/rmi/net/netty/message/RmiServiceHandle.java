package com.cheuks.bin.original.rmi.net.netty.message;

import com.cheuks.bin.original.common.rmi.RmiBeanFactory;
import com.cheuks.bin.original.common.rmi.model.MethodBean;
import com.cheuks.bin.original.common.rmi.model.TransmissionModel;
import com.cheuks.bin.original.common.rmi.net.MessageHandle;

import io.netty.channel.ChannelHandlerContext;

public class RmiServiceHandle implements MessageHandle<ChannelHandlerContext, TransmissionModel> {

	private RmiBeanFactory<?, ?> rmiBeanFactory;
	private String scanPath;

	public RmiServiceHandle(RmiBeanFactory<?, ?> rmiBeanFactory) {
		super();
		this.rmiBeanFactory = rmiBeanFactory;
	}

	public void invoke(ChannelHandlerContext i, TransmissionModel v) {

	}

	public RmiBeanFactory<?, ?> getRmiBeanFactory() {
		return rmiBeanFactory;
	}

	public RmiServiceHandle setRmiBeanFactory(RmiBeanFactory<?, ?> rmiBeanFactory) {
		this.rmiBeanFactory = rmiBeanFactory;
		return this;
	}

	public String getScanPath() {
		return scanPath;
	}

	public RmiServiceHandle setScanPath(String scanPath) {
		this.scanPath = scanPath;
		return this;
	}

	public int serverType() {
		return RMI_SERVICE_TYPE_REQUEST;
	}

	public void doHandle(ChannelHandlerContext i, TransmissionModel v) {
		// if (!rmiBeanFactory.isActivate()) {
		// synchronized (this) {
		// if (!rmiBeanFactory.isActivate()) {
		// Map<String, Object> args = new HashMap<String, Object>();
		// args.put("scan", scanPath);
		// rmiBeanFactory.init(args);
		// }
		// }
		// }
		try {
			MethodBean methodBean = rmiBeanFactory.getMethod(v.getMethodCode());
			if (null != methodBean) {
				Object result = methodBean.getCurrentMethod().invoke(methodBean.getClassBean().getInstance(), v.getParams());
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
