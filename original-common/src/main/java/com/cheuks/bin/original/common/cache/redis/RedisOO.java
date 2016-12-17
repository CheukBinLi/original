package com.cheuks.bin.original.common.cache.redis;

import java.util.Map;

public interface RedisOO {

	public void deleteOO(Object key) throws RedisExcecption;

	public boolean setOO(Object key, Object value) throws RedisExcecption;

	public boolean setOO(Object key, Object value, int expireSeconds) throws RedisExcecption;

	public <R> R getAndSetOO(Object key, Object value) throws RedisExcecption;

	public <R> R getOO(Object key) throws RedisExcecption;

	public boolean setMapOO(Object key, Object mapKey, Object value) throws RedisExcecption;

	public <R> R getMapObjectValue(Object key, Object mapKey) throws RedisExcecption;

	public boolean mapKeyExistsOO(Object key, Object mapKey) throws RedisExcecption;

	public Map<byte[], byte[]> getMapOO(Object key) throws RedisExcecption;

	public boolean addListFirstOO(Object key, Object value) throws RedisExcecption;

	public boolean addListLastOO(Object key, Object value) throws RedisExcecption;

	public <R> R getListIndexOO(Object key, int index) throws RedisExcecption;

	public boolean setListIndexOO(Object key, int index, Object value) throws RedisExcecption;

	public long listLenOO(Object key) throws RedisExcecption;

	public <R> R popListFirstOO(Object key) throws RedisExcecption;

	public <R> R popListLastOO(Object key) throws RedisExcecption;

	/***
	 * 删除列表中的值
	 * 
	 * @param key
	 * @param value
	 * @param count
	 *            删除个数 正数：从头部至尾部进行移除，负数：从尾部至头部进行移除
	 * @return
	 * @throws RedisExcecption
	 */
	public long removeListOO(Object key, Object value, int count) throws RedisExcecption;
}
