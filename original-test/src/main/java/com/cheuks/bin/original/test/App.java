package com.cheuks.bin.original.test;

import java.net.URLEncoder;

import org.junit.Test;

/**
 * Hello world!
 *
 */
public class App {
	// public static void main(String[] args) {
	// ApplicationContext ac = new ClassPathXmlApplicationContext("application-config.xml");
	// DoThing doThing = (DoThing) ac.getBean("doThing");
	// doThing.MM();
	// }

	@Test
	public void httpsEncode() {
		String str = "https://www.baidu.com/";
		System.out.println(URLEncoder.encode(str));
	}

}
