package com.cheuks.bin.original.test.springconfig;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class T1 {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext application = new ClassPathXmlApplicationContext("/springconfig/application-config.xml");
		application.start();

		Ioc ioc = application.getBean(Ioc.class);
		
		System.out.println(ioc.getA());

	}

}
