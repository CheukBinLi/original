package java.com.cheuks.bin.original.reflect.net;

import com.cheuks.bin.original.reflect.rmi.TransmissionModel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyClientHandle extends SimpleChannelInboundHandler<TransmissionModel> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TransmissionModel msg) throws Exception {
		System.out.println("收到消息_交给 invoke handler处理");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		TransmissionModel transmissionModel = new TransmissionModel();
		// transmissionModel.setError(new Exception("哇哈哈"));
		ctx.writeAndFlush(transmissionModel);

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.ALL_IDLE)) {
			// 不管是读事件空闲还是写事件空闲都向服务器发送心跳包
			ctx.writeAndFlush(new TransmissionModel(TransmissionModel.HEART_BEAT));
			System.err.println("client发回复心跳");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// // TODO Auto-generated method stub
		// super.exceptionCaught(ctx, cause);
	}

}
