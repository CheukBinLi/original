package com.cheuks.bin.original.rmi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Unit test for simple App.
 */
public class AppTest {

	public static void main(String[] args) throws UnknownHostException, IOException {
		InetSocketAddress inetSocketAddress = new InetSocketAddress("10.73.18.96", 1001);
		System.err.println(new Socket("10.73.18.96", 1011));

	}
}
