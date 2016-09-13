package com.cheuks.bin.original.cache;

import java.util.Collection;
import java.util.Set;

import com.cheuks.bin.original.cache.redis.JedisClusterFactory;
import com.cheuks.bin.original.common.cache.CacheException;
import com.cheuks.bin.original.common.cache.RedisCacheFactory;

public class JedisClusterCacheFactory extends JedisClusterFactory implements RedisCacheFactory<Object, Object> {

	public Object take(Object key) throws CacheException {
		return get(key);
	}

	
	
	public Object put(Object key, Object value) throws CacheException {
		return getAndSet(key, value);
	}

	public Object remove(Object key) throws CacheException {
		Object o = get(key);
		deleteOO(key);
		return o;
	}

	public void clear() throws CacheException {
		System.err.println("REDIS NOT SUPPORT CLEAR");
	}

	public int size() throws CacheException {
		return -1;
	}

	public Set<Object> keys() throws CacheException {
		System.err.println("REDIS NOT SUPPORT keys");
		return null;
	}

	public Collection<Object> values() throws CacheException {
		System.err.println("REDIS NOT SUPPORT Collection");
		return null;
	}

}
