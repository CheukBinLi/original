package com.cheuks.bin.original.rmi.net.netty.server;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.RmiBeanFactory;
import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel.ServiceType;
import com.cheuks.bin.original.common.rmi.net.MessageHandleFactory;
import com.cheuks.bin.original.rmi.config.RmiConfigArg;
import com.cheuks.bin.original.rmi.config.ServiceGroupConfig.ServiceGroupModel;
import com.cheuks.bin.original.rmi.net.ZookeeperLoadBalanceFactory;
import com.cheuks.bin.original.rmi.net.netty.NettyMessageDecoder;
import com.cheuks.bin.original.rmi.net.netty.NettyMessageEncoder;
import com.cheuks.bin.original.rmi.net.netty.message.NettyHearBeatServiceHandle;
import com.cheuks.bin.original.rmi.net.netty.message.RmiServiceHandle;
import com.cheuks.bin.original.rmi.net.netty.message.handle.HandleService;

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

@SuppressWarnings({"rawtypes", "unchecked", "static-access"})
public class NettyServer implements RmiContant {

	private static final Logger LOG = LoggerFactory.getLogger(NettyServer.class);
	private Thread task;

	private CacheSerialize cacheSerialize;
	private RmiConfigArg rmiConfigArg;
	private RmiBeanFactory<RmiConfigArg, Boolean> rmiBeanFactory;
	private MessageHandleFactory messageHandleFactory;
	private LoadBalanceFactory<String, Void> loadBalanceFactory;

	private Thread work;

	public synchronized void start() throws InterruptedException, NullPointerException {
		if (null == task || !task.isInterrupted()) {
			try {
				int poolSize = rmiConfigArg.getProtocolModel().getHandleThreads();
				if (poolSize < 0) {
					poolSize = Runtime.getRuntime().availableProcessors() * 2;
				}
				init(poolSize);
			} catch (Throwable e) {
				LOG.error("NettyServer.class -method: run()", e);
			}
		}
	}

	private void init(final int poolSize) throws Throwable {
		if (null == work || work.interrupted()) {

			if (LOG.isDebugEnabled())
				LOG.info("server is start.");
			if (null == rmiBeanFactory) {
				// rmiBeanFactory = new SimpleRmiBeanFactory();
				throw new NullPointerException("rmiBeanFactory is null");
			}
			// 参数
			rmiBeanFactory.start(rmiConfigArg, true);

			if (null == cacheSerialize)
				cacheSerialize = new FstCacheSerialize();
			if (null == messageHandleFactory) {
				messageHandleFactory = new HandleService();
				messageHandleFactory.start(rmiConfigArg.getProtocolModel().getHandleThreads() < 0 ? Runtime.getRuntime().availableProcessors() * 2 : rmiConfigArg.getProtocolModel().getHandleThreads());
			}
			// messageHandleFactory = NettyHandleServiceFactory.newInstance(handleThreads);
			// throw new NullPointerException("messageHandle is
			// null");
			// 心跳
			messageHandleFactory.registrationMessageHandle(RMI_SERVICE_TYPE_HEAR_BEAT, new NettyHearBeatServiceHandle());
			if (!messageHandleFactory.serviceTypeContains(RMI_SERVICE_TYPE_REQUEST)) {
				// RMI服务
				messageHandleFactory.registrationMessageHandle(RMI_SERVICE_TYPE_REQUEST, new RmiServiceHandle(rmiBeanFactory));
			}
			// 注解目录
			if (null == loadBalanceFactory) {
				loadBalanceFactory = new ZookeeperLoadBalanceFactory();
				loadBalanceFactory.setUrl(rmiConfigArg.getRegistryModel().getServerAddress());
				loadBalanceFactory.init();
				// throw new NullPointerException("loadBalanceFactory is null");
				// loadBalanceFactory = new ZookeeperRegistrationFactory(zookeeperServerList, baseSleepTimeMs, maxRetries);
			}
			/***
			 * 注册服务
			 */
			RegisterLoadBalanceModel registerLoadBalanceModel = new RegisterLoadBalanceModel();
			for (Entry<String, ServiceGroupModel> en : rmiConfigArg.getServiceGroup().getServiceGroupConfig().entrySet()) {
				// 拥有的服务
				registerLoadBalanceModel.setServiceName(en.getKey()).setType(ServiceType.server);
				// 服务器主机名
				registerLoadBalanceModel.setServerName(rmiConfigArg.getProtocolModel().getLocalName());
				// 服务地址
				registerLoadBalanceModel.setUrl(rmiConfigArg.getProtocolModel().getLocalAddress() + ":" + rmiConfigArg.getProtocolModel().getPort());
				// 健康
				registerLoadBalanceModel.setHealthCheck(rmiConfigArg.getProtocolModel().getLocalAddress() + ":" + rmiConfigArg.getProtocolModel().getPort());
				loadBalanceFactory.registration(registerLoadBalanceModel);
			}
			work = new Thread(new Runnable() {

				public void run() {
					try {
						final EventLoopGroup bossGroup = new NioEventLoopGroup(poolSize);
						final EventLoopGroup workerGroup = new NioEventLoopGroup(poolSize);
						try {
							ServerBootstrap server = new ServerBootstrap();
							server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
								@Override
								public void initChannel(SocketChannel ch) throws Exception {
									// 注册handler
									ch.pipeline().addLast(new NettyMessageDecoder(rmiConfigArg.getProtocolModel().getFrameLength(), 4, 4, cacheSerialize));
									ch.pipeline().addLast(new NettyMessageEncoder(cacheSerialize));
									ch.pipeline().addLast(new IdleStateHandler(rmiConfigArg.getProtocolModel().getHeartbeat(), 0, 0));
									// 服务处理
									ch.pipeline().addLast(new NettyServerHandle(messageHandleFactory));
								}
							});
							server.bind(rmiConfigArg.getProtocolModel().getPort()).sync().channel().closeFuture().sync();
							LOG.warn("service is close.");

						} finally {
							workerGroup.shutdownGracefully();
							bossGroup.shutdownGracefully();
						}
					} catch (Throwable e) {
						LOG.error(null, e);
					}
				}
			});
			work.start();
		}

	}

	public NettyServer() {
		super();
	}

	public NettyServer(RmiConfigArg rmiConfigArg, RmiBeanFactory rmiBeanFactory, MessageHandleFactory<Object, Object, Object> messageHandleFactory) {
		super();
		this.rmiConfigArg = rmiConfigArg;
		this.rmiBeanFactory = rmiBeanFactory;
		this.messageHandleFactory = messageHandleFactory;
	}

	public CacheSerialize getCacheSerialize() {
		return cacheSerialize;
	}

	public NettyServer setCacheSerialize(CacheSerialize cacheSerialize) {
		this.cacheSerialize = cacheSerialize;
		return this;
	}

	public RmiConfigArg getRmiConfigArg() {
		return rmiConfigArg;
	}

	public NettyServer setRmiConfigArg(RmiConfigArg rmiConfigArg) {
		this.rmiConfigArg = rmiConfigArg;
		return this;
	}

	public RmiBeanFactory<RmiConfigArg, Boolean> getRmiBeanFactory() {
		return rmiBeanFactory;
	}

	public NettyServer setRmiBeanFactory(RmiBeanFactory<RmiConfigArg, Boolean> rmiBeanFactory) {
		this.rmiBeanFactory = rmiBeanFactory;
		return this;
	}

	public MessageHandleFactory getMessageHandleFactory() {
		return messageHandleFactory;
	}

	public NettyServer setMessageHandleFactory(MessageHandleFactory messageHandleFactory) {
		this.messageHandleFactory = messageHandleFactory;
		return this;
	}

	public LoadBalanceFactory<String, Void> getLoadBalanceFactory() {
		return loadBalanceFactory;
	}

	public NettyServer setLoadBalanceFactory(LoadBalanceFactory<String, Void> loadBalanceFactory) {
		this.loadBalanceFactory = loadBalanceFactory;
		return this;
	}

	public static void main(String[] args) throws Throwable {
		// RmiBeanFactory rmiBeanFactory = DefaultRmiBeanFactory.newInstance();
		// rmiBeanFactory.init(CollectionUtil.newInstance().toMap("scan", "com.cheuks.bin.original.reflect"));
		// NettyServer ns = new NettyServer();
		// NettyHandleServiceFactory handleServiceFactory = NettyHandleServiceFactory.newInstance(8);
		//
		// ns.setPoolSize(5).setMessageHandle(handleServiceFactory).setPort(10087).setRmiBeanFactory(rmiBeanFactory).setCacheSerialize(new FstCacheSerialize());
		// ns.run();

		// NettyServer ns = new NettyServer();
		//
		// RegistrationFactory register = new
		// ZookeeperRegistrationFactory(ns.getZookeeperServerList(),
		// ns.baseSleepTimeMs, ns.maxRetries);
		// register.init();
		// register.createService("/abcdefg", new
		// RegistrationEventListener<PathChildrenCacheEvent>() {
		// public void nodeChanged(PathChildrenCacheEvent params) throws
		// Exception {
		// System.err.println("createService:" + new
		// String(params.getData().getData()));
		// }
		// });

	}
}
