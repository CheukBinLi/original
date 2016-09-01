package com.cheuks.bin.original.common.cache.redis;

import java.util.List;
import java.util.Map;

public interface RedisCommand {

	public void delete(String key) throws RedisExcecption;

	public boolean exists(String key) throws RedisExcecption;

	public boolean set(String key, String value) throws RedisExcecption;

	public boolean set(String key, String value, int expireSeconds) throws RedisExcecption;

	/***
	 * 运算操作: +value
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws RedisExcecption
	 */
	public boolean incr(String key, Integer value) throws RedisExcecption;

	/***
	 * 运算操作: +value
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws RedisExcecption
	 */
	public boolean incrByMap(String key, String mapKey, Integer value) throws RedisExcecption;

	public String getAndSet(String key, String value) throws RedisExcecption;

	public String get(String key) throws RedisExcecption;

	public List<String> get(String... key) throws RedisExcecption;

	/**
	 * 设置数据有效时间 /***
	 * 
	 * Set<Map<mapKey,value>>
	 * 
	 * @param key
	 * @param mapKey
	 * @param value
	 * @return
	 */
	public boolean setMap(String key, String mapKey, String value) throws RedisExcecption;

	public boolean setMap(String key, Map<String, String> map) throws RedisExcecption;

	public boolean mapKeyExists(String key, String mapKey) throws RedisExcecption;

	public String getMapValue(String key, String mapKey) throws RedisExcecption;

	public boolean mapRemove(String key, String... mapKey) throws RedisExcecption;

	public List<String> getListByString(String key, int start, int end) throws RedisExcecption;

	/** List */

	public boolean addListFirst(String key, String... value) throws RedisExcecption;

	public boolean addListLast(String key, String... value) throws RedisExcecption;

	/***
	 * 删除列表，指定行除外
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public boolean removeListWithoutFor(String key, int start, int end) throws RedisExcecption;

	/***
	 * 修改指定行
	 * 
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	public boolean setListIndex(String key, int index, String value) throws RedisExcecption;

	public String getListIndex(String key, int index) throws RedisExcecption;

	/***
	 * 获取列表长度
	 * 
	 * @param key
	 * @return
	 */
	public long listLen(String key) throws RedisExcecption;

	public String popListFirst(String key) throws RedisExcecption;

	public Object eval(String script, int keysCount, String... params) throws RedisExcecption;

	public Object eval(String script, List<String> keys, List<String> params) throws RedisExcecption;

}
