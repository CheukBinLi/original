package com.cheuks.bin.original.web.customer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import javax.websocket.ClientEndpoint;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.apache.catalina.websocket.WsOutbound;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.cache.JedisStandAloneCacheFactory;
import com.cheuks.bin.original.common.cache.redis.RedisExcecption;
import com.cheuks.bin.original.web.customer.MessageOption.ContainerType;

public class MessageHandleFactory implements MessageHandle<DefaultMessageInbound, MessagePackage> {

	// 服务器唯一标式
	final String serverName;
	// 服务自治主机
	final String ledderServerName;
	final boolean isLedder;

	final JedisStandAloneCacheFactory redis = new JedisStandAloneCacheFactory();

	final Map<String, DefaultMessageInbound> CONNECTION = new ConcurrentHashMap<String, DefaultMessageInbound>();

	final Map<String, Session> CLUSTER = new ConcurrentHashMap<String, Session>();
	final Map<String, DefaultMessageInbound> CLUSTER_SERVER = new ConcurrentHashMap<String, DefaultMessageInbound>();

	final BlockingDeque<DefaultMessageInbound> SYSTEM = new LinkedBlockingDeque<DefaultMessageInbound>();

	final ObjectMapper mapper = new ObjectMapper();

	final String customerServiceName = "CUSTOMER_SERVICE_NAME";

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

	public MessageHandleFactory(String serverName, String ledderServerName) {
		super();
		this.serverName = serverName;
		this.ledderServerName = ledderServerName;
		isLedder = serverName.equals(ledderServerName);
		// 密码、地址、端口
		redis.setCacheSerialize(new FstCacheSerialize());
		if (!isLedder) {
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
		}
	}

	// 客户过期检查
	@Override
	public MessageHandleFactory addConnection(DefaultMessageInbound c) throws RedisExcecption, JsonGenerationException, JsonMappingException, IOException {
		// 直接覆盖，后期修改，只覆盖连接，保留连接了的客户
		CONNECTION.put(c.getPartyId(), c);
		if (SenderType.CUSTOMER_SERVICE == c.getSender()) {
			redis.setMap(customerServiceName, comprise(c.getPsid(), c.getPartyId()), serverName);
		} else if (SenderType.SYSTEM == c.getSender()) {
			SYSTEM.addLast(c);
		} else if (SenderType.CLUSTER == c.getSender()) {// 新主机加入集群
			System.out.println("新主机加入集群:" + c.getPartyId());
			CLUSTER_SERVER.put(c.getPartyId(), c);
			clusterRefreshHandle();
		}
		return this;
	}

	@Override
	public void destory(DefaultMessageInbound c) throws RedisExcecption {
		CONNECTION.remove(c.getPartyId());
		if (SenderType.CUSTOMER_SERVICE == c.getSender()) {
			redis.mapRemove(customerServiceName, comprise(c.getPsid(), c.getPartyId()));
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
	public void sendToConsumerService(MessagePackage message) throws Throwable {
		String value = mapper.writeValueAsString(message);
		DefaultMessageInbound service;
		// 从Redis取客服所在服务器
		String tempServerName = redis.getMapValue(customerServiceName, comprise(message.getPsid(), message.getReceiver()));
		if (null != tempServerName && !this.serverName.equals(tempServerName)) {
			if (isLedder) {// 当前主机是集群的中心机
				service = CLUSTER_SERVER.get(tempServerName);
				if (null != service) {
					service.getWsOutbound().writeTextMessage(CharBuffer.wrap(value));
					return;
				}
			} else {
				Session session = CLUSTER.get(tempServerName);
				if (null != session) {
					session.getBasicRemote().sendText(value);
					return;
				}
			}
		}
		if (this.serverName.equals(tempServerName)) {// 客服在当前实例中
			service = CONNECTION.get(message.getReceiver());
			if (null != service) {
				service.getWsOutbound().writeTextMessage(CharBuffer.wrap(value));
				return;
			}
		}
		notFoundCustomerService(message);
	}

	@Override
	public void sendToSystem(MessagePackage message) throws Throwable {
		System.out.println(message.getReceiver() + "：接收消息:" + message.getMsg());
		String value = mapper.writeValueAsString(message);
		DefaultMessageInbound system = SYSTEM.pollFirst();
		try {
			if (null != system) {
				system.getWsOutbound().writeTextMessage(CharBuffer.wrap(value));
			}
		} finally {
			if (null != system) {
				SYSTEM.addLast(system);
			}
		}
	}

	@Override
	public void dispatcher(String message) throws Throwable {
		System.out.println(message);
		MessagePackage messagePackage = mapper.readValue(message, MessagePackage.class);
		if (MessagePackageType.HEART_BEAT == messagePackage.getType()) {
			return;
		} else if (MessagePackageType.CLUSTER_REFRESH == messagePackage.getType()) {// 刷新集群列表
			clusterRefresh(messagePackage);
		} else if (SenderType.SYSTEM == messagePackage.getSenderType()) {
			sendToConsumerService(messagePackage);
		} else if (SenderType.CUSTOMER_SERVICE == messagePackage.getSenderType()) {
			sendToSystem(messagePackage);
		}
	}

	@Override
	public void notFoundCustomerService(MessagePackage message) throws Throwable {

	}

	public void getCharBuffer(String msg) {

	}

	public void clusterRefresh(MessagePackage message) throws DeploymentException, IOException, URISyntaxException {
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

	public void clusterRefreshHandle() throws JsonGenerationException, JsonMappingException, IOException {
		MessagePackage message = new MessagePackage();
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
		DefaultMessageInbound def;
		for (Entry<String, DefaultMessageInbound> single : CLUSTER_SERVER.entrySet()) {
			def = single.getValue();
			WsOutbound ws = def.getWsOutbound();
			if (null == ws)
				return;
			ws.writeTextMessage(value);
		}
	}
}
