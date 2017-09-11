package com.cheuks.bin.original.rmi.config.model;

import java.io.Serializable;

public class ProtocolModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String localName;// 名称
	private String localAddress;// 地址
	private Integer port;// 端口
	private Integer netWorkThreads;// 网络你监听线程数
	private Integer handleThreads;// 处理线程数
	private String charset;// 编码
	private Integer frameLength;// 数据侦块大小
	private Integer heartbeat;// 心跳间隔
	private Integer packetSize;// 最大请求数据长度
	private Integer callBackTimeOut;// 回调超时，默认1分钟
	private String refSerialize;// 序列化接口
	private String refRmiBeanFactory;// 远程调用接口
	private String refServerMessageHandleFactory;// 远程服务处理接口
	private String refClientMessageHandleFactory;// 远程服务处理接口

	public String getLocalName() {
		return localName;
	}

	public ProtocolModel setLocalName(String localName) {
		this.localName = localName;
		return this;
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public ProtocolModel setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
		return this;
	}

	public Integer getPort() {
		return port;
	}

	public ProtocolModel setPort(Integer port) {
		this.port = port;
		return this;
	}

	public Integer getNetWorkThreads() {
		return netWorkThreads;
	}

	public ProtocolModel setNetWorkThreads(Integer netWorkThreads) {
		this.netWorkThreads = netWorkThreads;
		return this;
	}

	public Integer getHandleThreads() {
		return handleThreads;
	}

	public ProtocolModel setHandleThreads(Integer handleThreads) {
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

	public Integer getFrameLength() {
		return frameLength;
	}

	public ProtocolModel setFrameLength(Integer frameLength) {
		this.frameLength = frameLength;
		return this;
	}

	public Integer getHeartbeat() {
		return heartbeat;
	}

	public ProtocolModel setHeartbeat(Integer heartbeat) {
		this.heartbeat = heartbeat;
		return this;
	}

	public Integer getPacketSize() {
		return packetSize;
	}

	public ProtocolModel setPacketSize(Integer packetSize) {
		this.packetSize = packetSize;
		return this;
	}

	public Integer getCallBackTimeOut() {
		return callBackTimeOut;
	}

	public ProtocolModel setCallBackTimeOut(Integer callBackTimeOut) {
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
