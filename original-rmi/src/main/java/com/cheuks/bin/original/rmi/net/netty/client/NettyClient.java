package com.cheuks.bin.original.rmi.net.netty.client;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel.ServiceType;
import com.cheuks.bin.original.common.util.AbstractObjectPool;
import com.cheuks.bin.original.common.util.ObjectPoolManager;
import com.cheuks.bin.original.rmi.config.RmiConfigArg;
import com.cheuks.bin.original.rmi.model.ConsumerValueModel;
import com.cheuks.bin.original.rmi.net.ZookeeperLoadBalanceFactory;
import com.cheuks.bin.original.rmi.net.netty.NettyMessageDecoder;
import com.cheuks.bin.original.rmi.net.netty.NettyMessageEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/***
 * 默认客户端实现
 * 
 * @author ben
 *
 */
public class NettyClient {

	private static final Logger LOG = LoggerFactory.getLogger(NettyClient.class);

	private ObjectPoolManager<NettyClientHandle, InetSocketAddress> objectPoolManager;

	// session跟服务关联
	private Map<String, ConsumerValueModel> sessionInfo = new ConcurrentSkipListMap<String, ConsumerValueModel>();

	private RmiConfigArg rmiConfigArg;

	private LoadBalanceFactory<String, Void> loadBalanceFactory;

	private Bootstrap client;

	private EventLoopGroup worker;

	private CacheSerialize cacheSerialize;

	private volatile boolean isInit;

	public NettyClient() {
	}

	public void start() {
		if (isInit)
			return;
		isInit = true;
		try {
			if (null == cacheSerialize)
				cacheSerialize = new FstCacheSerialize();
			if (null == loadBalanceFactory) {
				/***
				 * @todo 根据协议区分
				 */
				loadBalanceFactory = new ZookeeperLoadBalanceFactory();
				loadBalanceFactory.setUrl(rmiConfigArg.getRegistryModel().getServerAddress());
				loadBalanceFactory.init();
			}
			if (null == objectPoolManager) {
				objectPoolManager = new ObjectPoolManager<NettyClientHandle, InetSocketAddress>();
			}
			if (null == rmiConfigArg)
				throw new NullPointerException("can't found rmiConfigArg instance.");
			client = new Bootstrap();
			int temp;
			worker = new NioEventLoopGroup((temp = rmiConfigArg.getProtocolModel().getNetWorkThreads()) > 0 ? temp : Runtime.getRuntime().availableProcessors() * 2);
			client.group(worker).option(ChannelOption.TCP_NODELAY, true).channel(NioSocketChannel.class);
			client.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new NettyMessageDecoder(rmiConfigArg.getProtocolModel().getFrameLength(), 4, 4, cacheSerialize));
					ch.pipeline().addLast(new NettyMessageEncoder(cacheSerialize));
					ch.pipeline().addLast(new IdleStateHandler(0, 0, rmiConfigArg.getProtocolModel().getHeartbeat()));
					ch.pipeline().addLast(new NettyClientHandle(NettyClient.this));
				}
			});
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

	public ObjectPoolManager<NettyClientHandle, InetSocketAddress> getObjectPoolManager() {
		return objectPoolManager;
	}

	public NettyClient setObjectPoolManager(ObjectPoolManager<NettyClientHandle, InetSocketAddress> objectPoolManager) {
		this.objectPoolManager = objectPoolManager;
		return this;
	}

	public Bootstrap getClient() {
		return client;
	}

	public NettyClient setClient(Bootstrap client) {
		this.client = client;
		return this;
	}

	public RmiConfigArg getRmiConfigArg() {
		return rmiConfigArg;
	}

	public NettyClient setRmiConfigArg(RmiConfigArg rmiConfigArg) {
		this.rmiConfigArg = rmiConfigArg;
		return this;
	}

	public CacheSerialize getCacheSerialize() {
		return cacheSerialize;
	}

	public NettyClient setCacheSerialize(CacheSerialize cacheSerialize) {
		this.cacheSerialize = cacheSerialize;
		return this;
	}

	public LoadBalanceFactory<String, Void> getLoadBalanceFactory() {
		return loadBalanceFactory;
	}

	public NettyClient setLoadBalanceFactory(LoadBalanceFactory<String, Void> loadBalanceFactory) {
		this.loadBalanceFactory = loadBalanceFactory;
		return this;
	}

	/***
	 * 连接完城
	 * 
	 * @param channel
	 * @throws Throwable
	 */
	public void operationComplete(Channel channel, String serverName, String serviceName, String serverUrl, String consumerName, String consumerUrl) throws Throwable {
		sessionInfo.put(channel.id().asLongText(), new ConsumerValueModel(serverName, serviceName));
		// 注册
		RegisterLoadBalanceModel registerLoadBalanceModel = new RegisterLoadBalanceModel();
		registerLoadBalanceModel.setType(ServiceType.client);
		registerLoadBalanceModel.setServerName(serverName);
		registerLoadBalanceModel.setServiceName(serviceName);
		registerLoadBalanceModel.setUrl(serverUrl);
		registerLoadBalanceModel.setValue(consumerName);
		registerLoadBalanceModel.setDesc(consumerUrl);
		loadBalanceFactory.useRegistration(registerLoadBalanceModel);
	}

	/***
	 * 获取服务连接信息
	 * 
	 * @param channel
	 * @return
	 */
	public ConsumerValueModel getServerInfo(Channel channel) {
		return sessionInfo.get(channel.id().asLongText());
	}
	public ConsumerValueModel removeServerInfo(Channel channel) {
		return sessionInfo.remove(channel.id().asLongText());
	}

	/***
	 * 连接完成，向对象池添加，worker
	 * 
	 * @param nettyClientHandle
	 * @throws Throwable
	 */
	public synchronized void addWorker(NettyClientHandle nettyClientHandle) throws Throwable {
		ConsumerValueModel serverInfo = getServerInfo(nettyClientHandle.getChannelHandlerContext().channel());
		if (null == serverInfo) {
			throw new NullPointerException("can't found sessionId's value.");
		} else {
			AbstractObjectPool<NettyClientHandle, InetSocketAddress> pool = objectPoolManager.getPool(serverInfo.getServiceName());
			NettyClientPool nettyClientPool;
			if (null == pool) {
				nettyClientPool = new NettyClientPool(this, loadBalanceFactory, rmiConfigArg, -1, serverInfo.getServiceName());
				nettyClientPool.addObject(nettyClientHandle);
				objectPoolManager.addPool(nettyClientPool);
			} else {
				pool.addObject(nettyClientHandle);
			}
		}
	}

	/***
	 * 掉线更搞服务器
	 * 
	 * @param nettyClientHandle
	 * @throws Throwable
	 */
	public void changeServerConnection(NettyClientHandle nettyClientHandle) throws Throwable {
		ConsumerValueModel serverInfo = removeServerInfo(nettyClientHandle.getChannelHandlerContext().channel());
		if (null == serverInfo) {
			throw new NullPointerException("can't found sessionId's value.");
		} else {
			AbstractObjectPool<NettyClientHandle, InetSocketAddress> pool = objectPoolManager.getPool(serverInfo.getServiceName());
			if (null == pool)
				throw new NullPointerException("can't found sessionId's value.");
			// 断开重连接
			((NettyClientPool) pool).addConnectionByServerName(serverInfo);
		}
	}
	public void changeServerConnection(ConsumerValueModel consumerValueModel) throws Throwable {
		AbstractObjectPool<NettyClientHandle, InetSocketAddress> pool = objectPoolManager.getPool(consumerValueModel.getServiceName());
		if (null == pool)
			throw new NullPointerException("can't found sessionId's value.");
		// 断开重连接
		((NettyClientPool) pool).addConnectionByServerName(consumerValueModel);
	}

}
