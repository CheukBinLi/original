package com.cheuks.bin.original.rmi.model;

public class ConsumerValueModel {

	private String consumerName;
	private String serviceName;
	private String serverName;
	private String serverUrl;
	private String consumerUrl;
	public String getConsumerName() {
		return consumerName;
	}
	public ConsumerValueModel setConsumerName(String consumerName) {
		this.consumerName = consumerName;
		return this;
	}
	public String getServiceName() {
		return serviceName;
	}
	public ConsumerValueModel setServiceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}

	public String getServerName() {
		return serverName;
	}
	public ConsumerValueModel setServerName(String serverName) {
		this.serverName = serverName;
		return this;
	}
	public String getServerUrl() {
		return serverUrl;
	}
	public ConsumerValueModel setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
		return this;
	}

	public String getConsumerUrl() {
		return consumerUrl;
	}
	public ConsumerValueModel setConsumerUrl(String consumerUrl) {
		this.consumerUrl = consumerUrl;
		return this;
	}
	public ConsumerValueModel(String consumerName, String serviceName) {
		super();
		this.consumerName = consumerName;
		this.serviceName = serviceName;
	}
	public ConsumerValueModel() {
		super();
	}

}
