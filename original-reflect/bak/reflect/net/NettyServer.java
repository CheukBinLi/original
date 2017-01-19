package java.com.cheuks.bin.original.reflect.net;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyServer {

	private CacheSerialize cacheSerialize;
	private int port = 10086;

	public void run() throws InterruptedException {
		System.out.println("启动");
		if (null == cacheSerialize)
			cacheSerialize = new FstCacheSerialize();
		// cacheSerialize = new KryoCacheSerialize();
		// cacheSerialize = new DefaultCacheSerialize();
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					// 注册handler
					ch.pipeline().addLast(new NettyMessageDecoder(Integer.MAX_VALUE, 4, 4, cacheSerialize));
					ch.pipeline().addLast(new NettyMessageEncoder(cacheSerialize));
					ch.pipeline().addLast(new IdleStateHandler(60, 0, 0));
					ch.pipeline().addLast(new NettyServerHandle());
				}
			});

			// ChannelFuture f = server.bind(port).sync();
			// f.channel().closeFuture().sync();
			// System.out.println("关闭");

			server.bind(port).sync().channel().closeFuture().sync();
			System.out.println("关闭");

		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Throwable {
		new NettyServer().run();

		String a = "1234567890";
		System.out.println(a.substring(0, 3) + "!");
		System.out.println(a.substring(3, 6) + "!");
		System.out.println(a.substring(6, 10) + "!");

		// Class a = Class.forName("java.lang.Exception");
		// System.err.println(a.getName());

		// TransmissionModel t = new TransmissionModel();
		// t.setId(1);
		// NettyServer ns = new NettyServer();
		// CacheSerialize cs = new KryoCacheSerialize();
		// byte[] b = cs.encode(t);
		// Object o = cs.decodeT(b);

		System.out.println("asdfasd\"fs\"adf".replace("\"", "@!"));

	}

}
