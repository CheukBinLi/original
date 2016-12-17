package com.cheuks.bin.original.reflect.rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.util.CollectionUtil;
import com.cheuks.bin.original.reflect.rmi.net.MessageHandle;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyHandleServiceFactory;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyHearBeatServiceHandle;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyMessageDecoder;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyMessageEncoder;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyServerHandle;
import com.cheuks.bin.original.reflect.rmi.net.netty.RmiServiceHandle;

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

	private static final Logger LOG = LoggerFactory.getLogger(NettyServer.class);

	private CacheSerialize cacheSerialize;
	private int port = 10086;
	private int poolSize = 15;
	private int heartBeatTimeoutSecond = 120;
	private RmiBeanFactory rmiBeanFactory;
	private int maxFrameLength = 5000;

	private MessageHandle<Object, Object, Object> messageHandle;

	public void run() throws InterruptedException, NullPointerException {
		start(this.poolSize);
	}

	public void start(int poolSize) throws InterruptedException, NullPointerException {
		LOG.info("server is start.");
		if (null == rmiBeanFactory)
			throw new NullPointerException("rmiBeanFactory is null");
		if (null == messageHandle)
			throw new NullPointerException("messageHandle is null");
		if (null == cacheSerialize)
			cacheSerialize = new FstCacheSerialize();
		// 心跳
		messageHandle.addHandle(MessageHandle.HEAR_BEAT, new NettyHearBeatServiceHandle());
		if (!messageHandle.handleContains(MessageHandle.RMI_REQUEST)) {
			// RMI服务
			messageHandle.addHandle(MessageHandle.RMI_REQUEST, new RmiServiceHandle(rmiBeanFactory));
		}

		final EventLoopGroup bossGroup = new NioEventLoopGroup(poolSize);
		final EventLoopGroup workerGroup = new NioEventLoopGroup(poolSize);
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					// 注册handler
					ch.pipeline().addLast(new NettyMessageDecoder(maxFrameLength, 4, 4, cacheSerialize));
					ch.pipeline().addLast(new NettyMessageEncoder(cacheSerialize));
					ch.pipeline().addLast(new IdleStateHandler(heartBeatTimeoutSecond, 0, 0));
					// 服务处理
					ch.pipeline().addLast(new NettyServerHandle(messageHandle));
				}
			});
			server.bind(port).sync().channel().closeFuture().sync();
			LOG.warn("service is close.");

		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public NettyServer() {
		super();
	}

	public NettyServer(int port, RmiBeanFactory rmiBeanFactory, MessageHandle<Object, Object, Object> messageHandle) {
		super();
		this.port = port;
		this.rmiBeanFactory = rmiBeanFactory;
		this.messageHandle = messageHandle;
	}

	public CacheSerialize getCacheSerialize() {
		return cacheSerialize;
	}

	public NettyServer setCacheSerialize(CacheSerialize cacheSerialize) {
		this.cacheSerialize = cacheSerialize;
		return this;
	}

	public int getPort() {
		return port;
	}

	public NettyServer setPort(int port) {
		this.port = port;
		return this;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public NettyServer setPoolSize(int poolSize) {
		this.poolSize = poolSize;
		return this;
	}

	public int getHeartBeatTimeoutSecond() {
		return heartBeatTimeoutSecond;
	}

	public NettyServer setHeartBeatTimeoutSecond(int heartBeatTimeoutSecond) {
		this.heartBeatTimeoutSecond = heartBeatTimeoutSecond;
		return this;
	}

	public int getMaxFrameLength() {
		return maxFrameLength;
	}

	public NettyServer setMaxFrameLength(int maxFrameLength) {
		this.maxFrameLength = maxFrameLength;
		return this;
	}

	public RmiBeanFactory getRmiBeanFactory() {
		return rmiBeanFactory;
	}

	public NettyServer setRmiBeanFactory(RmiBeanFactory rmiBeanFactory) {
		this.rmiBeanFactory = rmiBeanFactory;
		return this;
	}

	public MessageHandle<Object, Object, Object> getMessageHandle() {
		return messageHandle;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public NettyServer setMessageHandle(MessageHandle messageHandle) {
		this.messageHandle = messageHandle;
		return this;
	}

	public static void main(String[] args) throws Throwable {
		RmiBeanFactory rmiBeanFactory = DefaultRmiBeanFactory.newInstance();
		rmiBeanFactory.init(CollectionUtil.newInstance().toMap("scan", "com.cheuks.bin.original.reflect"));
		NettyServer ns = new NettyServer();
		NettyHandleServiceFactory handleServiceFactory = NettyHandleServiceFactory.newInstance(5);

		ns.setPoolSize(15).setMessageHandle(handleServiceFactory).setPort(10087).setRmiBeanFactory(rmiBeanFactory).setCacheSerialize(new FstCacheSerialize());
		ns.run();
	}
}
