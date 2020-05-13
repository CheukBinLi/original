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

    /**
     * 取出
     */
    V take(K key) throws CacheException;

    V put(K key, V value) throws CacheException;

    V remove(K key) throws CacheException;

    void scriptClear() throws CacheException;

    int size() throws CacheException;

    Set<K> keys() throws CacheException;

    Collection<V> values() throws CacheException;

    default void dectory() {
    }

    default void init() {
    }
}
