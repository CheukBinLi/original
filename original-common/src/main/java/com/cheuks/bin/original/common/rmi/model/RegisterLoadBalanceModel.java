package com.cheuks.bin.original.common.rmi.model;

import java.io.Serializable;
import java.util.Map;

/***
 * 
 * @Title: original-common
 * @Description: 服务注册
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年9月8日 上午11:26:25
 *
 */
public class RegisterLoadBalanceModel implements Serializable {

	private static final long serialVersionUID = -9081330390454362380L;

	public static enum ServiceType {
		client, server, service, provider, consumer, ledder, load
	}

	private String serverName;// 服务注册实例名
	private String serviceName;// 服务名
	private ServiceType type;// 服务类型
	private String value;// 扩展值
	private String url;// 服务地址
	private String healthCheck;// 健康检查地址
	private String desc;// 描述
	private Map<String, Object> additional;// 附加扩展参数

	public String getServerName() {
		return serverName;
	}
	public RegisterLoadBalanceModel setServerName(String serverName) {
		this.serverName = serverName;
		return this;
	}
	public String getServiceName() {
		return serviceName;
	}
	public RegisterLoadBalanceModel setServiceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}
	public ServiceType getType() {
		return type;
	}
	public RegisterLoadBalanceModel setType(ServiceType type) {
		this.type = type;
		return this;
	}

	public String getValue() {
		return value;
	}
	public RegisterLoadBalanceModel setValue(String value) {
		this.value = value;
		return this;
	}
	public String getUrl() {
		return url;
	}
	public RegisterLoadBalanceModel setUrl(String url) {
		this.url = url;
		return this;
	}
	public String getHealthCheck() {
		return healthCheck;
	}
	public RegisterLoadBalanceModel setHealthCheck(String healthCheck) {
		this.healthCheck = healthCheck;
		return this;
	}
	public String getDesc() {
		return desc;
	}
	public RegisterLoadBalanceModel setDesc(String desc) {
		this.desc = desc;
		return this;
	}
	public Map<String, Object> getAdditional() {
		return additional;
	}
	public RegisterLoadBalanceModel setAdditional(Map<String, Object> additional) {
		this.additional = additional;
		return this;
	}

}
