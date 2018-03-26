package com.cheuks.bin.original.prototype.spring.cloud.hystrix.server;

import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class HystrixController {

	public String mmx() {
		return "mmx";
	}

	public String aa() {
		int random = new Random().nextInt(100) + 1;
		if (random % 2 != 0)
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return "aa";
	}

	@GetMapping("/a")
	public String a() {
		return new BaseCommandAdapter("a") {

			@Override
			protected String run() throws Exception {
				return a();
			}
		}.execute();
	}

	@HystrixCommand(fallbackMethod = "mmx", groupKey = "mmx", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1"),
			@HystrixProperty(name = "execution.timeout.enabled", value = "true")

	})
	@GetMapping("/b")
	public String b() {
		int random = new Random().nextInt(100) + 1;
		if (random % 2 != 0)
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return "b";
	}

}
