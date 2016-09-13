package com.cheuks.bin.original.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.cheuks.bin.original.common.cache.CacheSerialize;


public class DefaultCacheSerialize implements CacheSerialize {

	public byte[] encode(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(o);
		try {
			return baos.toByteArray();
		} finally {
			out.close();
		}
	}

	public Object decode(byte[] o) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(o));
		try {
			return in.readObject();
		} finally {
			in.close();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T decodeT(byte[] o) throws ClassNotFoundException, IOException {
		return (T) decode(o);
	}

}
