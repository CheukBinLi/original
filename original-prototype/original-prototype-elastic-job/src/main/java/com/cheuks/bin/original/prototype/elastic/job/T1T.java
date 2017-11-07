package com.cheuks.bin.original.prototype.elastic.job;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class T1T {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ac=new ClassPathXmlApplicationContext("application-config.xml");
		ac.start();
	}
	
}
