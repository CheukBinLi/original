package com.cheuks.bin.original.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("application-config.xml");
		DoThing doThing = (DoThing) ac.getBean("doThing");
		doThing.MM();
	}
}
