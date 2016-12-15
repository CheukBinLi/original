package java.com.cheuks.bin.original.reflect.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.reflect.rmi.TransmissionModel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyMessageEncoder extends MessageToByteEncoder<TransmissionModel> {

	private static final Logger LOG = LoggerFactory.getLogger(NettyMessageEncoder.class);

	private CacheSerialize cacheSerialize;

	// 免去主次版本号
	public final static int HEADER_CODE = 0XABEF;
	public final static int PING = 0XAAAA;

	@Override
	protected void encode(ChannelHandlerContext ctx, TransmissionModel msg, ByteBuf out) throws Exception {
		System.out.println("发送消息");
		// 报头编码
		// 长度
		// 内容
		out.writeInt(HEADER_CODE);// 头
		byte[] data;
		try {
			data = cacheSerialize.encode(msg);
		} catch (Throwable e) {
			LOG.error("序列化失败", e);
			return;
		}
		out.writeInt(data.length);// 长度
		out.writeBytes(data);// 内容
		System.err.println(out.maxCapacity());
	}

	public NettyMessageEncoder(CacheSerialize cacheSerialize) {
		super();
		this.cacheSerialize = cacheSerialize;
	}

}
