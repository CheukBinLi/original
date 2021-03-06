package com.cheuks.bin.original.reflect.rmi.net.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.reflect.rmi.NettyClient;
import com.cheuks.bin.original.reflect.rmi.model.TransmissionModel;
import com.cheuks.bin.original.reflect.rmi.net.MessageHandle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class NettyClientHandle extends NettyClientMessageHandleAdapter<NettyClientHandle, TransmissionModel> {

	private static final Logger LOG = LoggerFactory.getLogger(NettyClientHandle.class);

	private ChannelHandlerContext channelHandlerContext;

	public NettyClientHandle getObject() {
		return this;
	}

	public ChannelHandlerContext getChannelHandlerContext() {
		return channelHandlerContext;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TransmissionModel msg) throws Exception {
		// System.out.println("收到消息_交给 invoke handler处理");
		if (MessageHandle.RMI_RESPONSE == msg.getServiceType())
			setResult(msg);
		ReferenceCountUtil.release(msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 对象池添加些对对象
		channelHandlerContext = ctx;
		NettyClient nettyClientObjectPool = ctx.channel().attr(NettyClient.NETTY_CLIENT_OBJECT_POOL).get();
		nettyClientObjectPool.addObject(this);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.ALL_IDLE)) {
			if (LOG.isDebugEnabled())
				LOG.debug("send hearBate package.");
			ctx.writeAndFlush(new TransmissionModel(HEAR_BEAT));
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		ctx.close();
		ctx.channel().close();
		LOG.warn("disconnect");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
		ctx.channel().close();
	}
}
