package com.cheuks.bin.original.common.rmi.model;

/***
 * 
 * @Title: original-rmi
 * @Description: 消费者连接信息模型
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年9月12日 上午9:55:37
 *
 */
public class ConsumerValueModel {

	// 服务名
	private String serviceName;
	// 消费者名
	private String consumerName;
	// 消费者连接地址
	private String consumerUrl;
	// 被连接服务器名称
	private String serverName;
	// 被连接服务器地址
	private String serverUrl;
	// 尝试次数
	private int tryAgain;

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

	public int getTryAgain() {
		return tryAgain;
	}
	public ConsumerValueModel setTryAgain(int tryAgain) {
		this.tryAgain = tryAgain;
		return this;
	}
	public ConsumerValueModel addTryAgain(int value) {
		this.tryAgain += value;
		return this;
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
	public ConsumerValueModel(String consumerName, String consumerUrl, String serviceName) {
		super();
		this.consumerName = consumerName;
		this.serviceName = serviceName;
		this.consumerUrl = consumerUrl;
	}
	public ConsumerValueModel() {
		super();
	}

}
