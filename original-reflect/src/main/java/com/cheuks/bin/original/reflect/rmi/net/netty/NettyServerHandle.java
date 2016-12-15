package com.cheuks.bin.original.reflect.rmi.net.netty;

import java.util.concurrent.atomic.AtomicInteger;

import com.cheuks.bin.original.reflect.rmi.model.TransmissionModel;
import com.cheuks.bin.original.reflect.rmi.net.MessageHandle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class NettyServerHandle extends SimpleChannelInboundHandler<TransmissionModel> {

	private final MessageHandle<Object, Object, Object> messageHandle;

	public NettyServerHandle(MessageHandle<Object, Object, Object> messageHandle) {
		this.messageHandle = messageHandle;
	}

	private AtomicInteger counter = new AtomicInteger();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TransmissionModel msg) throws Exception {
		// System.out.println("收到消息_交给 invoke handler处理");
		counter.set(0);
		// NettyHandleServiceFactory.newInstance().messageHandle(ctx, msg, msg.getServiceType());
		messageHandle.messageHandle(ctx, msg, msg.getServiceType());
		ReferenceCountUtil.release(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
		ctx.channel().close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			// 空闲6s之后触发 (心跳包丢失)
			if (counter.getAndAdd(1) >= 3) {
				// 连续丢失3个心跳包 (断开连接)
				ctx.close();
				ctx.channel().close();
				System.out.println("已与Client断开连接");
			} else {
				System.out.println("丢失了第 " + counter + " 个心跳包");
			}
		}
	}
}
