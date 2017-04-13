package com.cheuks.bin.original.test.rmi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RmiServer {

	
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("rmi/application-config.xml");
		applicationContext.start();
		
	}
	
	
}
