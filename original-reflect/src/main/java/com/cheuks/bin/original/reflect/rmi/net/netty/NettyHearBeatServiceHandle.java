package com.cheuks.bin.original.reflect.rmi.net.netty;

import com.cheuks.bin.original.reflect.rmi.model.TransmissionModel;
import com.cheuks.bin.original.reflect.rmi.net.ServiceHandle;

import io.netty.channel.ChannelHandlerContext;

/***
 * 心跳消息处理
 * 
 * @author ben
 *
 */
public class NettyHearBeatServiceHandle implements ServiceHandle<ChannelHandlerContext, TransmissionModel> {

	public void invoke(ChannelHandlerContext i, TransmissionModel v) {
		// System.err.println("心跳包");
	}

}
