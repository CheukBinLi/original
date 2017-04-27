package com.cheuks.bin.original.test.rmi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cheuks.bin.original.reflect.config.ProtocolConfig;

public class RmiServer {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("rmi/application-config.xml");
		applicationContext.start();
		ProtocolConfig protocolConfig = applicationContext.getBean(ProtocolConfig.class);
		System.out.println(protocolConfig.getHost() + ":" + protocolConfig.getPort());

	}

}
