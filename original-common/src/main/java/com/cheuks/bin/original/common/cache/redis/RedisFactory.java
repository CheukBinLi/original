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
		return StringUtil.isBlank(slotKey) ? key : "{" + slotKey + "}" + key;
	}
	
	default byte[] generateScriptBytesKey(String slotKey, String key) {
		return (StringUtil.isBlank(slotKey) ? key : "{" + slotKey + "}" + key).getBytes();
	}

	default String[] generateScriptKeys(String slotKey, int count, String... keys) {
		if (StringUtil.isBlank(slotKey))
			return keys;
		for (int i = 0, len = keys.length; i < len; i++) {
			keys[i] = "{" + slotKey + "}" + keys[i];
		}
		return keys;
	}

	default byte[][] generateScriptBytesKeys(String slotKey, int count, String... keys) {
		byte[][] result = new byte[keys.length][];
		boolean isBlank = StringUtil.isBlank(slotKey);
		for (int i = 0, len = keys.length; i < len; i++) {
			result[i] = ((isBlank || i >= count) ? keys[i] : "{" + slotKey + "}" + keys[i]).getBytes();
		}
		return result;
	}
}
