package com.cheuks.bin.original.web.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import net.sf.ehcache.util.concurrent.ConcurrentHashMap;

@Deprecated
public class WebSocketDemo_3 extends WebSocketServlet {

	private final BlockingDeque<ChatMessageInbound> CLIENT_QUEUE = new LinkedBlockingDeque<ChatMessageInbound>();
	private final BlockingDeque<ChatMessageInbound> CUSTOMER_SERVICE_QUEUE = new LinkedBlockingDeque<ChatMessageInbound>();

	protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
		return new ChatMessageInbound();
	}

	final class ChatMessageInbound extends MessageInbound {

		private String id;
		private int type;

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
			System.out.println("onTextMessage");
		}

		@Override
		protected void onClose(int status) {
			super.onClose(status);
			// if(客户/客服)
			CLIENT_QUEUE.remove(this);
		}

		@Override
		protected void onOpen(WsOutbound outbound) {
			super.onOpen(outbound);
		}
	}
}