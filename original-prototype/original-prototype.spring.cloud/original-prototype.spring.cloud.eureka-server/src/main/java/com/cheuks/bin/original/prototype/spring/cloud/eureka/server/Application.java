package com.cheuks.bin.original.prototype.spring.cloud.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;

@SpringBootApplication
@EnableEurekaServer
@ComponentScan
@ImportAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Filter httpTraceFilter() {
		return new CommonsRequestLoggingFilter();
	}

}
