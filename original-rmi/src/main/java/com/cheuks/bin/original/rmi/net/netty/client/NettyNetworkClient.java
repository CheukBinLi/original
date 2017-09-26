package com.cheuks.bin.original.rmi.net.netty.client;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.model.ConsumerValueModel;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel.ServiceType;
import com.cheuks.bin.original.common.rmi.net.NetworkClient;
import com.cheuks.bin.original.common.util.pool.AbstractObjectPool;
import com.cheuks.bin.original.common.util.pool.ObjectPoolManager;
import com.cheuks.bin.original.rmi.config.ReferenceGroupConfig.ReferenceGroupModel;
import com.cheuks.bin.original.rmi.config.RmiConfig.RmiConfigGroup;
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
public class NettyNetworkClient implements NetworkClient<Bootstrap, NettyClientHandle, InetSocketAddress, String, Void, Channel, RmiConfigGroup> {

	private static final Logger LOG = LoggerFactory.getLogger(NettyNetworkClient.class);

	private ObjectPoolManager<NettyClientHandle, InetSocketAddress> objectPoolManager;

	// session跟服务关联
	private Map<String, ConsumerValueModel> sessionInfo = new ConcurrentSkipListMap<String, ConsumerValueModel>();

	private RmiConfigGroup rmiConfigGroup;

	private LoadBalanceFactory<String, Void> loadBalanceFactory;

	private Bootstrap client;

	private EventLoopGroup worker;

	private CacheSerialize cacheSerialize;

	private volatile boolean isInit;

	public NettyNetworkClient() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#start()
	 */
	public void start() {
		if (isInit)
			return;
		isInit = true;
		try {
			// 参数
			if (null == rmiConfigGroup)
				throw new NullPointerException("can't found rmiConfigGroup instance.");
			if (null == cacheSerialize)
				cacheSerialize = new FstCacheSerialize();
			if (null == loadBalanceFactory) {
				throw new NullPointerException("can't found loadBalanceFactory instance.");
			}
			loadBalanceFactory.init();
			if (null == objectPoolManager) {
				throw new NullPointerException("can't found objectPoolManager instance.");
			}
			objectPoolManager.setTryAgain(rmiConfigGroup.getRegistryModel().getMaxRetries());

			// rmiBeanFactory.start(rmiConfigArg, false);
			client = new Bootstrap();
			if (rmiConfigGroup.getProtocolModel().getNetWorkThreads() <= 0) {
				rmiConfigGroup.getProtocolModel().setNetWorkThreads(Runtime.getRuntime().availableProcessors() * 2);
			}
			initInfo();
			worker = new NioEventLoopGroup(rmiConfigGroup.getProtocolModel().getNetWorkThreads());
			client.group(worker).option(ChannelOption.SO_KEEPALIVE, true).channel(NioSocketChannel.class);
			client.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new NettyMessageDecoder(rmiConfigGroup.getProtocolModel().getFrameLength(), 4, 4, cacheSerialize));
					ch.pipeline().addLast(new NettyMessageEncoder(cacheSerialize));
					ch.pipeline().addLast(new IdleStateHandler(0, 0, rmiConfigGroup.getProtocolModel().getHeartbeat()));
					ch.pipeline().addLast(new NettyClientHandle(NettyNetworkClient.this));
				}
			});
			for (Entry<String, ReferenceGroupModel> en : this.getRmiConfigGroup().getReferenceGroup().getReferenceGroup().entrySet()) {
				NettyClientPool pool = new NettyClientPool(this, en.getKey());
				objectPoolManager.addPool(pool);
				pool.start();
			}
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

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

	public void addWorker(NettyClientHandle nettyClientHandle) throws Throwable {
		ConsumerValueModel serverInfo = getServerInfo(nettyClientHandle.getChannelHandlerContext().channel());
		if (null == serverInfo) {
			int tryagain = 5;
			while (tryagain-- > 0) {
				synchronized (sessionInfo) {
					serverInfo = getServerInfo(nettyClientHandle.getChannelHandlerContext().channel());
					if (null == serverInfo && tryagain < 0)
						throw new NullPointerException("can't found sessionId's value.");
					else if (null != serverInfo)
						break;
					try {
						sessionInfo.wait(200);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			AbstractObjectPool<NettyClientHandle, InetSocketAddress> pool = this.objectPoolManager.getPool(serverInfo.getServiceName());
			NettyClientPool nettyClientPool;
			if (null == pool) {
				nettyClientPool = new NettyClientPool(this, serverInfo.getServiceName());
				nettyClientPool.addObject(nettyClientHandle);
				objectPoolManager.addPool(nettyClientPool);
			} else {
				pool.addObject(nettyClientHandle);
			}
		}
	}

	public void changeServerConnection(NettyClientHandle nettyClientHandle) throws Throwable {
		ConsumerValueModel serverInfo = removeServerInfo(nettyClientHandle.getChannelHandlerContext().channel());
		if (null == serverInfo) {
			throw new NullPointerException("can't found sessionId's value.");
		} else {
			AbstractObjectPool<NettyClientHandle, InetSocketAddress> pool = objectPoolManager.getPool(serverInfo.getServiceName());
			if (null == pool)
				throw new NullPointerException("can't found sessionId's value.");
			// 断开重连接
			pool.removeObject(nettyClientHandle);
			((NettyClientPool) pool).addConnectionByServerName(serverInfo.setTryAgain(rmiConfigGroup.getRegistryModel().getMaxRetries()));
		}
	}

	public void changeServerConnection(ConsumerValueModel consumerValueModel) throws Throwable {
		AbstractObjectPool<NettyClientHandle, InetSocketAddress> pool = objectPoolManager.getPool(consumerValueModel.getServiceName());
		if (null == pool)
			throw new NullPointerException("can't found sessionId's value.");
		// 断开重连接
		((NettyClientPool) pool).addConnectionByServerName(consumerValueModel);
	}

	void initInfo() {
		LOG.info("rmi client info.");
		LOG.info("registration center address:{}", rmiConfigGroup.getRegistryModel().getServerAddress());
		LOG.info("registration name:{}", rmiConfigGroup.getProtocolModel().getLocalName());
		LOG.info("network address:{}", rmiConfigGroup.getProtocolModel().getLocalAddress());
		LOG.info("network frame max size:{}", (rmiConfigGroup.getProtocolModel().getFrameLength() / 1024) + "KB");
		LOG.info("network access thread:{} , message handle thread:{}", rmiConfigGroup.getProtocolModel().getNetWorkThreads(), rmiConfigGroup.getProtocolModel().getHandleThreads());
	}

	public ObjectPoolManager<NettyClientHandle, InetSocketAddress> getObjectPoolManager() {
		return this.objectPoolManager;
	}

	public NettyNetworkClient setObjectPoolManager(ObjectPoolManager<NettyClientHandle, InetSocketAddress> objectPoolManager) {
		this.objectPoolManager = objectPoolManager;
		return this;
	}

	public Bootstrap getClient() {
		return client;
	}

	public NettyNetworkClient setClient(Bootstrap client) {
		this.client = client;
		return this;
	}

	public RmiConfigGroup getRmiConfigGroup() {
		return this.rmiConfigGroup;
	}

	public NettyNetworkClient setRmiConfigGroup(RmiConfigGroup rmiConfigGroup) {
		this.rmiConfigGroup = rmiConfigGroup;
		return this;
	}

	public CacheSerialize getCacheSerialize() {
		return cacheSerialize;
	}

	public NettyNetworkClient setCacheSerialize(CacheSerialize cacheSerialize) {
		this.cacheSerialize = cacheSerialize;
		return this;
	}

	public LoadBalanceFactory<String, Void> getLoadBalanceFactory() {
		return loadBalanceFactory;
	}

	public NettyNetworkClient setLoadBalanceFactory(LoadBalanceFactory<String, Void> loadBalanceFactory) {
		this.loadBalanceFactory = loadBalanceFactory;
		return this;
	}

}
