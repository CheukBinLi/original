package com.cheuks.bin.original.web.customer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.websocket.DeploymentException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import org.apache.catalina.websocket.WsOutbound;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.cache.JedisStandAloneCacheFactory;
import com.cheuks.bin.original.common.cache.redis.RedisExcecption;

@SuppressWarnings("deprecation")
public class MessageHandleFactory implements MessageHandle<DefaultMessageInbound, MessagePackage> {

	private final static Logger LOG = LoggerFactory.getLogger(MessageHandleFactory.class);
	// 服务器唯一标式
	final String serverName;
	// 服务自治主机目录
	final String ledderServerName;
	final boolean isLedder;
	final JedisStandAloneCacheFactory redis = new JedisStandAloneCacheFactory();
	final Map<String, DefaultMessageInbound> CONNECTION = new ConcurrentHashMap<String, DefaultMessageInbound>();
	final Map<String, Session> CLUSTER = new ConcurrentHashMap<String, Session>();
	final Map<String, DefaultMessageInbound> CLUSTER_SERVER = new ConcurrentHashMap<String, DefaultMessageInbound>();
	final BlockingDeque<DefaultMessageInbound> SYSTEM = new LinkedBlockingDeque<DefaultMessageInbound>();
	final BlockingDeque<MessagePackage> NOT_FOUND_CUSTOMER_SERVICE_QUEUE = new LinkedBlockingDeque<MessagePackage>();
	final ObjectMapper mapper = new ObjectMapper();// json
	private int inConversationTimeOut = 600000;// 10分钟结束
	{
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
	}
	final String customerServiceQueueName = "CUSTOMER_SERVICE_QUEUE_NAME";// 客服名
	final String customerServiceCloneQueueName = "CUSTOMER_SERVICE_CLONE_QUEUE_NAME";// 作用：分配客服
	final String customerServiceServerQueueName = "CUSTOMER_SERVICE_SERVER_QUEUE_NAME";// 客服所在服务器名
	final String inConversationQueueName = "CUSTOMER_IN_CONVERSATION_QUEUE_NAME";// 会话中队列名
	final BlockingDeque<Runnable> TASK;
	volatile boolean interrupt = false;
	final int maxAttempts = 10;// 尝试次数
	final int attemptInterval = 500;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);

	public MessageHandleFactory(String serverName, String ledderServerName) {
		super();
		this.serverName = serverName;
		this.ledderServerName = ledderServerName;
		isLedder = serverName.equals(ledderServerName);
		// 密码、地址、端口
		redis.setCacheSerialize(new FstCacheSerialize());
		if (!isLedder) {
			TASK = null;
			int tryCount = 10;
			Session session = null;
			while (tryCount-- > 0 && null == session) {
				try {
					if (tryCount != 9)
						Thread.sleep(3000);
					session = WebSocketUtil.connectionCluster(ClusterRefreshClient.class, ledderServerName, "", this.serverName, SenderType.CLUSTER);
					CLUSTER.put(ledderServerName, session);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			TASK = new LinkedBlockingDeque<Runnable>();
			executorService.submit(new DelayedTaskHandler(interrupt, TASK));
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					interrupt = true;
					executorService.shutdown();
				}
			}));
		}
		executorService.submit(new NotFoundCustomerService(NOT_FOUND_CUSTOMER_SERVICE_QUEUE));
	}

	private String getInConversationQueueKey(String psid, String client) {
		return comprise(inConversationQueueName, psid, client);
	}

	private String getCustomerServiceQueueKey(String psid) {
		return comprise(customerServiceQueueName, psid);
	}

	private String getCustomerServiceCloneQueueKey(String psid) {
		return comprise(customerServiceCloneQueueName, psid);
	}

	// 客户过期检查
	@Override
	public MessageHandleFactory addConnection(DefaultMessageInbound c) throws RedisExcecption, JsonGenerationException, JsonMappingException, IOException {
		// 直接覆盖，后期修改，只覆盖连接，保留连接了的客户
		if (null != c.getPartyId()) {
			CONNECTION.put(c.getPartyId(), c);
		}
		if (SenderType.CUSTOMER_SERVICE == c.getSender()) {
			redis.setMap(customerServiceServerQueueName, comprise(c.getPsid(), c.getPartyId()), serverName);
			redis.addListLast(getCustomerServiceQueueKey(c.getPsid()), c.getPartyId());
			redis.addListLast(getCustomerServiceCloneQueueKey(c.getPsid()), c.getPartyId());
		} else if (SenderType.SYSTEM == c.getSender()) {
			SYSTEM.addLast(c);
		} else if (SenderType.CLUSTER == c.getSender()) {// 新主机加入集群
			// System.out.println("新主机加入集群:" + c.getPartyId());
			CLUSTER_SERVER.put(c.getPartyId(), c);
			if (isLedder) {
				clusterRefreshHandle();
			}
		}
		return this;
	}

	@Override
	public void destory(final DefaultMessageInbound c) throws RedisExcecption {
		if (null != c.getPartyId()) {
			CONNECTION.remove(c.getPartyId());
		}
		SYSTEM.remove(c);
		if (SenderType.CUSTOMER_SERVICE == c.getSender()) {
			redis.mapRemove(customerServiceServerQueueName, comprise(c.getPsid(), c.getPartyId()));
			redis.removeListValue(getCustomerServiceQueueKey(c.getPsid()), c.getPartyId(), 10);
			redis.removeListValue(getCustomerServiceCloneQueueKey(c.getPsid()), c.getPartyId(), 10);
		}
		System.out.println("断开连接:" + c.getPartyId());
	}

	@Override
	public void destory(ContainerType containerType, Object o) throws Throwable {
		if (ContainerType.CLUSTER == containerType) {
			CLUSTER.remove(o);
		}
	}

	@Override
	public int activityNumber() {
		return CONNECTION.size();
	}

	@Override
	public synchronized void sendToConsumerService(final MessagePackage message) throws Throwable {
		if (message.getType() == MessagePackageType.WAITING_FOR_ACCESS) {
			List<String> customerServiceList = redis.getMapList(customerServiceQueueName, comprise(message.getPsid(), message.getReceiver()));
			if (null != customerServiceList) {
				for (String cs : customerServiceList) {
					doSendToConsumerService(message.setReceiver(cs));
				}
			}
		} else if (message.getType() == MessagePackageType.SERVICE_DISTRIBUTION) {// 分配客服
			String inConversationQueueKey = getInConversationQueueKey(message.getPsid(), message.getSender());
			String consumerService = redis.get(inConversationQueueKey);
			if (null == consumerService) {// 分配客服
				String consumerServiceKey = getCustomerServiceCloneQueueKey(message.getPsid());
				consumerService = redis.popListFirst(consumerServiceKey);
				if (null == consumerService) {
					message.setType(MessagePackageType.NO_CUSTOMER_SERVICE);
					dispatcher(message);
					return;
				}
				redis.addListLast(consumerServiceKey, consumerService);
				redis.set(getInConversationQueueKey(message.getPsid(), message.getSender()), consumerService, inConversationTimeOut);
			}
			message.setType(null).setReceiver(consumerService);
			doSendToConsumerService(message);
		} else {
			doSendToConsumerService(message);
		}
	}

	private void doSendToConsumerService(final MessagePackage message) throws Throwable {
		String value = mapper.writeValueAsString(message);
		DefaultMessageInbound service;
		WsOutbound out;
		String tempServerName = redis.getMapValue(customerServiceServerQueueName, comprise(message.getPsid(), message.getReceiver()));
		if (null != tempServerName && !this.serverName.equals(tempServerName)) {
			if (isLedder) {// 当前主机是集群的中心机
				service = CLUSTER_SERVER.get(tempServerName);
				if (null != service) {
					out = service.getWsOutbound();
					out.writeTextMessage(CharBuffer.wrap(value));
					out.flush();
					return;
				}
			} else {
				Session session = CLUSTER.get(tempServerName);
				if (null != session) {
					RemoteEndpoint.Basic remote = session.getBasicRemote();
					remote.sendText(value);
					remote.flushBatch();
					return;
				}
			}
		}
		if (this.serverName.equals(tempServerName)) {// 客服在当前实例中
			service = CONNECTION.get(message.getReceiver());
			if (null != service) {
				out = service.getWsOutbound();
				out.writeTextMessage(CharBuffer.wrap(value));
				out.flush();
				return;
			}
		}
		notFoundCustomerService(message);
	}

	@Override
	public void sendToSystem(final MessagePackage message) throws Throwable {
		// System.out.println(message.getReceiver() + "：接收消息:" + message.getMsg());
		String value = mapper.writeValueAsString(message);
		DefaultMessageInbound system = SYSTEM.pollFirst();
		WsOutbound out;
		try {
			if (null != system) {
				out = system.getWsOutbound();
				out.writeTextMessage(CharBuffer.wrap(value));
				out.flush();
			}
		} finally {
			if (null != system) {
				SYSTEM.addLast(system);
			}
		}
	}

	public void dispatcher(final MessagePackage messagePackage) throws Throwable {
		if (MessagePackageType.HEART_BEAT == messagePackage.getType()) {
			return;
		} else if (MessagePackageType.CLOSE == messagePackage.getType()) {

		} else if (MessagePackageType.NO_CUSTOMER_SERVICE == messagePackage.getType()) {
			sendToSystem(messagePackage.setReceiver(messagePackage.getSender()));
		} else if (MessagePackageType.CLUSTER_REFRESH == messagePackage.getType()) {// 刷新集群列表
			clusterRefresh(messagePackage);
		} else if (MessagePackageType.SEND_TO_SYSTEM == messagePackage.getType()) {// 刷新集群列表
			sendToSystem(messagePackage);
		} else if (SenderType.SYSTEM == messagePackage.getSenderType()) {
			sendToConsumerService(messagePackage);
		} else if (SenderType.CUSTOMER_SERVICE == messagePackage.getSenderType()) {
			sendToSystem(messagePackage);
			redis.set(getInConversationQueueKey(messagePackage.getPsid(), messagePackage.getReceiver()), messagePackage.getSender(), inConversationTimeOut);
		}
	}

	@Override
	public void dispatcher(String message) throws Throwable {
		System.out.println(message);
		final MessagePackage messagePackage = mapper.readValue(message, MessagePackage.class);
		dispatcher(messagePackage);
	}

	@Override
	public void notFoundCustomerService(final MessagePackage message) throws Throwable {
		NOT_FOUND_CUSTOMER_SERVICE_QUEUE.addLast(message);
	}

	/***
	 * 刷新集群列表
	 * 
	 * @param message
	 * @throws DeploymentException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public void clusterRefresh(final MessagePackage message) throws DeploymentException, IOException, URISyntaxException {
		StringTokenizer tokenizer = new StringTokenizer(message.getMsg(), ",");
		String tempServerName;
		while (tokenizer.hasMoreTokens()) {
			tempServerName = tokenizer.nextToken();
			if (!serverName.equals(tempServerName) && !CLUSTER.containsKey(tempServerName)) {
				Session session = WebSocketUtil.connectionCluster(ClusterRefreshClient.class, tempServerName, "", this.serverName, SenderType.CLUSTER);
				CLUSTER.put(tempServerName, session);
			}
		}
	}

	/***
	 * 发送集群列表
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void clusterRefreshHandle() {
		// 延迟任务
		Runnable delayedTask = new Runnable() {
			@Override
			public void run() {
				try {
					final MessagePackage message = new MessagePackage();
					message.setType(MessagePackageType.CLUSTER_REFRESH);
					StringBuilder sb = new StringBuilder();
					for (Entry<String, DefaultMessageInbound> single : CLUSTER_SERVER.entrySet()) {
						sb.append(single.getKey()).append(",");
					}
					if (sb.length() > 0) {
						sb.setLength(sb.length() - 1);
					}
					message.setMsg(sb.toString());
					CharBuffer value = CharBuffer.wrap(mapper.writeValueAsString(message));
					DefaultMessageInbound defaultMessageInbound;
					for (Entry<String, DefaultMessageInbound> single : CLUSTER_SERVER.entrySet()) {
						defaultMessageInbound = single.getValue();
						WsOutbound ws = defaultMessageInbound.getWsOutbound();
						ws.writeTextMessage(value);
						ws.flush();
						value.rewind();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		TASK.addLast(delayedTask);
	}

	/***
	 * 字符串组合
	 * 
	 * @param params
	 * @return
	 */
	String comprise(final String... params) {
		StringBuilder sb = new StringBuilder();
		if (null != params) {
			for (String str : params) {
				sb.append(str).append("_");
			}
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	final class NotFoundCustomerService implements Runnable {
		private final BlockingDeque<MessagePackage> notFoundCustomerServiceQueue;

		@Override
		public void run() {
			MessagePackage messagePackage;
			try {
				while (!interrupt) {
					if (null != (messagePackage = notFoundCustomerServiceQueue.pollFirst(500, TimeUnit.MILLISECONDS))) {
						if (maxAttempts <= messagePackage.getAndAddAttempts(1)) {
							messagePackage.setType(MessagePackageType.NO_CUSTOMER_SERVICE);
						} else if (maxAttempts / 2 == messagePackage.getAttempts()) {
							redis.delete(getInConversationQueueKey(messagePackage.getPsid(), messagePackage.getSender()));
							messagePackage.setType(MessagePackageType.SERVICE_DISTRIBUTION);

							MessagePackage message = new MessagePackage().setAdditional(messagePackage.getAdditional()).setReceiver(messagePackage.getSender());
							message.setPsid(messagePackage.getPsid()).setMsg("正在为你转接.请稍候...").setType(MessagePackageType.SEND_TO_SYSTEM);
							dispatcher(message);
						}
						dispatcher(messagePackage);
						Thread.sleep(500);
					}
				}
			} catch (Throwable e) {
				LOG.error("", e);
			}
		}

		public NotFoundCustomerService(final BlockingDeque<MessagePackage> notFoundCustomerServiceQueue) {
			this.notFoundCustomerServiceQueue = notFoundCustomerServiceQueue;
		}
	}
}
