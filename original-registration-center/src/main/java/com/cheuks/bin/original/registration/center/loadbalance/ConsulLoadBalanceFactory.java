package com.cheuks.bin.original.registration.center.loadbalance;

import java.util.List;

import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.model.RegisterLoadBalanceModel;

public class ConsulLoadBalanceFactory implements LoadBalanceFactory<String, Void> {

	public void setUrl(String url) {
		// TODO Auto-generated method stub

	}

	public List<String> getResource(RegisterLoadBalanceModel registerInfo) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public String getResourceAndUseRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public Void useRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public Void registration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public void cancleRegistration(RegisterLoadBalanceModel registerInfo) throws Throwable {
		// TODO Auto-generated method stub

	}

	public void init() throws Throwable {
		// TODO Auto-generated method stub

	}

}
