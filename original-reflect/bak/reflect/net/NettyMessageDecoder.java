package java.com.cheuks.bin.original.reflect.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.reflect.rmi.TransmissionModel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

	private static final Logger LOG = LoggerFactory.getLogger(NettyMessageDecoder.class);

	private CacheSerialize cacheSerialize;

	// 免去主次版本号
	public final static int HEADER_CODE = 0XABEF;
	public final static int PING = 0XAAAA;

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

		System.out.println("接收消息");
		ByteBuf b = (ByteBuf) super.decode(ctx, in);
		if (null == b)
			return null;
		TransmissionModel result = null;
		int headerCode = b.readInt();
		if (PING == headerCode) {
		}
		// if (headerCode != HEADER_CODE) {
		// LOG.error("校验失败,HEADER_CODE:" + headerCode);
		// return;
		// }
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
		return result;
	}

	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, CacheSerialize cacheSerialize) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		this.cacheSerialize = cacheSerialize;
	}
}
