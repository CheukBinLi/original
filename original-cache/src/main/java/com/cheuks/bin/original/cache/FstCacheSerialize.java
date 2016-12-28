package com.cheuks.bin.original.cache;

import org.nustaq.serialization.FSTConfiguration;

import com.cheuks.bin.original.common.cache.CacheSerialize;

@SuppressWarnings("unchecked")
public class FstCacheSerialize implements CacheSerialize {

	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

	public byte[] encode(Object o) throws Throwable {
		return conf.asByteArray(o);
	}

	public Object decode(byte[] o) throws Throwable {
		return conf.asObject(o);
	}

	public <T> T decodeT(byte[] o) throws Throwable {
		Object result = decode(o);
		return null == result ? null : (T) result;
	}

}
