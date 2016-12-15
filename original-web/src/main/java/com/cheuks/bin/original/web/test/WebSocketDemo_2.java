package com.cheuks.bin.original.web.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

@Deprecated
public class WebSocketDemo_2 extends WebSocketServlet {

	protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
		return new ChatMessageInbound();
	}

	public static class ChatMessageInbound extends MessageInbound {

		@Override
		protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
			System.out.println("onBinaryMessage");
		}

		@Override
		protected void onTextMessage(CharBuffer arg0) throws IOException {
			// for (MessageInbound messageInbound : socketList) {
			// CharBuffer buffer = CharBuffer.wrap(message);
			// WsOutbound outbound = messageInbound.getWsOutbound();
			// outbound.writeTextMessage(buffer);
			// outbound.flush();
			// }
		}

	}
}