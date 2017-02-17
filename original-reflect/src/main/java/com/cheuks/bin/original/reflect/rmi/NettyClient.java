package com.cheuks.bin.original.reflect.rmi;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.util.AbstractObjectPool;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyClientHandle;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyMessageDecoder;
import com.cheuks.bin.original.reflect.rmi.net.netty.NettyMessageEncoder;

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

	private volatile boolean isInit;

	private Bootstrap client;
	private EventLoopGroup worker;

	private CacheSerialize cacheSerialize;

	public NettyClient(int poolSize) {
		super(poolSize);
		this.maxActiveCount = poolSize > 0 ? poolSize : this.maxActiveCount;
		init();
	}

	public NettyClient() {
		this(-1);
	}

	private void init() {
		if (isInit)
			return;
		isInit = true;
		if (null == cacheSerialize)
			cacheSerialize = new FstCacheSerialize();
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
	}

	public void start() throws NumberFormatException, IllegalStateException, UnsupportedOperationException, InterruptedException, Exception {
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
				addObject(socketAddress);
			}
		}
	}

	public void addObject(final InetSocketAddress socketAddress) throws IllegalStateException, UnsupportedOperationException, Exception {
		// Channel channel = client.connect(socketAddress).sync().channel();
		// super.addObject(channel);
		// client.connect(socketAddress).sync().channel();
		client.connect(socketAddress).sync().addListener(new GenericFutureListener<Future<? super Void>>() {

			public void operationComplete(Future<? super Void> future) throws Exception {

			}
		}).channel();
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
}
