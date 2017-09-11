package com.cheuks.bin.original.rmi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cheuks.bin.original.rmi.net.SimpleRmiService;
import com.cheuks.bin.original.rmi.t.test2I;

public class RmiServer {

	public static void main(String[] args) {

		// PropertyConfigurator.configure("log4j.properties");
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("rmi/application-config.xml");
		applicationContext.start();

		SimpleRmiService simpleRmiService = applicationContext.getBean(SimpleRmiService.class);

		test2I t = (test2I) applicationContext.getBean("CCTV-1");
		System.err.println(t.a3("xxx", 10));
		SimpleRmiBeanFactory factory = applicationContext.getBean(SimpleRmiBeanFactory.class);

	}

}
