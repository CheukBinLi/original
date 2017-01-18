package com.cheuks.bin.original.common.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;

import javax.swing.JFrame;

public class FileUtil extends JFrame {

	private RandomAccessFile randomAccessFile;

	public FileChannel getFileChannel(String path) throws FileNotFoundException {
		randomAccessFile = new RandomAccessFile(path, "r");
		return randomAccessFile.getChannel();
	}

	public Socket connection(String ip, int port, int timeoutSecond) throws IOException {
		final Socket socket = new Socket();
		socket.connect(new InetSocketAddress(ip, port), timeoutSecond * 1000);
		return socket;
	}

	public void sendFile(File file, final Socket socket, int bufferSize) throws IOException {

		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		InputStream in;
		byte[] buffer = new byte[bufferSize > 1024 ? bufferSize : 1024];
		final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
		System.out.println("开始");
		try {
			int len;
			 out.writeInt(1);
			out.writeLong(file.length());

			while ((len = randomAccessFile.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

		} finally {
			randomAccessFile.close();
		}
		out.flush();
		out.close();
		System.err.println("完成");
	}

	public void receiveFile() throws IOException {
		final ServerSocket serverSocket = new ServerSocket();
		// final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buffer = new byte[50000];
		serverSocket.bind(new InetSocketAddress(5001));

		Thread t = new Thread(new Runnable() {
			public void run() {
				System.out.println("服务器启动");
				try {
					FileOutputStream out;
					DataInputStream in;
					long length = 0;
					int count = 0;
					int tempLength;
					while (true) {
						Socket client = serverSocket.accept();
						in = new DataInputStream(client.getInputStream());
						out = new FileOutputStream("/Users/ben/Desktop/core_keygen_dmg.zip1");
						length = in.readLong();
						count = (length % 50000) > 0 ? 1 : 0;
						if (length > 50000) {
							count += (int) (length / 50000);
						}
						System.out.println("文件大小：" + length);
						while (count-- > 0) {
							tempLength = in.read(buffer);
							out.write(buffer, 0, tempLength);
						}
						out.flush();
						out.close();
						System.out.println("接收完成");
						System.out.print(".....");
						client.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		t.start();
	}

	// public void

	public static void main(String[] args) throws IOException {
		FileUtil fu = new FileUtil();
		fu.sendFile(new File("/Users/ben/Downloads/wwwwwwww/rom/sun.cia"), fu.connection("192.168.3.6", 5000, 5), 1000);
//		fu.sendFile(new File("/Users/ben/Downloads/mh1.2.cia"), fu.connection("192.168.3.35", 5000, 5), 1000);
		// fu.sendFile(new File("/Users/ben/Downloads/FBI_1.4.17_chen_fix.cia"), fu.connection("192.168.3.6", 5000, 5), 1000);
		// fu.receiveFile();
	}

}
