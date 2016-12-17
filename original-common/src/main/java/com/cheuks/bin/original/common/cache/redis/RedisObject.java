package com.cheuks.bin.original.common.cache.redis;

import java.util.Map;

public interface RedisObject {

	public boolean setObject(String key, Object value) throws RedisExcecption;

	public boolean setObject(String key, Object value, int expireSeconds) throws RedisExcecption;

	public <R> R getAndSetObject(String key, Object value) throws RedisExcecption;

	public <R> R getObject(String key) throws RedisExcecption;

	public boolean setMapObject(String key, Object mapKey, Object value) throws RedisExcecption;

	public boolean mapKeyExistsObject(String key, Object mapKey) throws RedisExcecption;

	public Map<byte[], byte[]> getMapString(String key) throws RedisExcecption;

	public boolean addListFirstObject(String key, Object value) throws RedisExcecption;

	public boolean addListLastObject(String key, Object value) throws RedisExcecption;

	public <R> R getListIndexObject(String key, int index) throws RedisExcecption;

	public boolean setListIndexObject(String key, int index, Object value) throws RedisExcecption;

	public long listLenObject(String key) throws RedisExcecption;

	public <R> R popListFirstObject(String key) throws RedisExcecption;

	public <R> R popListLastObject(String key) throws RedisExcecption;

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
	public long removeListObject(String key, Object value, int count) throws RedisExcecption;
}
