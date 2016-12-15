package com.cheuks.bin.original.common.cache.redis;

import java.util.Map;

public interface RedisOO {

	public void deleteOO(Object key) throws RedisExcecption;

	public boolean set(Object key, Object value) throws RedisExcecption;

	public boolean set(Object key, Object value, int expireSeconds) throws RedisExcecption;

	public <R> R getAndSet(Object key, Object value) throws RedisExcecption;

	public <R> R get(Object key) throws RedisExcecption;

	public boolean setMap(Object key, Object mapKey, Object value) throws RedisExcecption;

	public <R> R getMapObjectValue(Object key, Object mapKey) throws RedisExcecption;

	public boolean mapKeyExists(Object key, Object mapKey) throws RedisExcecption;

	public Map<byte[], byte[]> getMapObject(Object key) throws RedisExcecption;

	public boolean addListFirst(Object key, Object value) throws RedisExcecption;

	public boolean addListLast(Object key, Object value) throws RedisExcecption;

	public <R> R getListIndex(Object key, int index) throws RedisExcecption;

	public boolean setListIndex(Object key, int index, Object value) throws RedisExcecption;

	public long listLen(Object key) throws RedisExcecption;

	public <R> R popListFirst(Object key) throws RedisExcecption;

	public <R> R popListLast(Object key) throws RedisExcecption;
}
