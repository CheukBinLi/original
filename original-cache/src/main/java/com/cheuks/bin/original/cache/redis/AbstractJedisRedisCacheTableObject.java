package com.cheuks.bin.original.cache.redis;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.cache.redis.RedisCacheTableObject;
import com.cheuks.bin.original.common.cache.redis.RedisExcecption;
import com.cheuks.bin.original.common.cache.redis.RedisFactory;

/***
 * 表缓存
 * 
 * @author ben
 *
 */
public abstract class AbstractJedisRedisCacheTableObject implements RedisCacheTableObject {

	public abstract RedisFactory getRedisFactory();

	public abstract byte[] getEntryCacheName();

	public abstract CacheSerialize getCacheSerialize();

	public String encode = "utf-8";

	@SuppressWarnings("unchecked")
	public <T> T get(String key) throws RedisExcecption {
		try {
			byte[] result = getRedisFactory().getMapValue(getEntryCacheName(), key.getBytes(encode));
			return null == result ? null : (T) getCacheSerialize().decode(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean containsKey(String key) throws RedisExcecption {
		try {
			return getRedisFactory().mapKeyExists(getEntryCacheName(), key.getBytes(encode));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean put(String key, Object value) throws RedisExcecption {
		try {
			return getRedisFactory().setMap(getEntryCacheName(), key.getBytes(encode), getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean reanme(String oldKey, String newKey) throws RedisExcecption {
		try {
			byte[] result = get(oldKey);
			getRedisFactory().mapRemove(getEntryCacheName(), oldKey.getBytes(encode));
			return getRedisFactory().setMap(getEntryCacheName(), newKey.getBytes(encode), result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean delete(String key) throws RedisExcecption {
		try {
			return getRedisFactory().mapRemove(getEntryCacheName(), key.getBytes(encode));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public String getEncode() throws RedisExcecption {
		return encode;
	}

	public AbstractJedisRedisCacheTableObject setEncode(String encode) {
		this.encode = encode;
		return this;
	}

}
