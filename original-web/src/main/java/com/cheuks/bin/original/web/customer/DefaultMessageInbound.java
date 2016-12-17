package com.cheuks.bin.original.web.customer;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.apache.catalina.websocket.MessageInbound;

@SuppressWarnings("deprecation")
public class DefaultMessageInbound extends MessageInbound implements Serializable, MessageOption {

	private static final long serialVersionUID = 1L;

	private MessageHandle<DefaultMessageInbound, MessagePackage> messageHandle;

	private String psid;

	private String partyId;

	private SenderType sender;

	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {

	}

	@Override
	protected void onTextMessage(CharBuffer arg0) throws IOException {
		try {
			messageHandle.dispatcher(arg0.toString());
		} catch (Throwable e) {
			e.printStackTrace();
			this.getWsOutbound().writeTextMessage(CharBuffer.wrap("错误信息"));
		}
	}

	@Override
	protected void onClose(int status) {
		super.onClose(status);
		try {
			messageHandle.destory(this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public String getPsid() {
		return psid;
	}

	public String getPartyId() {
		return partyId;
	}

	public SenderType getSender() {
		return sender;
	}

	@Override
	public int hashCode() {
		return this.partyId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefaultMessageInbound) {
			return this.partyId.equals(((DefaultMessageInbound) obj).partyId);
		}
		return false;
	}

	public DefaultMessageInbound(MessageHandle<DefaultMessageInbound, MessagePackage> messageHandle, String psid, String partyId, SenderType sender) {
		super();
		this.messageHandle = messageHandle;
		this.psid = psid;
		this.partyId = partyId;
		this.sender = sender;
	}

}
