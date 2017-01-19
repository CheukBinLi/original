package java.com.cheuks.bin.original.reflect.net;

import java.util.concurrent.atomic.AtomicInteger;

import com.cheuks.bin.original.reflect.rmi.TransmissionModel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class NettyServerHandle extends SimpleChannelInboundHandler<TransmissionModel> {

	public final static int HEADER_CODE = 0XABEF;
	public final static int PING = 0XAAAA;
	private AtomicInteger counter = new AtomicInteger();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TransmissionModel msg) throws Exception {
		System.out.println("收到消息_交给 invoke handler处理");
		if (TransmissionModel.HEART_BEAT == msg.getServiceType()) {
			counter.set(0);
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// // TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			// 空闲6s之后触发 (心跳包丢失)
			if (counter.getAndAdd(1) >= 3) {
				// 连续丢失3个心跳包 (断开连接)
				ctx.channel().close().sync();
				System.out.println("已与Client断开连接");
			} else {
				System.out.println("丢失了第 " + counter + " 个心跳包");
			}
		}
	}

}
