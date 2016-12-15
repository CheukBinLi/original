package com.cheuks.bin.original.web.customer;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

import com.cheuks.bin.original.web.customer.MessageOption.SenderType;

@SuppressWarnings({ "deprecation", "serial" })
public class CustomerServiceServlet extends WebSocketServlet {

	private static CustomerServiceServlet instance;

	public static final CustomerServiceServlet instance() {
		return instance;
	}

	public CustomerServiceServlet() {
		super();
		instance = this;
	}

	private MessageHandle<DefaultMessageInbound, MessagePackage> messageHandle = null;

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request) {
		System.out.println(request.getServletPath());
		String psid = request.getParameter("psid");
		String partyId = request.getParameter("partyId");
		String tempSender = request.getParameter("senderType");
		SenderType sender = SenderType.valueOf(tempSender.toUpperCase());
		if (null == messageHandle) {
			synchronized (this) {
				if (null == messageHandle) {
					messageHandle = new MessageHandleFactory(request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + request.getServletPath(), "localhost:8888" + request.getContextPath() + request.getServletPath());
				}
			}
		}
		try {
			DefaultMessageInbound defaultMessageInbound = new DefaultMessageInbound(messageHandle, psid, partyId, sender);
			try {
				messageHandle.addConnection(defaultMessageInbound);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return defaultMessageInbound;
		} finally {
			System.out.println("连接人数:" + messageHandle.activityNumber());
		}
	}

	public MessageHandle<DefaultMessageInbound, MessagePackage> getMessageHandle() {
		return messageHandle;
	}

	public static void main(String[] args) {
		System.out.println(SenderType.valueOf("system"));
	}
}
