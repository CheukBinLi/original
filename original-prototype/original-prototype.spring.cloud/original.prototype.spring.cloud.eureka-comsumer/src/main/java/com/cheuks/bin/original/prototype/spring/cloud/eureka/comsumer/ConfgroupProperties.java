package com.cheuks.bin.original.prototype.spring.cloud.eureka.comsumer;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Configurable
@RefreshScope
@ConfigurationProperties(prefix = "comsumer")
public class ConfgroupProperties {

	private String port;
	private String server;
	private String username;
	private String path;

}
