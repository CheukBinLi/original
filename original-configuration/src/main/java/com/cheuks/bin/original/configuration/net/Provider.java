package com.cheuks.bin.original.configuration.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/***
 * 广播
 * 
 * @author BIN
 *
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Provider {

	/***
	 * 广播地址(224.0.0.0—244.0.0.255)
	 */
	private final String radioIP;
	private final int port;
	private final ReceiveHandle receiveHandle;
	private final DecodeAndEncode decodeAndEncode;
	private SocketAddress socketAddress;

	private DatagramChannel channel;

	private Thread listening;

	private volatile boolean isShutdown = false;

	private synchronized void init() throws IOException {
		if (!isShutdown || null != channel || channel.isConnected())
			return;
		isShutdown = false;
		socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), port);
		channel = DatagramChannel.open(StandardProtocolFamily.INET);
		channel.setOption(StandardSocketOptions.SO_REUSEADDR, true).bind(new InetSocketAddress(port));
		// 允许接收自己发送出去的数据报
		// datagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, true);
		channel.configureBlocking(false);

		channel.join(InetAddress.getByName(radioIP), NetworkInterface.getByName("net4"));
		// channel.receive(dst)
		listening = new Thread(new Runnable() {

			public void run() {
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				byte[] data;
				while (!isShutdown) {
					try {
						buffer.clear();
						SocketAddress socketAddress = channel.receive(buffer);
						if (null != socketAddress) {
							data = new byte[buffer.position()];
							buffer.flip();
							buffer.get(data);
							receiveHandle.receiveEvent(decodeAndEncode.decode(data));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		listening.start();
	}

	public int send(final byte[] data) throws IOException {
		return channel.send(ByteBuffer.wrap(data), socketAddress);
	}

	public void start() throws Throwable {
		init();
	}

	public void stop() {
		if (null == channel)
			return;
		isShutdown = true;
		listening.interrupt();
		try {
			channel.disconnect();
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		channel = null;
	}

	public static void main(String[] args) throws Throwable {
		// new Radio(radioIP, port, receiveHandle, decodeAndEncode)
		Provider radio = new Provider("224.1.1.1", 11010, new DefaultReceiveHandle(), new DefaultDecodeAndEncode());
		radio.start();
		for (int i = 0, len = 10; i < len; i++) {
			Thread.sleep(1000);
			radio.send(("叼拿升_" + i).getBytes());
		}
	}
}
