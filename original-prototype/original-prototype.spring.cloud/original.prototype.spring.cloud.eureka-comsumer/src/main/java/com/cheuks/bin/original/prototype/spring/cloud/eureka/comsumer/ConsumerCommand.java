package com.cheuks.bin.original.prototype.spring.cloud.eureka.comsumer;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public abstract class ConsumerCommand extends HystrixCommand<String> {

	public ConsumerCommand() {
		super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"), 500000);
	}

	@Override
	protected String getFallback() {
		return "server is busy";
	}

}
