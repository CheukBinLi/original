package com.cheuks.bin.original.web.customer;

import java.nio.CharBuffer;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.codehaus.jackson.map.ObjectMapper;

public class MessageHandleFactory2 implements MessageHandle<DefaultMessageInbound, MessagePackage> {
	// 服务列表
	// 客户列表
	// 任务队列

	final Map<String, DefaultMessageInbound> CONNECTION_POOL = new ConcurrentHashMap<>();
	// 服务队列:推一个加一个
	final Map<String, LinkedBlockingDeque<DefaultMessageInbound>> SERVICE_QUEUE = new ConcurrentHashMap<String, LinkedBlockingDeque<DefaultMessageInbound>>();
	// 系统队列
	final Map<String, LinkedBlockingDeque<DefaultMessageInbound>> SYSTEM_QUEUE = new ConcurrentHashMap<String, LinkedBlockingDeque<DefaultMessageInbound>>();

	final ObjectMapper mapper = new ObjectMapper();

	/** 任务 */
	final BlockingDeque<MessagePackage> TASK_QUEUE = new LinkedBlockingDeque<MessagePackage>();

	// 客户过期检查
	@Override
	public MessageHandleFactory2 addConnection(DefaultMessageInbound c) {
		// 直接覆盖，后期修改，只覆盖连接，保留连接了的客户
		CONNECTION_POOL.put(c.getPartyId(), c);
		if (c.isSystem()) {
			SYSTEM_QUEUE.addLast(c);
		} else {
			SERVICE_QUEUE.addLast(c);
		}
		return this;
	}

	@Override
	public void destory(DefaultMessageInbound c) {
		System.out.println("断开连接:" + c.getPartyId());
		CONNECTION_POOL.remove(c.getPartyId());
	}

	@Override
	public int activityNumber() {
		return CONNECTION_POOL.size();
	}

	@Override
	public void sendToConsumerService(MessagePackage message) throws Throwable {
		// 没实现分配规则
		DefaultMessageInbound service = SERVICE_QUEUE.pollFirst();
		if (null == service)
			throw new IndexOutOfBoundsException("is not servicer on line.");
		// defaultMessageInbound.getClientList().put(message.getSender(), new ClientInfo());
		String value = mapper.writeValueAsString(message);
		try {
			service.getWsOutbound().writeTextMessage(CharBuffer.wrap(value));
		} finally {
			if (null != service) {
				SERVICE_QUEUE.putLast(service);
			}
		}
	}

	@Override
	public void sendToSystem(MessagePackage message) throws Throwable {
		System.out.println(message.getReceiver() + "：接收消息:" + message.getMsg());
		String value = mapper.writeValueAsString(message);
		DefaultMessageInbound system = SYSTEM_QUEUE.pollFirst();
		if (null != system) {
			system.getWsOutbound().writeTextMessage(CharBuffer.wrap(value));
		}
	}

	@Override
	public void dispatcher(String message) throws Throwable {
		MessagePackage messagePackage = mapper.readValue(message, MessagePackage.class);
		if (MessagePackage.SenderType.SYSTEM == messagePackage.getSenderType()) {
			sendToConsumerService(messagePackage);
		} else {
			sendToSystem(messagePackage);
		}
	}

}
