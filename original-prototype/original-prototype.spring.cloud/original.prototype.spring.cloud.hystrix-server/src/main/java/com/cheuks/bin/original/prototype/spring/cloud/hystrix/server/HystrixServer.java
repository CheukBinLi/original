package com.cheuks.bin.original.prototype.spring.cloud.hystrix.server;

import javax.servlet.http.HttpServlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@EnableDiscoveryClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrixDashboard
public class HystrixServer {

	public static void main(String[] args) {
		SpringApplication.run(HystrixServer.class, args);
	}

	@Bean
	public ServletRegistrationBean<HttpServlet> hystrixMetricsStreamServlet() {
		return new ServletRegistrationBean<HttpServlet>(new HystrixMetricsStreamServlet(), "/hystrix.stream");
	}
}
