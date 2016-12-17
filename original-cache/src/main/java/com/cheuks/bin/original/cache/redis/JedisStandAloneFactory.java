package com.cheuks.bin.original.cache.redis;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.cache.redis.RedisExcecption;
import com.cheuks.bin.original.common.cache.redis.RedisFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SuppressWarnings({ "unchecked", "restriction" })
public class JedisStandAloneFactory implements RedisFactory {

	private static transient final Logger LOG = LoggerFactory.getLogger(JedisStandAloneFactory.class);

	private JedisPoolConfig config;
	private static JedisPool pool;
	private static volatile boolean isInit;
	private int timeOut = 10000;
	private String password;
	private String host = "127.0.0.1";
	private int port = 6379;
	private boolean testOnBorrow;// ping
	private int expireSecond = 1800;// 30分钟
	private String encoding = "UTF-8";

	private CacheSerialize cacheSerialize;

	public CacheSerialize getCacheSerialize() {
		return cacheSerialize;
	}

	public JedisStandAloneFactory setCacheSerialize(CacheSerialize cacheSerialize) {
		this.cacheSerialize = cacheSerialize;
		return this;
	}

	public Jedis getResource() {
		if (!isInit)
			synchronized (this) {
				init();
			}
		return pool.getResource();
	}

	public void destory(Jedis jedis) {
		jedis.close();
	}

	@PostConstruct
	synchronized void init() {
		if (isInit)
			return;
		isInit = true;
		if (null == host || port == 0) {
			LOG.error("host:%s,port:%d is null.", host, port);
			return;
		}
		config = new JedisPoolConfig();
		config.setTestOnBorrow(testOnBorrow);
		pool = ((null != password && password.length() > 1) ? new JedisPool(config, host, port, timeOut, password) : new JedisPool(config, host, port, timeOut));
	}

	public void delete(byte[] key) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			result = jedis.del(key);
			if (LOG.isDebugEnabled())
				LOG.debug("delete:" + result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public byte[] scriptLoad(byte[] key, byte[] script) throws RedisExcecption {
		Jedis jedis = getResource();
		byte[] result;
		try {
			result = jedis.scriptLoad(script);
			if (LOG.isDebugEnabled())
				LOG.debug("scriptLoad:" + (null == result ? null : new String(result)));
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean exists(byte[] key) throws RedisExcecption {
		Jedis jedis = getResource();
		boolean result;
		try {
			result = jedis.exists(key);
			if (LOG.isDebugEnabled())
				LOG.debug("exists:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object evalSha(String sha, String key) throws RedisExcecption {
		Jedis jedis = getResource();
		Object result;
		try {
			result = jedis.evalsha(sha);
			if (LOG.isDebugEnabled())
				LOG.debug("evalSha:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object evalSha(String sha, int keys, String... params) throws RedisExcecption {
		Jedis jedis = getResource();
		Object result;
		try {
			result = jedis.evalsha(sha, keys, params);
			if (LOG.isDebugEnabled())
				LOG.debug("evalSha:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object evalSha(String sha, List<String> keys, List<String> argv) throws RedisExcecption {
		Jedis jedis = getResource();
		Object result;
		try {
			result = jedis.evalsha(sha, keys, argv);
			if (LOG.isDebugEnabled())
				LOG.debug("evalSha:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean scriptExists(String sha, String key) throws RedisExcecption {
		Jedis jedis = getResource();
		boolean result;
		try {
			result = jedis.scriptExists(sha);
			if (LOG.isDebugEnabled())
				LOG.debug("scriptExists:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public void scriptFlush() throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = jedis.scriptFlush();
			if (LOG.isDebugEnabled())
				LOG.debug("scriptFlush:" + result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public void scriptKill() throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = jedis.scriptKill();
			if (LOG.isDebugEnabled())
				LOG.debug("scriptKill:" + result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public String scriptLoad(String key, String script) throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = jedis.scriptLoad(script);
			if (LOG.isDebugEnabled())
				LOG.debug("scriptLoad:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean expireAt(byte[] key, long unixTime) throws RedisExcecption {
		Jedis jedis = getResource();
		long result;
		try {
			result = jedis.expireAt(key, unixTime);
			if (LOG.isDebugEnabled())
				LOG.debug("expireAt:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean expire(byte[] key, int seconds) throws RedisExcecption {
		Jedis jedis = getResource();
		long result;
		try {
			result = jedis.expire(key, seconds);
			if (LOG.isDebugEnabled())
				LOG.debug("expire:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean set(byte[] key, byte[] value) throws RedisExcecption {
		getResource().set(key, value);
		return set(key, value, expireSecond);
	}

	public boolean incr(byte[] key, Integer value) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			result = jedis.incrBy(key, value);
			if (LOG.isDebugEnabled())
				LOG.debug("incr:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean set(byte[] key, byte[] value, int expireSeconds) throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = jedis.setex(key, expireSeconds, value);
			if (LOG.isDebugEnabled())
				LOG.debug("set:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public byte[] getAndSet(byte[] key, byte[] value) throws RedisExcecption {
		Jedis jedis = getResource();
		byte[] result;
		try {
			result = jedis.getSet(key, value);
			jedis.expire(key, expireSecond);
			try {
				return null == result ? null : result;
			} finally {
				if (LOG.isDebugEnabled())
					LOG.debug("get:List<byte[]>:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public byte[] get(byte[] key) throws RedisExcecption {
		Jedis jedis = getResource();
		byte[] result;
		try {
			result = jedis.get(key);
			try {
				return null == result ? null : result;
			} finally {
				if (LOG.isDebugEnabled())
					LOG.debug("get:byte[]:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public List<byte[]> get(byte[]... keys) throws RedisExcecption {
		Jedis jedis = getResource();
		List<byte[]> result;
		try {
			result = jedis.mget(keys);
			try {
				return result;
			} finally {
				if (LOG.isDebugEnabled())
					LOG.debug("get:List<byte[]>:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean setMap(byte[] key, byte[] mapKey, byte[] value) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			result = jedis.hset(key, mapKey, value);
			if (LOG.isDebugEnabled())
				LOG.debug("setMap:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean setMap(byte[] key, Map<byte[], byte[]> map) throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = jedis.hmset(key, map);
			if (LOG.isDebugEnabled())
				LOG.debug("setMap:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public List<byte[]> getMapList(byte[] key, byte[]... subKyes) throws RedisExcecption {
		Jedis jedis = getResource();
		List<byte[]> result;
		try {
			result = jedis.hmget(key, subKyes);
			try {
				return null == result ? null : result;
			} finally {
				if (LOG.isDebugEnabled())
					LOG.debug("getMapList:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Map<byte[], byte[]> getMap(byte[] key) throws RedisExcecption {
		Jedis jedis = getResource();
		Map<byte[], byte[]> result;
		try {
			result = jedis.hgetAll(key);
			try {
				return null == result ? null : result;
			} finally {
				if (LOG.isDebugEnabled())
					LOG.debug("getMap:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean mapKeyExists(byte[] key, byte[] mapKey) throws RedisExcecption {
		Jedis jedis = getResource();
		boolean result;
		try {
			result = jedis.hexists(key, mapKey);
			if (LOG.isDebugEnabled())
				LOG.debug("mapKeyExists:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean incrByMap(byte[] key, byte[] mapKey, Integer value) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			result = jedis.hincrBy(key, mapKey, value);
			if (LOG.isDebugEnabled())
				LOG.debug("incrByMap:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public byte[] getMapValue(byte[] key, byte[] mapKey) throws RedisExcecption {
		Jedis jedis = getResource();
		byte[] result;
		try {
			result = jedis.hget(key, mapKey);
			try {
				return null == result ? null : result;
			} finally {
				if (LOG.isDebugEnabled())
					LOG.debug("getMapValue:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean mapRemove(byte[] key, byte[]... mapKeys) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			result = jedis.hdel(key, mapKeys);
			if (LOG.isDebugEnabled())
				LOG.debug("mapRemove:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public List<byte[]> getListByBytes(byte[] key, int start, int end) throws RedisExcecption {
		Jedis jedis = getResource();
		List<byte[]> result;
		try {
			result = jedis.lrange(key, start, end);
			if (LOG.isDebugEnabled())
				LOG.debug("mapRemove-list:size:" + result.size());
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean addListFirst(byte[] key, byte[]... value) throws RedisExcecption {
		return addList(key, true, value);
	}

	public boolean addListLast(byte[] key, byte[]... value) throws RedisExcecption {
		return addList(key, false, value);
	}

	protected boolean addList(byte[] key, boolean isFirst, byte[]... value) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			if (isFirst)
				result = jedis.lpush(key, value);
			else
				result = jedis.rpush(key, value);
			if (LOG.isDebugEnabled())
				LOG.debug("addList" + (isFirst ? "First:" : "Last:") + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	protected boolean addList(String key, boolean isFirst, String... value) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			if (isFirst)
				result = jedis.lpush(key, value);
			else
				result = jedis.rpush(key, value);
			if (LOG.isDebugEnabled())
				LOG.debug("addList" + (isFirst ? "First:" : "Last:") + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean removeListWithoutFor(byte[] key, int start, int end) throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = jedis.ltrim(key, start, end);
			if (LOG.isDebugEnabled())
				LOG.debug("removeListWithoutFor:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean setListIndex(byte[] key, int index, byte[] value) throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = getResource().lset(key, index, value);
			if (LOG.isDebugEnabled())
				LOG.debug("setListIndex:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public long listLen(byte[] key) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			result = jedis.llen(key);
			if (LOG.isDebugEnabled())
				LOG.debug("incr:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public byte[] popListFirst(byte[] key) throws RedisExcecption {
		return popList(key, true);
	}

	public byte[] popListLast(byte[] key) throws RedisExcecption {
		return popList(key, false);
	}

	protected byte[] popList(byte[] key, boolean isFirst) throws RedisExcecption {
		Jedis jedis = getResource();
		byte[] result;
		try {
			if (isFirst)
				result = jedis.lpop(key);
			else
				result = jedis.rpop(key);
			if (LOG.isDebugEnabled())
				LOG.debug(isFirst ? "popListFirst:" : "popListLast:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	protected String popList(String key, boolean isFirst) throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			if (isFirst)
				result = jedis.lpop(key);
			else
				result = jedis.rpop(key);
			if (LOG.isDebugEnabled())
				LOG.debug(isFirst ? "popListFirst:" : "popListLast:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	/** ################## */

	public void delete(String key) throws RedisExcecption {
		try {
			this.delete(key.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public boolean exists(String key) throws RedisExcecption {
		try {
			return this.exists(key.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean expireAt(String key, long unixTime) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			result = jedis.expireAt(key, unixTime);
			if (LOG.isDebugEnabled())
				LOG.debug("expireAt:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean expire(String key, int seconds) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			result = jedis.expire(key, seconds);
			if (LOG.isDebugEnabled())
				LOG.debug("expire:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean set(String key, String value) throws RedisExcecption {
		try {
			return this.set(key.getBytes(encoding), value.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean incr(String key, Integer value) throws RedisExcecption {
		try {
			return this.incr(key.getBytes(encoding), value);
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean set(String key, String value, int expireSeconds) throws RedisExcecption {
		try {
			return this.set(key.getBytes(encoding), value.getBytes(encoding), expireSeconds);
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public String getAndSet(String key, String value) throws RedisExcecption {
		byte[] result;
		try {
			result = this.getAndSet(key.getBytes(encoding), value.getBytes(encoding));
			return null == result ? null : new String(result, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public String get(String key) throws RedisExcecption {
		byte[] result;
		try {
			result = this.get(key.getBytes(encoding));
			return null == result ? null : new String(result, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public List<String> get(String... key) throws RedisExcecption {
		Jedis jedis = getResource();
		List<String> result;
		try {
			result = jedis.mget(key);
			try {
				return null == result ? null : result;
			} finally {
				if (LOG.isDebugEnabled())
					LOG.debug("get:List<String>:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean setMap(String key, String mapKey, String value) throws RedisExcecption {
		try {
			return this.setMap(key.getBytes(encoding), mapKey.getBytes(encoding), value.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean setMap(String key, Map<String, String> map) throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = jedis.hmset(key, map);
			if (LOG.isDebugEnabled())
				LOG.debug("setMap:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public List<String> getMapList(String key, String... subKyes) throws RedisExcecption {
		Jedis jedis = getResource();
		List<String> result;
		try {
			result = jedis.hmget(key, subKyes);
			try {
				return null == result ? null : result;
			} finally {
				if (LOG.isDebugEnabled())
					LOG.debug("getMapList:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Map<String, String> getMap(String key) throws RedisExcecption {
		Jedis jedis = getResource();
		Map<String, String> result;
		try {
			result = jedis.hgetAll(key);
			try {
				return null == result ? null : result;
			} finally {
				if (LOG.isDebugEnabled())
					LOG.debug("getMap:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean mapKeyExists(String key, String mapKey) throws RedisExcecption {
		try {
			return this.mapKeyExists(key.getBytes(encoding), mapKey.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean incrByMap(String key, String mapKey, Integer value) throws RedisExcecption {
		try {
			return this.incrByMap(key.getBytes(encoding), mapKey.getBytes(encoding), value);
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public String getMapValue(String key, String mapKey) throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = jedis.hget(key, mapKey);
			if (LOG.isDebugEnabled())
				LOG.debug("getMapValue:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean mapRemove(String key, String... mapKey) throws RedisExcecption {
		Jedis jedis = getResource();
		Long result;
		try {
			result = jedis.hdel(key, mapKey);
			if (LOG.isDebugEnabled())
				LOG.debug("incr:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public List<String> getListByString(String key, int start, int end) throws RedisExcecption {
		Jedis jedis = getResource();
		List<String> result;
		try {
			result = jedis.lrange(key, start, end);
			if (LOG.isDebugEnabled())
				LOG.debug("incr:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean addListFirst(String key, String... value) throws RedisExcecption {
		return addList(key, true, value);
	}

	public boolean addListLast(String key, String... value) throws RedisExcecption {
		return addList(key, false, value);
	}

	public boolean removeListWithoutFor(String key, int start, int end) throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = jedis.ltrim(key, start, end);
			if (LOG.isDebugEnabled())
				LOG.debug("incr:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean setListIndex(String key, int index, String value) throws RedisExcecption {
		try {
			return this.setListIndex(key.getBytes(encoding), index, value.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public long listLen(String key) throws RedisExcecption {
		try {
			return this.listLen(key.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RedisExcecption(e);
		}
	}

	public String getListIndex(String key, int index) throws RedisExcecption {
		Jedis jedis = getResource();
		String result;
		try {
			result = jedis.lindex(key, index);
			if (LOG.isDebugEnabled())
				LOG.debug("incr:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public byte[] getListIndex(byte[] key, int index) throws RedisExcecption {
		Jedis jedis = getResource();
		byte[] result;
		try {
			result = jedis.lindex(key, index);
			if (LOG.isDebugEnabled())
				LOG.debug("incr:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public String popListFirst(String key) throws RedisExcecption {
		return popList(key, true);
	}

	public String popListLast(String key) throws RedisExcecption {
		return popList(key, false);
	}

	public long removeListValue(byte[] key, byte[] value, int count) throws RedisExcecption {
		Jedis jedis = getResource();
		long result = -1l;
		try {
			result = jedis.lrem(key, count, value);
			if (LOG.isDebugEnabled())
				LOG.debug("removeListValue:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public long removeListValue(String key, String value, int count) throws RedisExcecption {
		Jedis jedis = getResource();
		long result = -1l;
		try {
			result = jedis.lrem(key, count, value);
			if (LOG.isDebugEnabled())
				LOG.debug("removeListString:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public long removeListObject(String key, Object value, int count) throws RedisExcecption {
		try {
			return this.removeListValue(key.getBytes(), getCacheSerialize().encode(value), count);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public long removeListOO(Object key, Object value, int count) throws RedisExcecption {
		try {
			return this.removeListValue(getCacheSerialize().encode(key), getCacheSerialize().encode(value), count);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	/*********************/

	public boolean setObject(String key, Object value) throws RedisExcecption {
		try {
			return this.set(key.getBytes(getEncoding()), getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean setObject(String key, Object value, int expireSeconds) throws RedisExcecption {
		try {
			return this.set(key.getBytes(getEncoding()), getCacheSerialize().encode(value), expireSeconds);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R getAndSetObject(String key, Object value) throws RedisExcecption {
		byte[] result;
		try {
			result = this.getAndSet(key.getBytes(getEncoding()), getCacheSerialize().encode(value));
			return null == result ? null : (R) getCacheSerialize().decode(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R getObject(String key) throws RedisExcecption {
		byte[] result;
		try {
			result = this.get(key.getBytes(getEncoding()));
			return null == result ? null : (R) getCacheSerialize().decode(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean setMapObject(String key, Object mapKey, Object value) throws RedisExcecption {
		try {
			return this.setMap(key.getBytes(getEncoding()), getCacheSerialize().encode(mapKey), getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public Map<byte[], byte[]> getMapString(String key) throws RedisExcecption {
		Jedis jedis = getResource();
		Map<byte[], byte[]> result;
		try {
			result = jedis.hgetAll(key.getBytes(getEncoding()));
			if (LOG.isDebugEnabled())
				LOG.debug("getMapObject-size:" + result.size());
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean mapKeyExistsObject(String key, Object mapKey) throws RedisExcecption {
		try {
			return this.mapKeyExists(key.getBytes(getEncoding()), getCacheSerialize().encode(mapKey));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean addListFirstObject(String key, Object value) throws RedisExcecption {
		try {
			return this.addListFirst(key.getBytes(getEncoding()), getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean addListLastObject(String key, Object value) throws RedisExcecption {
		try {
			return this.addListLast(key.getBytes(getEncoding()), getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R getListIndexObject(String key, int index) throws RedisExcecption {
		byte[] result;
		try {
			result = this.getListIndex(key.getBytes(getEncoding()), index);
			return null == result ? null : (R) getCacheSerialize().decode(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean setListIndexObject(String key, int index, Object value) throws RedisExcecption {
		try {
			return this.setListIndex(key.getBytes(getEncoding()), index, getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public long listLenObject(String key) throws RedisExcecption {
		try {
			return this.listLen(key.getBytes(getEncoding()));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R popListFirstObject(String key) throws RedisExcecption {
		byte[] result;
		try {
			result = this.popListFirst(key.getBytes(getEncoding()));
			return null == result ? null : (R) getCacheSerialize().decode(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R popListLastObject(String key) throws RedisExcecption {
		byte[] result;
		try {
			result = this.popListLast(key.getBytes(getEncoding()));
			return null == result ? null : (R) getCacheSerialize().decode(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}

	}

	/****/
	public void deleteOO(Object key) throws RedisExcecption {
		try {
			delete(getCacheSerialize().encode(key));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean setOO(Object key, Object value) throws RedisExcecption {
		try {
			return this.set(getCacheSerialize().encode(key), getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean setOO(Object key, Object value, int expireSeconds) throws RedisExcecption {
		try {
			return this.set(getCacheSerialize().encode(key), getCacheSerialize().encode(value), expireSeconds);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R getAndSetOO(Object key, Object value) throws RedisExcecption {
		try {
			return (R) this.getAndSet(getCacheSerialize().encode(key), getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R getOO(Object key) throws RedisExcecption {
		try {
			return (R) this.get(getCacheSerialize().encode(key));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean setMapOO(Object key, Object mapKey, Object value) throws RedisExcecption {
		try {
			return this.setMap(getCacheSerialize().encode(key), getCacheSerialize().encode(mapKey), getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R getMapObjectValue(Object key, Object mapKey) throws RedisExcecption {
		try {
			byte[] result = this.getMapValue(getCacheSerialize().encode(key), getCacheSerialize().encode(mapKey));
			if (null != result)
				return getCacheSerialize().decodeT(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
		return null;
	}

	public boolean mapKeyExistsOO(Object key, Object mapKey) throws RedisExcecption {
		try {
			return this.mapKeyExists(getCacheSerialize().encode(key), getCacheSerialize().encode(mapKey));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public Map<byte[], byte[]> getMapOO(Object key) throws RedisExcecption {
		try {
			return this.getMap(getCacheSerialize().encode(key));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean addListFirstOO(Object key, Object value) throws RedisExcecption {
		try {
			// return this.addListFirst(getCacheSerialize().encode(key), getCacheSerialize().encode(value));
			return this.addList(getCacheSerialize().encode(key), true, getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean addListLastOO(Object key, Object value) throws RedisExcecption {
		try {
			return this.addList(getCacheSerialize().encode(key), false, getCacheSerialize().encode(value));
			// return this.addListLast(getCacheSerialize().encode(key), getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R getListIndexOO(Object key, int index) throws RedisExcecption {
		try {
			return (R) this.getListIndex(getCacheSerialize().encode(key), index);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean setListIndexOO(Object key, int index, Object value) throws RedisExcecption {
		try {
			return this.setListIndex(getCacheSerialize().encode(key), index, getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public long listLenOO(Object key) throws RedisExcecption {
		try {
			return this.listLen(getCacheSerialize().encode(key));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R popListFirstOO(Object key) throws RedisExcecption {
		try {
			return (R) this.popListFirst(getCacheSerialize().encode(key));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public <R> R popListLastOO(Object key) throws RedisExcecption {
		try {
			return (R) this.popListLast(getCacheSerialize().encode(key));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public Object eval(String script, int keysCount, String... params) throws RedisExcecption {
		Jedis jedis = getResource();
		Object result;
		try {
			result = jedis.eval(script, keysCount, params);
			if (LOG.isDebugEnabled())
				LOG.debug("eval:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object eval(String script, List<String> keys, List<String> params) throws RedisExcecption {
		Jedis jedis = getResource();
		Object result;
		try {
			result = jedis.eval(script, keys, params);
			if (LOG.isDebugEnabled())
				LOG.debug("eval:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object eval(byte[] script, int keysCount, byte[]... params) throws RedisExcecption {
		Jedis jedis = getResource();
		Object result;
		try {
			result = jedis.eval(script, keysCount, params);
			if (LOG.isDebugEnabled())
				LOG.debug("eval:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object eval(byte[] script, List<byte[]> keys, List<byte[]> params) throws RedisExcecption {
		Jedis jedis = getResource();
		Object result;
		try {
			result = jedis.eval(script, keys, params);
			if (LOG.isDebugEnabled())
				LOG.debug("eval:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public String getEncoding() {
		return encoding;
	}

	public JedisStandAloneFactory setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public JedisStandAloneFactory setTimeOut(int timeOut) {
		this.timeOut = timeOut;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public JedisStandAloneFactory setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getHost() {
		return host;
	}

	public JedisStandAloneFactory setHost(String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public JedisStandAloneFactory setPort(int port) {
		this.port = port;
		return this;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public JedisStandAloneFactory setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
		return this;
	}

	public int getExpireSecond() {
		return expireSecond;
	}

	public JedisStandAloneFactory setExpireSecond(int expireSecond) {
		this.expireSecond = expireSecond;
		return this;
	}
}
