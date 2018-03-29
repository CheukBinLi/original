package com.cheuks.bin.original.prototype.spring.cloud.eureka.comsumer;

import javax.servlet.http.HttpServlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.bootstrap.BootstrapConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@EnableDiscoveryClient
@SpringBootApplication
// @EnableCircuitBreaker
@BootstrapConfiguration
@EnableHystrixDashboard
@EnableHystrix
@RefreshScope
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// @Bean
	// @ConditionalOnMissingBean(HystrixCommandAspect.class)
	// HystrixCommandAspect hystrixCommandAspect() {
	// // org/aspectj/lang/JoinPoint
	// return new HystrixCommandAspect();
	// }

	@Bean
	public ServletRegistrationBean<HttpServlet> hystrixMetricsStreamServlet() {
		return new ServletRegistrationBean<HttpServlet>(new HystrixMetricsStreamServlet(), "/hystrix.stream");
	}

}
