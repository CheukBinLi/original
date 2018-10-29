package com.cheuks.bin.original.anything.test.net.im.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {

	private static final Logger LOG = LoggerFactory.getLogger(MessageDecoder.class);

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf b = (ByteBuf) super.decode(ctx, in);
		if (null == b)
			return null;
		int headerCode = b.readInt();
		if (headerCode != RMI_SERVICE_TYPE_HEADER_TAG) {
			LOG.error("校验失败,HEADER_CODE:" + headerCode);
			return null;
		}
		int length = b.readInt();
		if (LOG.isDebugEnabled())
			LOG.debug(String.format("TransmissionModel大小:%d 字节", length));
		byte[] data = new byte[length];
		b.readBytes(data);
		try {
			result = cacheSerialize.decodeT(data);
		} catch (Throwable e) {
			LOG.error("序列化失败", e);
			return result;
		}
		try {
			return result;
		} finally {
			ReferenceCountUtil.release(in);
		}

	}

	public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, CacheSerialize cacheSerialize) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		this.cacheSerialize = cacheSerialize;
	}
}
