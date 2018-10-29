package com.cheuks.bin.original.anything.test.net.im.server;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.RmiBeanFactory;
import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel.ServiceType;
import com.cheuks.bin.original.common.rmi.net.MessageHandleFactory;
import com.cheuks.bin.original.rmi.config.RmiConfig.RmiConfigGroup;
import com.cheuks.bin.original.rmi.config.ServiceGroupConfig.ServiceGroupModel;
import com.cheuks.bin.original.rmi.net.netty.NettyMessageDecoder;
import com.cheuks.bin.original.rmi.net.netty.NettyMessageEncoder;
import com.cheuks.bin.original.rmi.net.netty.message.NettyHearBeatServiceHandle;
import com.cheuks.bin.original.rmi.net.netty.message.RmiServiceHandle;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
public class NettyServer implements RmiContant {

	private static final Logger LOG = LoggerFactory.getLogger(NettyServer.class);
	private Thread uploadLoadInfotask;
	private int port = 10010;
	private Thread work;

	public synchronized void start() throws InterruptedException, NullPointerException {

		//		try {
		//			if (rmiConfigGroup.getProtocolModel().getNetWorkThreads() < 0) {
		//				rmiConfigGroup.getProtocolModel().setNetWorkThreads(Runtime.getRuntime().availableProcessors() * 2);
		//			}
		//			if (rmiConfigGroup.getProtocolModel().getHandleThreads() < 0) {
		//				rmiConfigGroup.getProtocolModel().setHandleThreads(Runtime.getRuntime().availableProcessors() * 2);
		//			}
		//			initInfo();
		//			init(rmiConfigGroup.getProtocolModel().getNetWorkThreads());
		//		} catch (Throwable e) {
		//			LOG.error("NettyServer.class -method: run()", e);
		//		}
	}

	private void init(final int poolSize) throws Throwable {
		work = new Thread(new Runnable() {

			public void run() {
				try {
					final EventLoopGroup bossGroup = new NioEventLoopGroup(poolSize);
					final EventLoopGroup workerGroup = new NioEventLoopGroup(poolSize);
					try {
						ServerBootstrap server = new ServerBootstrap();
						server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<SocketChannel>() {
							@Override
							public void initChannel(SocketChannel ch) throws Exception {
								// 注册handler
								ch.pipeline().addLast(new MessageDecoder(rmiConfigGroup.getProtocolModel().getFrameLength(), 4, 4, cacheSerialize));
								ch.pipeline().addLast(new NettyMessageEncoder(cacheSerialize));
								ch.pipeline().addLast(new IdleStateHandler(rmiConfigGroup.getProtocolModel().getHeartbeat(), 0, 0));
								ch.pipeline().addLast(new NettyServerHandle(NettyServer.this, messageHandleFactory));
								// ch.pipeline().addLast("logging", new LoggingHandler(LogLevel.DEBUG));
							}

						});
						server.bind(port).sync().channel().closeFuture().sync();
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

	/***
	 * 
	 * @param value
	 *            基数加/减 value
	 */
	public void modifyConnectionCount(int value) {
		connectionCount.addAndGet(value);
		synchronized (uploadLoadInfotask) {
			uploadLoadInfotask.notify();
		}
	}

	void initInfo() {
		LOG.info("rmi server info.");
		LOG.info("registration center address:{}", rmiConfigGroup.getRegistryModel().getServerAddress());
		LOG.info("registration name:{}", rmiConfigGroup.getProtocolModel().getLocalName());
		LOG.info("network address:{}", rmiConfigGroup.getProtocolModel().getLocalAddress());
		LOG.info("network frame max size:{}", (rmiConfigGroup.getProtocolModel().getFrameLength() / 1024) + "KB");
		LOG.info("network access thread:{} , message handle thread:{}", rmiConfigGroup.getProtocolModel().getNetWorkThreads(), rmiConfigGroup.getProtocolModel().getHandleThreads());
	}

	public NettyServer() {
		super();
	}

	public NettyServer(RmiConfigGroup rmiConfigGroup, RmiBeanFactory rmiBeanFactory, MessageHandleFactory<Object, Object, Object> messageHandleFactory) {
		super();
		this.rmiConfigGroup = rmiConfigGroup;
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

	public RmiConfigGroup getRmiConfigGroup() {
		return this.rmiConfigGroup;
	}

	public NettyServer setRmiConfigGroup(RmiConfigGroup rmiConfigGroup) {
		this.rmiConfigGroup = rmiConfigGroup;
		return this;
	}

	public RmiBeanFactory getRmiBeanFactory() {
		return rmiBeanFactory;
	}

	public NettyServer setRmiBeanFactory(RmiBeanFactory rmiBeanFactory) {
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

}
