package com.cheuks.bin.original.common.cache.redis;

import com.cheuks.bin.original.common.cache.CacheSerialize;

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

}
