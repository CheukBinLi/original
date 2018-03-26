package com.cheuks.bin.original.prototype.spring.cloud.hystrix.server;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

public abstract class BaseCommandAdapter extends HystrixCommand<String> {

	public BaseCommandAdapter(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool,
			int executionIsolationThreadTimeoutInMilliseconds) {
		super(group, threadPool, executionIsolationThreadTimeoutInMilliseconds);
	}

	public BaseCommandAdapter(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool) {
		super(group, threadPool);
	}

	public BaseCommandAdapter(HystrixCommandGroupKey group, int executionIsolationThreadTimeoutInMilliseconds) {
		super(group, executionIsolationThreadTimeoutInMilliseconds);
	}

	public BaseCommandAdapter(HystrixCommandGroupKey group) {
		super(group);
	}

	public BaseCommandAdapter(Setter setter) {
		super(setter);
	}

	public BaseCommandAdapter(String groupName) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupName)));
	}

	@Override
	protected String getFallback() {
		return "server is busy,try later.";
	}

}
