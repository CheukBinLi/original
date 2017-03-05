package com.cheuks.bin.original.reflect.rmi;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;
import com.cheuks.bin.original.common.util.AbstractObjectPool;
import com.cheuks.bin.original.common.util.CollectionUtil;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyClientHandle;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyMessageDecoder;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyMessageEncoder;
import com.cheuks.bin.original.registration.center.ZookeeperRegistrationFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/***
 * 默认多线程客户端池实现
 * 
 * @author ben
 *
 */
public class NettyClient extends AbstractObjectPool<NettyClientHandle, InetSocketAddress> {

	private static NettyClient instance;

	public static NettyClient getInstance() {
		return instance;
	}

	private static final Logger LOG = LoggerFactory.getLogger(NettyClient.class);

	private String address = "127.0.0.1:10086";
	private List<InetSocketAddress> inetSocketAddress = new ArrayList<InetSocketAddress>();
	public static final AttributeKey<NettyClient> NETTY_CLIENT_OBJECT_POOL = AttributeKey.valueOf("NETTY_CLIENT_OBJECT_POOL");
	private int maxActiveCount = 15;
	private int heartBeatInterval = 60;
	private int maxFrameLength = 5000;
	private String applicationName;
	private String zookeeperServerList = "127.0.0.1:2181";
	private int baseSleepTimeMs = 5000;
	private int maxRetries = 20;
	private RegistrationFactory registrationFactory = null;
	private RegisterService registerClientHandler;
	private RmiBeanFactory rmiBeanFactory;
	private String scanPath;
	private Thread work = null;

	private volatile long changeServerTime = System.currentTimeMillis();

	private volatile boolean isInit;

	private Bootstrap client;
	private EventLoopGroup worker;

	private CacheSerialize cacheSerialize;

	public NettyClient(int poolSize) {
		super(poolSize);
		this.maxActiveCount = poolSize > 0 ? poolSize : this.maxActiveCount;
	}

	public NettyClient() {
		this(Runtime.getRuntime().availableProcessors() * 2);
	}

	private void init() {
		if (isInit)
			return;
		isInit = true;
		try {
			if (null == cacheSerialize)
				cacheSerialize = new FstCacheSerialize();
			if (null == rmiBeanFactory) {
				// rmiBeanFactory = new SimpleRmiBeanFactory();
				throw new NullPointerException("rmiBeanFactory is null");
			}
			rmiBeanFactory.init(CollectionUtil.newInstance().toMap("scan", scanPath, "isServer", false));
			if (null == registrationFactory)
				registrationFactory = new ZookeeperRegistrationFactory(zookeeperServerList, baseSleepTimeMs,
						maxRetries);
			registrationFactory.init();
			if (null == registerClientHandler)
				registerClientHandler = new RegisterServiceClientHandler(applicationName, registrationFactory);
			address = registerClientHandler.register();

			client = new Bootstrap();
			worker = new NioEventLoopGroup(maxActiveCount);
			client.group(worker).option(ChannelOption.TCP_NODELAY, true).channel(NioSocketChannel.class);
			client.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new NettyMessageDecoder(maxFrameLength, 4, 4, cacheSerialize));
					ch.pipeline().addLast(new NettyMessageEncoder(cacheSerialize));
					ch.pipeline().addLast(new IdleStateHandler(0, 0, heartBeatInterval));
					ch.pipeline().addLast(new NettyClientHandle());
				}
			}).attr(NETTY_CLIENT_OBJECT_POOL, this);
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

	public void start() throws NumberFormatException, IllegalStateException, UnsupportedOperationException,
			InterruptedException, Exception {
		if (null == work || work.interrupted()) {
			work = new Thread(new Runnable() {
				public void run() {
					init();
					if (LOG.isDebugEnabled())
						LOG.debug(address);
					StringTokenizer ipList = new StringTokenizer(address, ",");
					StringTokenizer ip;
					String temp;
					String host;
					int port;
					InetSocketAddress socketAddress;
					int count = maxActiveCount / ipList.countTokens();
					while (ipList.hasMoreTokens()) {
						temp = ipList.nextToken();
						ip = new StringTokenizer(temp, ":");
						host = ip.nextToken();
						port = Integer.valueOf(ip.nextToken());
						inetSocketAddress.add(socketAddress = new InetSocketAddress(host, port));
						for (int i = 0; i < count; i++) {
							try {
								addObject(socketAddress);
							} catch (Exception e) {
								LOG.error(null, e);
							}
						}
					}
				}
			});
			work.start();
		}
	}

	public void addObject(final InetSocketAddress socketAddress)
			throws IllegalStateException, UnsupportedOperationException, Exception {
		// Channel channel = client.connect(socketAddress).sync().channel();
		// super.addObject(channel);
		// client.connect(socketAddress).sync().channel();
		// TODO Auto-generated method stub
		try {
			client.connect(socketAddress).sync().addListener(new GenericFutureListener<Future<? super Void>>() {

				public void operationComplete(Future<? super Void> future) throws Exception {
					System.out.println("是否成功:"+future.isSuccess());
					if (!future.isSuccess()) {
						System.out.println("掉线更换服务器");
					}
				}
			}).channel();
		} catch (InterruptedException e) {
			LOG.error(null, e);
		}

	}

	@Override
	public void invalidateReBuildObject(int count) throws Exception {
		// 一共过期N个对象
		LOG.info("一共过期" + count + "个对象。");
	}

	public void invalidateObject(NettyClientHandle t) throws Exception {
		destroyObject(t);
	}

	public void destroyObject(NettyClientHandle t) throws Exception {
		ChannelHandlerContext ctx = t.getObject().getChannelHandlerContext();
		ctx.close();
		ctx.channel().close();
	}

	public void shutdown() {
		super.shutdown();
		worker.shutdownGracefully();
	}

	public String getAddress() {
		return address;
	}

	public NettyClient setAddress(String address) {
		this.address = address;
		return this;
	}

	public int getMaxActiveCount() {
		return maxActiveCount;
	}

	public NettyClient setMaxActiveCount(int maxActiveCount) {
		this.maxActiveCount = maxActiveCount;
		return this;
	}

	public CacheSerialize getCacheSerialize() {
		return cacheSerialize;
	}

	public NettyClient setCacheSerialize(CacheSerialize cacheSerialize) {
		this.cacheSerialize = cacheSerialize;
		return this;
	}

	public int getHeartBeatInterval() {
		return heartBeatInterval;
	}

	public NettyClient setHeartBeatInterval(int heartBeatInterval) {
		this.heartBeatInterval = heartBeatInterval;
		return this;
	}

	public int getMaxFrameLength() {
		return maxFrameLength;
	}

	public NettyClient setMaxFrameLength(int maxFrameLength) {
		this.maxFrameLength = maxFrameLength;
		return this;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getZookeeperServerList() {
		return zookeeperServerList;
	}

	public void setZookeeperServerList(String zookeeperServerList) {
		this.zookeeperServerList = zookeeperServerList;
	}

	public int getBaseSleepTimeMs() {
		return baseSleepTimeMs;
	}

	public void setBaseSleepTimeMs(int baseSleepTimeMs) {
		this.baseSleepTimeMs = baseSleepTimeMs;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public RegistrationFactory getRegistrationFactory() {
		return registrationFactory;
	}

	public void setRegistrationFactory(RegistrationFactory registrationFactory) {
		this.registrationFactory = registrationFactory;
	}

	public RegisterService getRegisterClientHandler() {
		return registerClientHandler;
	}

	public void setRegisterClientHandler(RegisterService registerClientHandler) {
		this.registerClientHandler = registerClientHandler;
	}

	public RmiBeanFactory getRmiBeanFactory() {
		return rmiBeanFactory;
	}

	public void setRmiBeanFactory(RmiBeanFactory rmiBeanFactory) {
		this.rmiBeanFactory = rmiBeanFactory;
	}

	public String getScanPath() {
		return scanPath;
	}

	public void setScanPath(String scanPath) {
		this.scanPath = scanPath;
	}

}
