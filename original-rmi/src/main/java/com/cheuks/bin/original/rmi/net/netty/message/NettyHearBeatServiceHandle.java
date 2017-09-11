package com.cheuks.bin.original.rmi.net.netty.message;

import com.cheuks.bin.original.common.rmi.model.TransmissionModel;
import com.cheuks.bin.original.common.rmi.net.MessageHandle;

import io.netty.channel.ChannelHandlerContext;

/***
 * 心跳消息处理
 * 
 * @author ben
 *
 */
public class NettyHearBeatServiceHandle implements MessageHandle<ChannelHandlerContext, TransmissionModel> {

	public int serverType() {
		return RMI_SERVICE_TYPE_HEAR_BEAT;
	}

	public void doHandle(ChannelHandlerContext i, TransmissionModel v) {
		// System.err.println("心跳包");
	}

}
