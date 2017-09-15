package com.cheuks.bin.original.rmi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.rmi.t.test2I;

@Component
public class RmiServer {

	@Autowired
	private test2I test2i;

	public static void main(String[] args) throws InterruptedException {

		// PropertyConfigurator.configure("log4j.properties");
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("rmi/application-config.xml");
		applicationContext.start();

		// SimpleRmiService simpleRmiService = applicationContext.getBean(SimpleRmiService.class);
		//
		// SimpleRmiBeanFactory factory = applicationContext.getBean(SimpleRmiBeanFactory.class);
		//
		// Thread.sleep(5000);
		// // test2I t = (test2I) applicationContext.getBean("CCTV2");
		// // System.err.println(t.a3("xxx", 10));
		//

		RmiServer rmiServer = applicationContext.getBean(RmiServer.class);

		System.err.println(rmiServer.test2i.a4("xxxxxxxxxxxxx"));
		// NettyRmiInvokeClientImpl r=applicationContext.getBean(NettyRmiInvokeClientImpl.class);
		// System.err.println(r);

		Thread.sleep(13000); 
		
		System.err.println(rmiServer.test2i.a4("换服"));
	}

}
