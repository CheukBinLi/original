package com.cheuks.bin.original.reflect.rmi;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerLoad implements Comparable<ServerLoad> {
	private String serverName;
	private String serverUrl;
	private final AtomicInteger loadCount = new AtomicInteger();

	public String getServerName() {
		return serverName;
	}

	public ServerLoad setServerName(String serverName) {
		this.serverName = serverName;
		return this;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public ServerLoad setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
		return this;
	}

	public AtomicInteger getLoadCount() {
		return loadCount;
	}

	public ServerLoad setLoadCount(int loadCount) {
		this.loadCount.set(loadCount);
		return this;
	}

	public synchronized int addLoadCount(int count) {
		return this.loadCount.addAndGet(count);
	}

	public ServerLoad(String serverName, String serverUrl, int loadCount) {
		super();
		this.serverName = serverName;
		this.serverUrl = serverUrl;
		this.loadCount.set(loadCount);
	}

	public int compareTo(ServerLoad o) {
		return (loadCount.get() - o.loadCount.get());
	}
}