package com.cheuks.bin.original.prototype.spring.cloud.eureka.comsumer;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	DiscoveryClient client;

	@GetMapping("/info")
	public String info() {
		System.err.println(Arrays.toString(client.getServices().toArray()));
		return "success";
	}

	@GetMapping("/getname")
	public String getName() {
		return restTemplate.getForObject("http://PROVIDER-SERVICE-0/getname", String.class);
	}

}
