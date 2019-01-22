package com.cheuks.bin.original.reflect.rmi;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;
import com.cheuks.bin.original.common.util.conver.CollectionUtil;
import com.cheuks.bin.original.common.util.pool.AbstractObjectPool;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyClientHandle;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyMessageDecoder;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyMessageEncoder;
import com.cheuks.bin.original.registration.center.ZookeeperRegistrationFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

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
	//连接任务
	private Thread connectionWorker;
	private final BlockingQueue<Object> connectionQueue = new LinkedBlockingQueue<Object>();

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
			rmiBeanFactory.init(CollectionUtil.toMap("scan", scanPath, "isServer", false));
			if (null == registrationFactory)
				registrationFactory = new ZookeeperRegistrationFactory(zookeeperServerList, baseSleepTimeMs, maxRetries);
			registrationFactory.start();
			if (null == registerClientHandler)
				registerClientHandler = new RegisterServiceClientHandler(applicationName, registrationFactory);
			//			address = registerClientHandler.register();

			client = new Bootstrap();
			worker = new NioEventLoopGroup(maxActiveCount);
			client.group(worker).option(ChannelOption.TCP_NODELAY, true).channel(NioSocketChannel.class);
			client.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new NettyMessageDecoder(maxFrameLength, 4, 4, cacheSerialize));
					ch.pipeline().addLast(new NettyMessageEncoder(cacheSerialize));
					ch.pipeline().addLast(new IdleStateHandler(0, 0, heartBeatInterval));
					ch.pipeline().addLast(new NettyClientHandle(registerClientHandler));
				}
			}).attr(NETTY_CLIENT_OBJECT_POOL, this);
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

	public void start() throws NumberFormatException, IllegalStateException, UnsupportedOperationException, InterruptedException, Exception {
		if (null == connectionWorker || connectionWorker.interrupted()) {
			init();
			//			if (null == work || work.interrupted()) {
			//			work = new Thread(new Runnable() {
			//				public void run() {
			//					init();
			//					if (LOG.isDebugEnabled())
			//						LOG.debug(address);
			//					StringTokenizer ipList = new StringTokenizer(address, ",");
			//					StringTokenizer ip;
			//					String temp;
			//					String host;
			//					int port;
			//					InetSocketAddress socketAddress;
			//					int count = maxActiveCount / ipList.countTokens();
			//					//返回list列表的情况:改为每次请求负载
			//					//					while (ipList.hasMoreTokens()) {
			//					//						temp = ipList.nextToken();
			//					//						ip = new StringTokenizer(temp, ":");
			//					//						host = ip.nextToken();
			//					//						port = Integer.valueOf(ip.nextToken());
			//					//						inetSocketAddress.add(socketAddress = new InetSocketAddress(host, port));
			//					//						for (int i = 0; i < count; i++) {
			//					//							try {
			//					//								addObject(socketAddress);
			//					//							} catch (Exception e) {
			//					//								LOG.error(null, e);
			//					//							}
			//					//						}
			//					//					}
			//					String[] address = null;
			//					//测试改单个IP，不负载
			//					//					try {
			//					//						address = registerClientHandler.register().split(":");
			//					//					} catch (Throwable e1) {
			//					//						e1.printStackTrace();
			//					//					}
			//					for (int i = 0; i < count; i++) {
			//						try {
			//							address = registerClientHandler.register().split(":");
			//							addObject(new InetSocketAddress(address[0], Integer.valueOf(address[1])));
			//						} catch (Throwable e) {
			//							LOG.error(null, e);
			//						}
			//					}
			//				}
			//			});
			//			work.start();
			connectionWorker = new Thread(new Runnable() {
				public void run() {
					try {
						while (true) {
							connectionQueue.take();
							connect();

						}
					} catch (Throwable e) {
						LOG.error(null, e);
					}
				}
			});
			for (int i = 0; i < maxActiveCount; i++) {
				connectionQueue.add(1);
			}
			connectionWorker.start();
		}
	}

	public void addConnection() {
		connectionQueue.add(1);
	}

	private void connect() throws Throwable {
		try {
			String[] address = registerClientHandler.getRegisterDirectory().split(":");
			InetSocketAddress inetSocketAddress = new InetSocketAddress(address[0], Integer.valueOf(address[1]));
			System.out.println(inetSocketAddress.getPort());
			client.connect(inetSocketAddress).addListener(new ChannelFutureListener() {

				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						//发送负载信息
					} else {
						System.out.println("掉线更换服务器");
						try {
							addConnection();
						} catch (Throwable e) {
							e.printStackTrace();
						}

					}
				}
			}).sync().channel();
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

	@Override
	public synchronized void removeObject(NettyClientHandle t) throws Exception {
		super.removeObject(t);
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

	public boolean isFailure(NettyClientHandle t) throws Exception {
		return false;
	}

}
