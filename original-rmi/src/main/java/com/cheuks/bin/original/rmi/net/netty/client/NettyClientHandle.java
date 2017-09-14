package com.cheuks.bin.original.rmi.net.netty.client;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.rmi.model.TransmissionModel;
import com.cheuks.bin.original.common.rmi.net.MessageHandle;
import com.cheuks.bin.original.common.rmi.net.NetworkClient;
import com.cheuks.bin.original.rmi.config.RmiConfig.RmiConfigGroup;
import com.cheuks.bin.original.rmi.net.netty.message.NettyClientMessageHandleAdapter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class NettyClientHandle extends NettyClientMessageHandleAdapter<NettyClientHandle, TransmissionModel> {

	private static final Logger LOG = LoggerFactory.getLogger(NettyClientHandle.class);

	private NetworkClient<Bootstrap, NettyClientHandle, InetSocketAddress, String, Void, Channel, RmiConfigGroup> nettyClient;

	private volatile ChannelHandlerContext channelHandlerContext;

	public NettyClientHandle getObject() {
		return this;
	}

	public ChannelHandlerContext getChannelHandlerContext() {
		return channelHandlerContext;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TransmissionModel msg) throws Exception {
		if (MessageHandle.RMI_SERVICE_TYPE_RESPONSE == msg.getServiceType()) {
			setResult(msg);
		}
		ReferenceCountUtil.release(msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		channelHandlerContext = ctx;
		try {
			// 对象池添加些对对象
			nettyClient.addWorker(this);
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.ALL_IDLE)) {
			if (LOG.isDebugEnabled())
				LOG.debug("send hearBate package.");
			ctx.writeAndFlush(new TransmissionModel(RMI_SERVICE_TYPE_HEAR_BEAT));
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.close();
		ctx.channel().close();
		super.channelInactive(ctx);
		if (LOG.isDebugEnabled())
			LOG.debug("disconnect: reconnection");
		try {
			// 断线重连
			nettyClient.changeServerConnection(this);
		} catch (Throwable e1) {
			throw new Exception(e1);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
		ctx.channel().close();
	}

	public NettyClientHandle(NetworkClient<Bootstrap, NettyClientHandle, InetSocketAddress, String, Void, Channel, RmiConfigGroup> nettyClient) {
		super();
		this.nettyClient = nettyClient;
	}

}
