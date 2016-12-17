package com.cheuks.bin.original.web.customer;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.cheuks.bin.original.web.customer.MessageOption.ContainerType;

@ClientEndpoint
public class ClusterRefreshClient {

	private CustomerServiceServlet customerServiceServlet = CustomerServiceServlet.instance();

	private MessageHandle<?, ?> getMessageHandle() {
		return customerServiceServlet.getMessageHandle();
	}

	private Session session;

	@OnOpen
	public void onOpen(Session session) throws IllegalArgumentException, IOException {
		System.out.println("Connected to endpoint: " + session.getBasicRemote());
		this.session = session;
	}

	@OnMessage
	public void onMessage(String message) {
		try {
			getMessageHandle().dispatcher(message);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	@OnClose
	public void onClose() {
		try {
			getMessageHandle().destory(ContainerType.CLUSTER, session);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
