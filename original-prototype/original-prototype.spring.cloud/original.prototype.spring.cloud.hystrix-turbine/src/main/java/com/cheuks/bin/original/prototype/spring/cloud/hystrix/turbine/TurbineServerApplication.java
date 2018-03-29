package com.cheuks.bin.original.prototype.spring.cloud.hystrix.turbine;

import javax.servlet.http.HttpServlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;
import org.springframework.context.annotation.Bean;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@EnableTurbine
@EnableTurbineStream
@EnableDiscoveryClient
@SpringBootApplication
@EnableEurekaClient
public class TurbineServerApplication {

	public static void main(String[] args) {
//		com.netflix.turbine.aggregator.InstanceKey
		SpringApplication.run(TurbineServerApplication.class, args);
	}

	@Bean
	public ServletRegistrationBean<HttpServlet> hystrixMetricsStreamServlet() {
		return new ServletRegistrationBean<HttpServlet>(new HystrixMetricsStreamServlet(), "/hystrix.stream");
	}
}
