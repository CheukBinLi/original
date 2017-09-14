package com.cheuks.bin.original.registration.center;

import java.util.List;
import java.util.Map;

import com.cheuks.bin.original.common.registrationcenter.ElectionCallBack;
import com.cheuks.bin.original.common.registrationcenter.RegistrationEventListener;
import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.cache.ServiceHealthKey;
import com.orbitz.consul.model.health.ServiceHealth;

public class ConsulRegistrationFactory implements RegistrationFactory<Consul, Void, Map<ServiceHealthKey, ServiceHealth>> {

	private String serverList = "127.0.0.1:2181";// 127.0.0.1:2181,127.0.0.2:2181,192.168.3.12:2181

	private Consul client;

	public void setUrl(String url) {
		this.serverList = url;
	}

	public Consul getClient() {
		return this.client;
	}

	public String createService(String serviceDirectory, RegistrationEventListener<Void> eventListener) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public String register(String serviceDirectory, String value, RegistrationEventListener<Map<ServiceHealthKey, ServiceHealth>> eventListener) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public String setValue(String serviceDirectory, String value) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public String getValue(String serviceDirectory) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getServiceList(String serviceDirectory) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeServiceDirectory(String serviceDirectory) throws Throwable {
		// TODO Auto-generated method stub

	}

	public boolean isRegister(String serviceDirectory) throws Throwable {
		// TODO Auto-generated method stub
		return false;
	}

	public void election(String ledderDirectory, ElectionCallBack electionCallBack) throws Throwable {
		// TODO Auto-generated method stub

	}

	public void reelect() throws Throwable {
		// TODO Auto-generated method stub

	}

	public void start() throws Throwable {
		if (null == client) {
			synchronized (this) {
				if (null != client) {
					return;
				}
			}
		}
		client = Consul.builder().withHostAndPort(HostAndPort.fromString("[" + serverList + "]")).build();
		// ServiceHealthCache serviceHealthCache = ServiceHealthCache.newCache(client.healthClient(), "serviceName");
		// serviceHealthCache.addListener(new ConsulCache.Listener<ServiceHealthKey, ServiceHealth>() {
		// public void notify(Map<ServiceHealthKey, ServiceHealth> newValues) {
		//
		// }
		//
		// });

	}

}
