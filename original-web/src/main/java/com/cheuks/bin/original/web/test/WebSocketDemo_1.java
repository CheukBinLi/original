package com.cheuks.bin.original.web.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

@ServerEndpoint("/hello")
public class WebSocketDemo_1 extends Endpoint {

	@Override
	public void onOpen(Session session, EndpointConfig config) {

		final RemoteEndpoint remoteEndpoint = session.getBasicRemote();
		session.addMessageHandler(new MessageHandler.Whole<String>() {

			public void onMessage(String message) {
				try {
					remoteEndpoint.sendPing(ByteBuffer.wrap(("Got your message (" + message + "). Thanks !").getBytes()));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		System.out.println("连接成功");
		try {
			session.getBasicRemote().sendText("hello client...");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch c = new CountDownLatch(10);
		ServerEndpointConfig config = ServerEndpointConfig.Builder.create(WebSocketDemo_1.class, "/hello").configurator(new Configurator()).build();
		c.await();
	}

	// @OnOpen
	// public void onopen(Session session) {
	// System.out.println("连接成功");
	// try {
	// session.getBasicRemote().sendText("hello client...");
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @OnClose
	// public void onclose(Session session) {
	// System.out.println("close....");
	//
	// }
	//
	// @OnMessage
	// public void onsend(Session session, String msg) {
	// try {
	// session.getBasicRemote().sendText("client" + session.getId() + "say:" + msg);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
}
