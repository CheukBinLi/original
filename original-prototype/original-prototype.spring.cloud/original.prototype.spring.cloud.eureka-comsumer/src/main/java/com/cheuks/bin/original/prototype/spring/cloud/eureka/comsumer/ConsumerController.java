package com.cheuks.bin.original.prototype.spring.cloud.eureka.comsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class ConsumerController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	DiscoveryClient client;

	@GetMapping("/")
	public String homePage() {
		return "homePage";
	}

	@GetMapping("/info")
	public String info() {
		return "success";
	}

	@GetMapping("/health")
	public String health() {
		return "success";
	}

	@Value("${comsumer.username:nil}")
	private String users;

	@Value("${mp.post.create.QrCode:nil}")
	private String qrCodeUrl;

	@GetMapping("/users")
	public String users() {
		return this.users + "  qrCodeUrl: " + qrCodeUrl;
	}

	@HystrixCommand(fallbackMethod = "getName", groupKey = "mmx", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1"),
			@HystrixProperty(name = "execution.timeout.enabled", value = "true")

	})
	@GetMapping("/hs")
	public String histixTest() {
		return "histixTest";
	}

	@GetMapping("/getname")
	public String getName() {
		// System.err.println(restTemplate.getForObject("http://PROVIDER-SERVICE-0/getname",
		// String.class));
		try {
			return new ConsumerCommand() {

				@Override
				protected String run() throws Exception {
					// System.err.println(getExecutionTimeInMilliseconds());
					return restTemplate.getForObject("http://PROVIDER-SERVICE-0/getname", String.class);
				}
			}.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
