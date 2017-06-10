package com.cheuks.bin.original.test.circuitbreaker;

public class CircuitBreakerModel {

	private int maxCount;
	private int successCount;
	private int failCount;
	private int status;

	public int getMaxCount() {
		return maxCount;
	}

	public CircuitBreakerModel setMaxCount(int maxCount) {
		this.maxCount = maxCount;
		return this;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public CircuitBreakerModel setSuccessCount(int successCount) {
		this.successCount = successCount;
		return this;
	}

	public int getFailCount() {
		return failCount;
	}

	public CircuitBreakerModel setFailCount(int failCount) {
		this.failCount = failCount;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public CircuitBreakerModel setStatus(int status) {
		this.status = status;
		return this;
	}

}
