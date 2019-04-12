package com.cheuks.bin.original.common.cache.redis;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.util.conver.StringUtil;

public interface RedisFactory extends RedisScript, RedisBinary, RedisCommand, RedisObject, RedisOO {

	CacheSerialize getCacheSerialize();
	
    void setCacheSerialize(CacheSerialize cacheSerialize);

    void setHost(String serverList);

    void setPassword(String password);

    void setSoTimeOut(int soTimeOut);

    void setPort(int port);

    void setTestOnBorrow(boolean testOnBorrow);

    void setExpireSecond(int expireSecond);

    void setEncoding(String encoding);

	/***
	 * 生成标准KEY（分布式时必须使用：标准化KEY）
	 * 
	 * @param slotKey
	 *            卡槽KEY
	 * @param key
	 *            脚本KEY
	 * @return
	 */
	default String generateScriptKey(String slotKey, String key) {
		return StringUtil.isBlank(slotKey) ? key : ("{" + slotKey + "}" + key);
	}
	
}
