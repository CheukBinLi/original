package com.cheuks.bin.original.reflect.rmi;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;
import com.cheuks.bin.original.common.util.CollectionUtil;
import com.cheuks.bin.original.reflect.rmi.net.MessageHandle;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyHandleServiceFactory;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyHearBeatServiceHandle;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyMessageDecoder;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyMessageEncoder;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyServerHandle;
import com.cheuks.bin.original.reflect.rmi.net.netty.RmiServiceHandle;
import com.cheuks.bin.original.registration.center.ZookeeperRegistrationFactory;

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

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NettyServer {

	private static final Logger LOG = LoggerFactory.getLogger(NettyServer.class);
	private Thread task;

	private CacheSerialize cacheSerialize;
	private int port = 10086;
	private int poolSize = 15;
	private int heartBeatTimeoutSecond = 120;
	private RmiBeanFactory rmiBeanFactory;
	private int maxFrameLength = 5000;
	private int handleThreads = 10;
	private String scanPath;
	private String zookeeperServerList = "127.0.0.1:2181";
	private String applicationName;
	private String applicationUrl;
	private int baseSleepTimeMs = 5000;
	private int maxRetries = 20;
	private boolean isServer;
	private boolean isClient;

	private MessageHandle messageHandle;

	private RegistrationFactory registrationFactory;

	private RegisterServiceServerHandler registerServiceHandler;

	public synchronized void run() throws InterruptedException, NullPointerException {
		if (null == task || !task.isInterrupted()) {
			task = new Thread(new Runnable() {
				public void run() {
					try {
						start(poolSize);
					} catch (Throwable e) {
						LOG.error("NettyServer.class -method: run()", e);
					}
				}
			});
			task.start();
		}
	}

	private void start(int poolSize) throws Throwable {
		if (LOG.isDebugEnabled())
			LOG.info("server is start.");
		if (null == rmiBeanFactory) {
			rmiBeanFactory = new SimpleRmiBeanFactory();
			// throw new NullPointerException("rmiBeanFactory is null");
		}
		rmiBeanFactory.init(CollectionUtil.newInstance().toMap("scan", scanPath));

		if (null == cacheSerialize)
			cacheSerialize = new FstCacheSerialize();
		if (null == messageHandle)
			messageHandle = NettyHandleServiceFactory.newInstance(handleThreads);
		// throw new NullPointerException("messageHandle is null");
		// 心跳
		messageHandle.addHandle(MessageHandle.HEAR_BEAT, new NettyHearBeatServiceHandle());
		if (!messageHandle.handleContains(MessageHandle.RMI_REQUEST)) {
			// RMI服务
			messageHandle.addHandle(MessageHandle.RMI_REQUEST, new RmiServiceHandle(rmiBeanFactory));
		}
		// 注解目录
		if (null == registrationFactory) {
			registrationFactory = new ZookeeperRegistrationFactory(zookeeperServerList, baseSleepTimeMs, maxRetries);
		}
		if (null == applicationUrl) {
			applicationUrl = InetAddress.getLocalHost().getHostAddress();
		}
		if (null == registerServiceHandler) {
			registrationFactory.setUrl(zookeeperServerList);
			registerServiceHandler = new RegisterServiceServerHandler(applicationName, applicationUrl + ":" + port, registrationFactory);
		}
		registerServiceHandler.register();
		// registrationFactory.init();
		// registrationFactory.createService("/aa", null);
		// registrationFactory.register("/aa", "/" + InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress() + ":" + this.port, null);

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

	public MessageHandle getMessageHandle() {
		return messageHandle;
	}

	public NettyServer setMessageHandle(MessageHandle messageHandle) {
		this.messageHandle = messageHandle;
		return this;
	}

	public int getHandleThreads() {
		return handleThreads;
	}

	public NettyServer setHandleThreads(int handleThreads) {
		this.handleThreads = handleThreads;
		return this;
	}

	public String getScanPath() {
		return scanPath;
	}

	public NettyServer setScanPath(String scanPath) {
		this.scanPath = scanPath;
		return this;
	}

	public String getZookeeperServerList() {
		return zookeeperServerList;
	}

	public NettyServer setZookeeperServerList(String zookeeperServerList) {
		this.zookeeperServerList = zookeeperServerList;
		return this;
	}

	public int getBaseSleepTimeMs() {
		return baseSleepTimeMs;
	}

	public NettyServer setBaseSleepTimeMs(int baseSleepTimeMs) {
		this.baseSleepTimeMs = baseSleepTimeMs;
		return this;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public NettyServer setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
		return this;
	}

	public RegistrationFactory getRegistrationFactory() {
		return registrationFactory;
	}

	public NettyServer setRegistrationFactory(RegistrationFactory registrationFactory) {
		this.registrationFactory = registrationFactory;
		return this;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public NettyServer setApplicationName(String applicationName) {
		this.applicationName = applicationName;
		return this;
	}

	public String getApplicationUrl() {
		return applicationUrl;
	}

	public NettyServer setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
		return this;
	}

	public boolean isServer() {
		return isServer;
	}

	public NettyServer setIsServer(boolean isServer) {
		this.isServer = isServer;
		return this;
	}

	public boolean isClient() {
		return isClient;
	}

	public NettyServer setIsClient(boolean isClient) {
		this.isClient = isClient;
		return this;
	}

	public RegisterServiceServerHandler getRegisterServiceHandler() {
		return registerServiceHandler;
	}

	public NettyServer setRegisterServiceHandler(RegisterServiceServerHandler registerServiceHandler) {
		this.registerServiceHandler = registerServiceHandler;
		return this;
	}

	public static void main(String[] args) throws Throwable {
		RmiBeanFactory rmiBeanFactory = DefaultRmiBeanFactory.newInstance();
		rmiBeanFactory.init(CollectionUtil.newInstance().toMap("scan", "com.cheuks.bin.original.reflect"));
		NettyServer ns = new NettyServer();
		NettyHandleServiceFactory handleServiceFactory = NettyHandleServiceFactory.newInstance(8);

		ns.setPoolSize(5).setMessageHandle(handleServiceFactory).setPort(10087).setRmiBeanFactory(rmiBeanFactory).setCacheSerialize(new FstCacheSerialize());
		ns.run();

		// NettyServer ns = new NettyServer();
		//
		// RegistrationFactory register = new ZookeeperRegistrationFactory(ns.getZookeeperServerList(), ns.baseSleepTimeMs, ns.maxRetries);
		// register.init();
		// register.createService("/abcdefg", new RegistrationEventListener<PathChildrenCacheEvent>() {
		// public void nodeChanged(PathChildrenCacheEvent params) throws Exception {
		// System.err.println("createService:" + new String(params.getData().getData()));
		// }
		// });

	}
}
