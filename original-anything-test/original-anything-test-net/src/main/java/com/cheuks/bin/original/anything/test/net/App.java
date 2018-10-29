package com.cheuks.bin.original.anything.test.net;

import java.util.UUID;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		String id = UUID.randomUUID().toString();
		System.out.println(id);
		System.err.println(id.length());
	}
}
