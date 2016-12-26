package com.cheuks.bin.original.web.customer;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class SystemClientChannel {

	private WebSocketFactoryForWeixin getWebSocketFactoryForWeixin() {
		return WebSocketFactoryForWeixin.instance();
	}

	@OnOpen
	public void onOpen(Session session) throws IllegalArgumentException, IOException {
	}

	@OnMessage
	public void onMessage(String message) {
		try {
			getWebSocketFactoryForWeixin().addCustomerTask(message);
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
			getWebSocketFactoryForWeixin().reconnection();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
