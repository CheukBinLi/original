package com.cheuks.bin.original.web.customer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;

import com.cheuks.bin.original.web.customer.MessageOption.MessagePackageType;
import com.cheuks.bin.original.web.customer.MessageOption.SenderType;

public class WebSocketFactoryForWeixin {

	// ToUserName 开发者微信号
	// FromUserName 发送方帐号（一个OpenID）
	// CreateTime 消息创建时间 （整型）
	// MsgType text
	// Content 文本消息内容
	// MsgId

	private static WebSocketFactoryForWeixin instance = new WebSocketFactoryForWeixin();

	public static WebSocketFactoryForWeixin instance() {
		return instance;
	}

	private final BlockingDeque<MessagePackage> sendToCustomerServiceTask = new LinkedBlockingDeque<MessagePackage>();
	private final BlockingDeque<MessagePackage> sendToCustomerTask = new LinkedBlockingDeque<MessagePackage>();
	private volatile boolean interrupt = false;
	private volatile Session session = null;
	private final CustomerServiceReply customerServiceReply = null;
	final ObjectMapper mapper = new ObjectMapper();
	private String ipAddress = "localhost:8888";
	private final Thread sendToCustomerServiceTaskThread = new Thread(new Runnable() {
		@Override
		public void run() {
			MessagePackage messagePackage;
			try {
				while (!interrupt) {
					if (null != (messagePackage = sendToCustomerServiceTask.pollFirst(100, TimeUnit.MILLISECONDS))) {
						String value = mapper.writeValueAsString(messagePackage);
						session.getBasicRemote().sendText(value);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	});

	private final Thread sendToCustomerTaskThread = new Thread(new Runnable() {
		@Override
		public void run() {
			MessagePackage messagePackage;
			try {
				while (!interrupt) {
					if (null != (messagePackage = sendToCustomerTask.pollFirst(100, TimeUnit.MILLISECONDS))) {
						customerServiceReply.sendToWeiXinCustomer(messagePackage);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});

	public void sendMessage(Map<String, String> weixinData, String psid) {
		// 检测有没和客服对话,没就随机选一个
		String receiver = "随机一个客服@小红红";
		MessagePackage messagePackage = new MessagePackage();
		messagePackage.setPsid(psid).setType(MessagePackageType.SERVICE_DISTRIBUTION).setSenderType(SenderType.SYSTEM).setSender(weixinData.get("FromUserName")).setAdditional(weixinData.get("ToUserName")).setReceiver(receiver).setMsg(weixinData.get("Content"));
		sendToCustomerTask.addLast(messagePackage);
	}

	public void addCustomerTask(String message) throws Exception {
		MessagePackage messagePackage = mapper.readValue(message, MessagePackage.class);
		sendToCustomerTask.addLast(messagePackage);
	}

	public void reconnection() {
		int tryCount = 10;
		while (tryCount-- > 0 && null == session) {
			try {
				if (tryCount != 9)
					Thread.sleep(3000);
				session = WebSocketUtil.connection(SystemClientChannel.class, ipAddress, "senderType=" + SenderType.SYSTEM);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private WebSocketFactoryForWeixin() {
		super();
		reconnection();
		sendToCustomerServiceTaskThread.start();
		sendToCustomerTaskThread.start();
	}

	public void shutdown() {
		interrupt = true;
	}
}
