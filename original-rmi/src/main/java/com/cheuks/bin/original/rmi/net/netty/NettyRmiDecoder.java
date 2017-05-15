package com.cheuks.bin.original.rmi.net.netty;

import com.cheuks.bin.original.common.rmi.RmiException;
import com.cheuks.bin.original.common.rmi.net.AbstractRmiDecoder;

import io.netty.channel.ChannelHandlerContext;

public class NettyRmiDecoder extends AbstractRmiDecoder<ChannelHandlerContext> {

	@Override
	public byte[] extractData(ChannelHandlerContext input) throws RmiException {
		return null;
	}

}
