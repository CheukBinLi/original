package com.cheuks.bin.original.rmi.config.model;

import java.io.Serializable;

public class ProtocolModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String address;// 地址
	private int port;// 端口
	private int netWorkThreads;// 网络你监听线程数
	private int handleThreads;// 处理线程数
	private String charset;// 编码
	private int heartbeat;// 心跳间隔
	private int packetSize;// 最大请求数据长度
	private int callBackTimeOut;// 回调超时，默认1分钟
	private String refSerialize;// 序列化接口
	private String refRmiBeanFactory;// 远程调用接口
	private String refServerMessageHandleFactory;// 远程服务处理接口
	private String refClientMessageHandleFactory;// 远程服务处理接口

	public String getAddress() {
		return address;
	}

	public ProtocolModel setAddress(String address) {
		this.address = address;
		return this;
	}

	public int getPort() {
		return port;
	}

	public ProtocolModel setPort(int port) {
		this.port = port;
		return this;
	}

	public int getNetWorkThreads() {
		return netWorkThreads;
	}

	public ProtocolModel setNetWorkThreads(int netWorkThreads) {
		this.netWorkThreads = netWorkThreads;
		return this;
	}

	public int getHandleThreads() {
		return handleThreads;
	}

	public ProtocolModel setHandleThreads(int handleThreads) {
		this.handleThreads = handleThreads;
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public ProtocolModel setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public int getHeartbeat() {
		return heartbeat;
	}

	public ProtocolModel setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
		return this;
	}

	public int getPacketSize() {
		return packetSize;
	}

	public ProtocolModel setPacketSize(int packetSize) {
		this.packetSize = packetSize;
		return this;
	}

	public int getCallBackTimeOut() {
		return callBackTimeOut;
	}

	public ProtocolModel setCallBackTimeOut(int callBackTimeOut) {
		this.callBackTimeOut = callBackTimeOut;
		return this;
	}

	public String getRefSerialize() {
		return refSerialize;
	}

	public ProtocolModel setRefSerialize(String refSerialize) {
		this.refSerialize = refSerialize;
		return this;
	}

	public String getRefRmiBeanFactory() {
		return refRmiBeanFactory;
	}

	public ProtocolModel setRefRmiBeanFactory(String refRmiBeanFactory) {
		this.refRmiBeanFactory = refRmiBeanFactory;
		return this;
	}

	public String getRefServerMessageHandleFactory() {
		return refServerMessageHandleFactory;
	}

	public ProtocolModel setRefServerMessageHandleFactory(String refServerMessageHandleFactory) {
		this.refServerMessageHandleFactory = refServerMessageHandleFactory;
		return this;
	}

	public String getRefClientMessageHandleFactory() {
		return refClientMessageHandleFactory;
	}

	public ProtocolModel setRefClientMessageHandleFactory(String refClientMessageHandleFactory) {
		this.refClientMessageHandleFactory = refClientMessageHandleFactory;
		return this;
	}

}
