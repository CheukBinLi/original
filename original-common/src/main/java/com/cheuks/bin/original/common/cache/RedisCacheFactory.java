package com.cheuks.bin.original.common.cache;

import com.cheuks.bin.original.common.cache.redis.RedisFactory;

/***
 * 带redis特性缓存接口
 * 
 * @author ben
 *
 * @param <K>
 * @param <V>
 */
public interface RedisCacheFactory<K, V> extends CacheFactory<K, V>, RedisFactory {

}
