package com.cheuks.bin.original.test.nginx_tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws IOException {
		Socket s = new Socket();
		s.connect(new InetSocketAddress("10.73.11.87", 10010));
		InputStream in = s.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[512];
		while (-1 != (len = in.read(buffer))) {
			out.write(buffer, 0, len);
		}
		System.out.println(new String(out.toByteArray()));
	}

}
