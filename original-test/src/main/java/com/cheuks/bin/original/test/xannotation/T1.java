package com.cheuks.bin.original.test.xannotation;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class T1 {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("xannotation/application-config.xml");
		ac.start();
	}

}
