package com.cheuks.bin.original.rmi.net.netty.client;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.RmiBeanFactory;
import com.cheuks.bin.original.common.rmi.model.ConsumerValueModel;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel.ServiceType;
import com.cheuks.bin.original.common.rmi.net.NetworkClient;
import com.cheuks.bin.original.common.util.AbstractObjectPool;
import com.cheuks.bin.original.common.util.ObjectPoolManager;
import com.cheuks.bin.original.rmi.config.RmiConfigArg;
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
public class SimpleNettyClient implements NetworkClient<Bootstrap, NettyClientHandle, InetSocketAddress, String, Void, RmiConfigArg, Boolean, Channel> {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleNettyClient.class);

	private volatile ObjectPoolManager<NettyClientHandle, InetSocketAddress> objectPoolManager;

	// session跟服务关联
	private Map<String, ConsumerValueModel> sessionInfo = new ConcurrentSkipListMap<String, ConsumerValueModel>();

	private RmiConfigArg rmiConfigArg;

	private LoadBalanceFactory<String, Void> loadBalanceFactory;

	private RmiBeanFactory<RmiConfigArg, Boolean> rmiBeanFactory;

	private Bootstrap client;

	private EventLoopGroup worker;

	private CacheSerialize cacheSerialize;

	private volatile boolean isInit;

	public SimpleNettyClient() {
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
			if (null == rmiBeanFactory) {
				// rmiBeanFactory = new SimpleRmiBeanFactory();
				throw new NullPointerException("rmiBeanFactory is null");
			}
			// 参数
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
			rmiBeanFactory.start(rmiConfigArg, false);
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
					ch.pipeline().addLast(new NettyClientHandle(SimpleNettyClient.this));
				}
			});
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#getObjectPoolManager()
	 */
	public ObjectPoolManager<NettyClientHandle, InetSocketAddress> getObjectPoolManager() {
		return objectPoolManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#setObjectPoolManager(com.cheuks.bin.original.common.util.ObjectPoolManager)
	 */
	public SimpleNettyClient setObjectPoolManager(ObjectPoolManager<NettyClientHandle, InetSocketAddress> objectPoolManager) {
		this.objectPoolManager = objectPoolManager;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#getClient()
	 */
	public Bootstrap getClient() {
		return client;
	}

	public SimpleNettyClient setClient(Bootstrap client) {
		this.client = client;
		return this;
	}

	public RmiConfigArg getRmiConfigArg() {
		return rmiConfigArg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#setRmiConfigArg(com.cheuks.bin.original.rmi.config.RmiConfigArg)
	 */
	public SimpleNettyClient setRmiConfigArg(RmiConfigArg rmiConfigArg) {
		this.rmiConfigArg = rmiConfigArg;
		return this;
	}

	public CacheSerialize getCacheSerialize() {
		return cacheSerialize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#setCacheSerialize(com.cheuks.bin.original.common.cache.CacheSerialize)
	 */
	public SimpleNettyClient setCacheSerialize(CacheSerialize cacheSerialize) {
		this.cacheSerialize = cacheSerialize;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#getLoadBalanceFactory()
	 */
	public LoadBalanceFactory<String, Void> getLoadBalanceFactory() {
		return loadBalanceFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#setLoadBalanceFactory(com.cheuks.bin.original.common.rmi.LoadBalanceFactory)
	 */
	public SimpleNettyClient setLoadBalanceFactory(LoadBalanceFactory<String, Void> loadBalanceFactory) {
		this.loadBalanceFactory = loadBalanceFactory;
		return this;
	}

	public RmiBeanFactory<RmiConfigArg, Boolean> getRmiBeanFactory() {
		return rmiBeanFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#setRmiBeanFactory(com.cheuks.bin.original.common.rmi.RmiBeanFactory)
	 */
	public SimpleNettyClient setRmiBeanFactory(RmiBeanFactory<RmiConfigArg, Boolean> rmiBeanFactory) {
		this.rmiBeanFactory = rmiBeanFactory;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#operationComplete(io.netty.channel.Channel, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void operationComplete(Channel channel, String serverName, String serviceName, String serverUrl, String consumerName, String consumerUrl) throws Throwable {
		synchronized (sessionInfo) {
			sessionInfo.put(channel.id().asLongText(), new ConsumerValueModel(serverName, serviceName));
		} // 注册
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#addWorker(com.cheuks.bin.original.rmi.net.netty.client.NettyClientHandle)
	 */
	public synchronized void addWorker(NettyClientHandle nettyClientHandle) throws Throwable {
		ConsumerValueModel serverInfo = getServerInfo(nettyClientHandle.getChannelHandlerContext().channel());
		if (null == serverInfo) {
			synchronized (sessionInfo) {
				serverInfo = getServerInfo(nettyClientHandle.getChannelHandlerContext().channel());
				if (null == serverInfo)
					throw new NullPointerException("can't found sessionId's value.");
			}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#changeServerConnection(com.cheuks.bin.original.rmi.net.netty.client.NettyClientHandle)
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cheuks.bin.original.rmi.net.netty.client.NettyClient#changeServerConnection(com.cheuks.bin.original.rmi.model.ConsumerValueModel)
	 */
	public void changeServerConnection(ConsumerValueModel consumerValueModel) throws Throwable {
		AbstractObjectPool<NettyClientHandle, InetSocketAddress> pool = objectPoolManager.getPool(consumerValueModel.getServiceName());
		if (null == pool)
			throw new NullPointerException("can't found sessionId's value.");
		// 断开重连接
		((NettyClientPool) pool).addConnectionByServerName(consumerValueModel);
	}

}
