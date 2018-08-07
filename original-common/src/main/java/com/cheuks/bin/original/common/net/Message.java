package com.cheuks.bin.original.common.net;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 4160389084283929387L;

	private long id;
	private int length;

	public static void main(String[] args) {
		System.err.println(0x000E);
		System.err.println(Integer.MAX_VALUE);
		System.err.println(Integer.toBinaryString(Integer.MAX_VALUE).length());
		byte[] b = intToByteArray(Integer.MAX_VALUE);
		System.err.println(intToByteArray(Integer.MAX_VALUE).length);
		System.err.println(byteArrayToInt(b));
		byte[] l = LongToBytes(Long.MAX_VALUE);
		System.err.println(Long.MAX_VALUE);
		System.err.println(LongToBytes(Long.MAX_VALUE).length);
		System.err.println(BytesToLong(l));
	}

	public static byte[] intToByteArray(int a) {
		return new byte[] { (byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF), (byte) (a & 0xFF) };
	}

	public static int byteArrayToInt(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}

	public static byte[] LongToBytes(long values) {
		byte[] buffer = new byte[8];
		for (int i = 0; i < 8; i++) {
			int offset = 64 - (i + 1) * 8;
			buffer[i] = (byte) ((values >> offset) & 0xff);
		}
		return buffer;
	}

	public static long BytesToLong(byte[] buffer) {
		long values = 0;
		for (int i = 0; i < 8; i++) {
			values <<= 8;
			values |= (buffer[i] & 0xff);
		}
		return values;
	}
}
