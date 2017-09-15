package com.cheuks.bin.original.rmi.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel;

public class P2pLoadBalanceFactory implements LoadBalanceFactory<String, Void> {

	private String serverList;

	private List<String> serverResource;

	public void setUrl(String url) {
		this.serverList = url;
	}

	public List<String> getResource(RegisterLoadBalanceModel registerInfo) throws Throwable {
		return getServerResource();
	}

	public String getResourceAndUseRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		return null;
	}

	public Void useRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		return null;
	}

	public Void registration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		return null;
	}

	public void cancleRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable {

	}

	public void init() throws Throwable {

	}

	protected List<String> getServerResource() {
		if (null == serverResource) {
			String serverName;
			try {
				InetAddress inetAddress = InetAddress.getLocalHost();
				serverName = inetAddress.getHostName();
			} catch (UnknownHostException e) {
				serverName = "default_p2p_server";
			}
			serverResource = Arrays.asList(serverName + "@" + serverList);
		}
		return serverResource;
	}
}
