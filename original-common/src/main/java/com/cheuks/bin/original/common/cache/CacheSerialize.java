package com.cheuks.bin.original.common.cache;

public interface CacheSerialize {

	byte[] encode(Object o) throws Throwable;

	Object decode(byte[] o) throws Throwable;

	<T> T decodeT(byte[] o) throws Throwable;

}
