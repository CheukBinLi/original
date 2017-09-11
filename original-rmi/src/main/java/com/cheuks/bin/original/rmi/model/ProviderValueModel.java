package com.cheuks.bin.original.rmi.model;

public class ProviderValueModel implements Comparable<ProviderValueModel> {
	private String serverName;
	private String url;
	private int connections;

	public String getServerName() {
		return serverName;
	}
	public ProviderValueModel setServerName(String serverName) {
		this.serverName = serverName;
		return this;
	}
	public String getUrl() {
		return url;
	}
	public ProviderValueModel setUrl(String url) {
		this.url = url;
		return this;
	}
	public int getConnections() {
		return connections;
	}
	public ProviderValueModel connectionsAdd(int i) {
		this.connections += i;
		return this;
	}
	public ProviderValueModel setConnections(int connections) {
		this.connections = connections;
		return this;
	}

	public int compareTo(ProviderValueModel o) {
		return (connections - o.connections);
	}

	public String getValue() {
		return this.serverName + "@" + this.url;
	}
	public ProviderValueModel(String value, int connection) {
		if (value.contains("@")) {
			String[] values = value.split("@");
			this.serverName = values[0];
			this.url = values[1];
		}
		this.connections = connection;
	}

	public ProviderValueModel(String serverName, String url) {
		this.serverName = serverName;
		this.url = url;
	}
	public ProviderValueModel(String serverName, String url, int connections) {
		this.serverName = serverName;
		this.url = url;
		this.connections = connections;
	}

	public ProviderValueModel() {
		super();
	}

}
