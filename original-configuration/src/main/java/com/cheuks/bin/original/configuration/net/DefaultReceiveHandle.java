package com.cheuks.bin.original.configuration.net;

public class DefaultReceiveHandle implements ReceiveHandle<String> {

	public void receiveEvent(String msg) {
		System.out.println(msg);
	}

}
