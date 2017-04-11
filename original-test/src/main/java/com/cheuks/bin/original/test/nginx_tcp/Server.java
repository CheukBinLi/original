package com.cheuks.bin.original.test.nginx_tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public static void main(String[] args) throws IOException {
		final int port = 10011;
		ServerSocket ss = new ServerSocket(port);
		ExecutorService executorService = Executors.newCachedThreadPool();
		while (true) {
			final Socket s = ss.accept();
			final OutputStream out = s.getOutputStream();
			if (null != s) {
				executorService.execute(new Runnable() {

					public void run() {
						try {
							out.write(("port:" + port + ",wahaha").getBytes());
							out.flush();
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

}
