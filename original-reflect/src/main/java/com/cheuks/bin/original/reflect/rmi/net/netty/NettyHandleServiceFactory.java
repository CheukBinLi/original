package com.cheuks.bin.original.reflect.rmi.net.netty;

import com.cheuks.bin.original.reflect.rmi.model.TransmissionModel;
import com.cheuks.bin.original.reflect.rmi.net.HandleService;

import io.netty.channel.ChannelHandlerContext;

public class NettyHandleServiceFactory extends HandleService<ChannelHandlerContext, TransmissionModel> {

	private static NettyHandleServiceFactory newInstance;

	public static final NettyHandleServiceFactory newInstance(int poolSize) {
		if (null == newInstance) {
			synchronized (ChannelHandlerContext.class) {
				if (null == newInstance) {
					newInstance = new NettyHandleServiceFactory();
					newInstance().start(poolSize);
				}
			}
		}
		return newInstance;
	}

	public static final NettyHandleServiceFactory newInstance() {
		return newInstance(15);
	}

}
