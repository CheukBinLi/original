package com.cheuks.bin.original.rmi.net.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private String applicationCode;

	private final NettyClient nettyClient;

	public void operationComplete(ChannelFuture future) throws Exception {
		if (!future.isSuccess()) {
			if (LOG.isDebugEnabled())
				LOG.debug("applicationCode:{} ,Reconnect...", applicationCode);
			try {
				nettyClient.addConnectionByServerName(applicationCode);
			} catch (Throwable e) {
				LOG.error(null, e);
			}
		} else {
		}
	}

	public SimpleChannelFutureListener(String applicationCode, final NettyClient nettyClient) {
		super();
		this.applicationCode = applicationCode;
		this.nettyClient = nettyClient;
	}

}
