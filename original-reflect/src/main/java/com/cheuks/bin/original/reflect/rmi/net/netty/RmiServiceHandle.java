package com.cheuks.bin.original.reflect.rmi.net.netty;

import java.util.HashMap;
import java.util.Map;

import com.cheuks.bin.original.reflect.rmi.DefaultRmiBeanFactory;
import com.cheuks.bin.original.reflect.rmi.RmiBeanFactory;
import com.cheuks.bin.original.reflect.rmi.model.MethodBean;
import com.cheuks.bin.original.reflect.rmi.model.TransmissionModel;
import com.cheuks.bin.original.reflect.rmi.net.ServiceHandle;

import io.netty.channel.ChannelHandlerContext;

public class RmiServiceHandle implements ServiceHandle<ChannelHandlerContext, TransmissionModel> {

	private RmiBeanFactory rmiBeanFactory;
	private String scanPath;

	public RmiServiceHandle(RmiBeanFactory rmiBeanFactory) {
		super();
		this.rmiBeanFactory = rmiBeanFactory;
	}

	public void invoke(ChannelHandlerContext i, TransmissionModel v) {
		if (!rmiBeanFactory.isActivate()) {
			synchronized (this) {
				if (!rmiBeanFactory.isActivate()) {
					Map<String, Object> args = new HashMap<String, Object>();
					args.put("scan", scanPath);
					rmiBeanFactory.init(args);
				}
			}
		}
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
			v.setServiceType(RMI_RESPONSE);
			i.writeAndFlush(v);
		}
	}

	public RmiBeanFactory getRmiBeanFactory() {
		return rmiBeanFactory;
	}

	public RmiServiceHandle setRmiBeanFactory(DefaultRmiBeanFactory defaultRmiBeanFactory) {
		this.rmiBeanFactory = defaultRmiBeanFactory;
		return this;
	}

	public String getScanPath() {
		return scanPath;
	}

	public RmiServiceHandle setScanPath(String scanPath) {
		this.scanPath = scanPath;
		return this;
	}

}
