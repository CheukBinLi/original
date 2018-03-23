package com.cheuks.bin.original.prototype.spring.cloud.eureka.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

	@SuppressWarnings("unused")
	@Autowired
	private DiscoveryClient client;

	@RequestMapping(value = "/getname", method = RequestMethod.GET)
	public String getName() {
		// ServiceInstance instance = client.getLocalServiceInstance();
		return "good good studuct.";
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String info() {
		return "success";
	}
}
