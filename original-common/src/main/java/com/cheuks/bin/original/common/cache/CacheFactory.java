package com.cheuks.bin.original.common.cache;

import java.util.Collection;
import java.util.Set;

/***
 * 缓存接口
 * 
 * @author ben
 *
 * @param <K>
 * @param <V>
 */
public interface CacheFactory<K, V> {

	/** 取出 */
	public V take(K key) throws CacheException;

	public V put(K key, V value) throws CacheException;

	public V remove(K key) throws CacheException;

	public void clear() throws CacheException;

	public int size() throws CacheException;

	public Set<K> keys() throws CacheException;

	public Collection<V> values() throws CacheException;
}
