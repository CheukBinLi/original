package com.cheuks.bin.original.common.cache.redis;

public interface RedisCacheTableObject {

	// Entry get(byte[] key);
	//
	// boolean update(byte[] key, byte[] value);
	//
	// boolean reanme(byte[] oldKey, byte[] newKey);
	//
	// void delete(byte[] key);
	<T> T get(String key) throws RedisExcecption;

	boolean put(String key, Object value) throws RedisExcecption;

	boolean containsKey(String key) throws RedisExcecption;

	boolean reanme(String oldKey, String newKey) throws RedisExcecption;

	boolean delete(String key) throws RedisExcecption;

}
