package com.cheuks.bin.original.web.customer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.web.customer.MessageOption.SenderType;

@SuppressWarnings({ "deprecation", "serial" })
public class CustomerServiceServlet extends WebSocketServlet {

	private final static Logger LOG = LoggerFactory.getLogger(CustomerServiceServlet.class);

	private static CustomerServiceServlet instance;

	private String centerIpAddress = "localhost:8888";
	private String currentIpAddress = "localhost:8888";
	private String servletPath = "original-web/test";

	public static final CustomerServiceServlet instance() {
		return instance;
	}

	public CustomerServiceServlet() {
		super();
		instance = this;
	}

	@Override
	public void init() throws ServletException {
		super.init();
		servletPath = getServletContext().getContextPath() + "/" + getServletName();
		System.err.println(servletPath);
		if (null == messageHandle) {
			synchronized (this) {
				if (null == messageHandle) {
					messageHandle = new MessageHandleFactory(currentIpAddress + servletPath, centerIpAddress + servletPath);
				}
			}
		}
	}

	private MessageHandle<DefaultMessageInbound, MessagePackage> messageHandle = null;

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request) {
		String psid = request.getParameter("psid");
		String partyId = request.getParameter("partyId");
		String tempSender = request.getParameter("senderType");
		SenderType sender = SenderType.valueOf(tempSender.toUpperCase());

		DefaultMessageInbound defaultMessageInbound = new DefaultMessageInbound(messageHandle, psid, partyId, sender);
		try {
			messageHandle.addConnection(defaultMessageInbound);
		} catch (Throwable e) {
			LOG.error(null, e);
		}
		return defaultMessageInbound;
	}

	public MessageHandle<DefaultMessageInbound, MessagePackage> getMessageHandle() {
		return messageHandle;
	}

	public static void main(String[] args) {
		System.out.println(SenderType.valueOf("system"));
	}
}
