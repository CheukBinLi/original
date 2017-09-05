package com.cheuks.bin.original.rmi.net.netty;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.RmiBeanFactory;
import com.cheuks.bin.original.common.util.AbstractObjectPool;
import com.cheuks.bin.original.rmi.net.RmiClient;

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

/***
 * 默认多线程客户端池实现
 * 
 * @author ben
 *
 */
public class NettyClient extends AbstractObjectPool<NettyClientHandle, InetSocketAddress> implements RmiClient<String, Void> {
	private static final Logger LOG = LoggerFactory.getLogger(NettyClient.class);

	private static NettyClient instance;

	public static NettyClient getInstance() {
		return instance;
	}

	// private String address = "127.0.0.1:10086";

	private Map<String, String> connectionInfo = new ConcurrentSkipListMap<String, String>();// key:连接上的服务器ID
																								// value:应用唯一ID

	public static final AttributeKey<NettyClient> NETTY_CLIENT_OBJECT_POOL = AttributeKey.valueOf("NETTY_CLIENT_OBJECT_POOL");

	private int maxActiveCount = 15;

	private int heartBeatInterval = 60;

	private int maxFrameLength = 5000;

	private LoadBalanceFactory<String, Void> loadBalanceFactory;

	private RmiBeanFactory rmiBeanFactory;

	// 连接任务
	private Thread connectionWorker = null;

	private final BlockingQueue<String> connectionQueue = new LinkedBlockingQueue<String>();

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
				throw new NullPointerException("rmiBeanFactory is null");
			}
			rmiBeanFactory.start(null);
			if (null == loadBalanceFactory) {
				throw new NullPointerException("can't instance loadBalanceFactory.");
			}
			loadBalanceFactory.init();

			client = new Bootstrap();
			worker = new NioEventLoopGroup(maxActiveCount);
			client.group(worker).option(ChannelOption.TCP_NODELAY, true).channel(NioSocketChannel.class);
			client.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new NettyMessageDecoder(maxFrameLength, 4, 4, cacheSerialize));
					ch.pipeline().addLast(new NettyMessageEncoder(cacheSerialize));
					ch.pipeline().addLast(new IdleStateHandler(0, 0, heartBeatInterval));
					ch.pipeline().addLast(new NettyClientHandle(loadBalanceFactory));
				}
			}).attr(NETTY_CLIENT_OBJECT_POOL, this);
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

	public void start() throws NumberFormatException, IllegalStateException, UnsupportedOperationException, InterruptedException, Exception {
		if (null == connectionWorker || connectionWorker.interrupted()) {
			init();
			connectionWorker = new Thread(new Runnable() {
				public void run() {
					try {
						while (true) {
							connect(connectionQueue.take());

						}
					} catch (Throwable e) {
						LOG.error(null, e);
					}
				}
			});
			connectionWorker.start();
		}
	}

	public void addConnection(String applicationCode) {
		connectionQueue.add(applicationCode);
	}

	public void addConnectionByServerName(String serverName) {
		connectionQueue.add(connectionInfo.get(serverName));
	}

	private void connect(String applicationCode) throws Throwable {
		try {
			String[] address = loadBalanceFactory.getResourceAndUseRegistration(applicationCode).split(":");
			InetSocketAddress inetSocketAddress = new InetSocketAddress(address[0], Integer.valueOf(address[1]));
			System.out.println(inetSocketAddress.getPort());
			client.connect(inetSocketAddress).addListener(new SimpleChannelFutureListener(applicationCode, this)).sync().channel();
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

	public NettyClient setHeartBeatTimeoutSecond(int heartBeatTimeoutSecond) {
		this.heartBeatInterval = heartBeatTimeoutSecond * 1000;
		return this;
	}

	public NettyClient setRmiBeanFactory(RmiBeanFactory rmiBeanFactory) {
		this.rmiBeanFactory = rmiBeanFactory;
		return this;
	}

	public NettyClient setMaxFrameLength(int maxFrameLength) {
		this.maxFrameLength = maxFrameLength;
		return this;
	}

	public NettyClient setHandleThreads(int handleThreads) {
		return null;
	}

	public NettyClient setScanPath(String path) {
		return null;
	}

	public NettyClient setLoadBalanceFactory(LoadBalanceFactory<String, Void> loadBalanceFactory) {
		this.loadBalanceFactory = loadBalanceFactory;
		return this;
	}

}
