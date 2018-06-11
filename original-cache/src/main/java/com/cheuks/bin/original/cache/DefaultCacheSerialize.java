package com.cheuks.bin.original.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.cheuks.bin.original.common.cache.CacheException;
import com.cheuks.bin.original.common.cache.CacheSerialize;

public class DefaultCacheSerialize implements CacheSerialize {

	public byte[] encode(Object o) throws CacheException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(o);
			return baos.toByteArray();
		} catch (Throwable e) {
			throw new CacheException(e);
		} finally {
			try {
				if (null != out)
					out.close();
			} catch (IOException e) {
			}
		}
	}

	public Object decode(byte[] o) throws CacheException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new ByteArrayInputStream(o));
			return in.readObject();
		} catch (Throwable e) {
			throw new CacheException(e);
		} finally {
			try {
				if (null != in)
					in.close();
			} catch (IOException e) {
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T decodeT(byte[] o) throws CacheException {
		return (T) decodeT(o, null);
	}

	@SuppressWarnings("unchecked")
	public <T> T decodeT(byte[] o, Class<T> t) throws CacheException {
		return (T) decode(o);
	}

}
