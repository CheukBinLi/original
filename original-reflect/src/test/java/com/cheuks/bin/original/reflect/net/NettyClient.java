package java.com.cheuks.bin.original.reflect.net;

import java.net.InetSocketAddress;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyClient {

	private CacheSerialize cacheSerialize;

	public void run(String hostname, int port) {
		if (null == cacheSerialize)
			cacheSerialize = new FstCacheSerialize();
		try {
			EventLoopGroup worker = new NioEventLoopGroup();
			Bootstrap b = new Bootstrap();
			b.group(worker);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new NettyMessageDecoder(Integer.MAX_VALUE, 4, 4, cacheSerialize));
					ch.pipeline().addLast(new NettyMessageEncoder(cacheSerialize));
					ch.pipeline().addLast(new IdleStateHandler(0, 0, 120));
					ch.pipeline().addLast(new NettyClientHandle());
				}
			}).option(ChannelOption.SO_KEEPALIVE, true);

			System.out.println("连接开始");
			ChannelFuture f = b.connect(new InetSocketAddress(hostname, port));
			f.channel().closeFuture().sync();
			System.out.println("连接结束");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new NettyClient().run("127.0.0.1", 10086);
	}

}
