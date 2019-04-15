package com.cheuks.bin.original.cache.redis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.cache.redis.RedisExcecption;
import com.cheuks.bin.original.common.cache.redis.RedisFactory;
import com.cheuks.bin.original.common.util.scan.Scan;
import com.cheuks.bin.original.common.util.scan.ScanSimple;

import redis.clients.jedis.JedisCluster;

@SuppressWarnings({ "unchecked" })
public abstract class AbstractJedisCluster<T extends JedisCluster> implements RedisFactory {

	private int expireSecond = 1800;// 30分钟

	private String encoding = "UTF-8";

	public abstract T getResource();

	public abstract Logger getLog();

	public abstract void destory(T jedis);

	private CacheSerialize cacheSerialize;

	private Scan scan;
	
	private String scanPath = "lua.*$lua";

	private final Map<String, String> sha = new ConcurrentHashMap<String, String>();
	private final Map<String, String> scriptPath = new ConcurrentHashMap<String, String>();

	public CacheSerialize getCacheSerialize() {
		if (null == cacheSerialize) {
			synchronized (this) {
				if (null == cacheSerialize) {
					cacheSerialize = new FstCacheSerialize();
				}
			}
		}
		return cacheSerialize;
	}

	public void setCacheSerialize(CacheSerialize cacheSerialize) {
		this.cacheSerialize = cacheSerialize;
	}

	public byte[] scriptLoad(byte[] key, byte[] script) throws RedisExcecption {
		T jedis = getResource();
		byte[] result;
		try {
			result = jedis.scriptLoad(script, getSlotKey(new String(key)).getBytes());
			if (getLog().isDebugEnabled())
				getLog().debug("scriptLoad:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}
	
	public String getSlotKey(String key) {
		int s = key.indexOf("{");
		if (s > -1) {
			int e = key.indexOf("}", s + 1);
			if (e > -1 && e != s + 1) {
				key = key.substring(s + 1, e);
			}
		}
		return key;
	}

	public void delete(byte[] key) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			result = jedis.del(key);
			if (getLog().isDebugEnabled())
				getLog().debug("delete:" + result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean exists(byte[] key) throws RedisExcecption {
		T jedis = getResource();
		boolean result;
		try {
			result = jedis.exists(key);
			if (getLog().isDebugEnabled())
				getLog().debug("exists:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean expireAt(byte[] key, long unixTime) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			result = jedis.expireAt(key, unixTime);
			if (getLog().isDebugEnabled())
				getLog().debug("expireAt:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean expire(byte[] key, int seconds) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			result = jedis.expire(key, seconds);
			if (getLog().isDebugEnabled())
				getLog().debug("expire:" + result);
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
		T jedis = getResource();
		Long result;
		try {
			result = jedis.incrBy(key, value);
			if (getLog().isDebugEnabled())
				getLog().debug("incr:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean set(byte[] key, byte[] value, int expireSeconds) throws RedisExcecption {
		T jedis = getResource();
		String result;
		try {
			result = jedis.setex(key, expireSeconds, value);
			if (getLog().isDebugEnabled())
				getLog().debug("set:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public byte[] getAndSet(byte[] key, byte[] value) throws RedisExcecption {
		T jedis = getResource();
		byte[] result;
		try {
			result = jedis.getSet(key, value);
			jedis.expire(key, expireSecond);
			try {
				return null == result ? null : result;
			} finally {
				if (getLog().isDebugEnabled())
					getLog().debug("get:List<byte[]>:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public byte[] get(byte[] key) throws RedisExcecption {
		T jedis = getResource();
		byte[] result;
		try {
			result = jedis.get(key);
			try {
				return null == result ? null : result;
			} finally {
				if (getLog().isDebugEnabled())
					getLog().debug("get:byte[]:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public List<byte[]> getMapList(byte[] key, byte[]... subKyes) throws RedisExcecption {
		T jedis = getResource();
		List<byte[]> result;
		try {
			result = jedis.hmget(key, subKyes);
			try {
				return null == result ? null : result;
			} finally {
				if (getLog().isDebugEnabled())
					getLog().debug("getMapList:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public List<byte[]> get(byte[]... keys) throws RedisExcecption {
		T jedis = getResource();
		List<byte[]> result;
		try {
			result = jedis.mget(keys);
			try {
				return result;
			} finally {
				if (getLog().isDebugEnabled())
					getLog().debug("get:List<byte[]>:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean setMap(byte[] key, byte[] mapKey, byte[] value) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			result = jedis.hset(key, mapKey, value);
			if (getLog().isDebugEnabled())
				getLog().debug("setMap:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean setMap(byte[] key, Map<byte[], byte[]> map) throws RedisExcecption {
		T jedis = getResource();
		String result;
		try {
			result = jedis.hmset(key, map);
			if (getLog().isDebugEnabled())
				getLog().debug("setMap:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Map<byte[], byte[]> getMap(byte[] key) throws RedisExcecption {
		T jedis = getResource();
		Map<byte[], byte[]> result;
		try {
			result = jedis.hgetAll(key);
			if (getLog().isDebugEnabled())
				getLog().debug("getMap:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean mapKeyExists(byte[] key, byte[] mapKey) throws RedisExcecption {
		T jedis = getResource();
		boolean result;
		try {
			result = jedis.hexists(key, mapKey);
			if (getLog().isDebugEnabled())
				getLog().debug("mapKeyExists:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean incrByMap(byte[] key, byte[] mapKey, Integer value) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			result = jedis.hincrBy(key, mapKey, value);
			if (getLog().isDebugEnabled())
				getLog().debug("incrByMap:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public byte[] getMapValue(byte[] key, byte[] mapKey) throws RedisExcecption {
		T jedis = getResource();
		byte[] result;
		try {
			result = jedis.hget(key, mapKey);
			try {
				return null == result ? null : result;
			} finally {
				if (getLog().isDebugEnabled())
					getLog().debug("getMapValue:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean mapRemove(byte[] key, byte[]... mapKeys) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			result = jedis.hdel(key, mapKeys);
			if (getLog().isDebugEnabled())
				getLog().debug("mapRemove:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public List<byte[]> getListByBytes(byte[] key, int start, int end) throws RedisExcecption {
		T jedis = getResource();
		List<byte[]> result;
		try {
			result = jedis.lrange(key, start, end);
			if (getLog().isDebugEnabled())
				getLog().debug("mapRemove-list:size:" + result.size());
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

	public Object eval(byte[] script, int keysCount, byte[]... params) throws RedisExcecption {
		T jedis = getResource();
		Object result;
		try {
			result = jedis.eval(script, keysCount, params);
			if (getLog().isDebugEnabled())
				getLog().debug("eval:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object eval(byte[] script, List<byte[]> keys, List<byte[]> params) throws RedisExcecption {
		T jedis = getResource();
		Object result;
		try {
			result = jedis.eval(script, keys, params);
			if (getLog().isDebugEnabled())
				getLog().debug("eval:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	protected boolean addList(byte[] key, boolean isFirst, byte[]... value) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			if (isFirst)
				result = jedis.lpush(key, value);
			else
				result = jedis.rpush(key, value);
			if (getLog().isDebugEnabled())
				getLog().debug("addList" + (isFirst ? "First:" : "Last:") + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	protected boolean addList(String key, boolean isFirst, String... value) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			if (isFirst)
				result = jedis.lpush(key, value);
			else
				result = jedis.rpush(key, value);
			if (getLog().isDebugEnabled())
				getLog().debug("addList" + (isFirst ? "First:" : "Last:") + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean removeListWithoutFor(byte[] key, int start, int end) throws RedisExcecption {
		T jedis = getResource();
		String result;
		try {
			result = jedis.ltrim(key, start, end);
			if (getLog().isDebugEnabled())
				getLog().debug("removeListWithoutFor:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean setListIndex(byte[] key, int index, byte[] value) throws RedisExcecption {
		T jedis = getResource();
		String result;
		try {
			result = getResource().lset(key, index, value);
			if (getLog().isDebugEnabled())
				getLog().debug("setListIndex:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public long listLen(byte[] key) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			result = jedis.llen(key);
			if (getLog().isDebugEnabled())
				getLog().debug("incr:" + result);
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
		T jedis = getResource();
		byte[] result;
		try {
			if (isFirst)
				result = jedis.lpop(key);
			else
				result = jedis.rpop(key);
			if (getLog().isDebugEnabled())
				getLog().debug(isFirst ? "popListFirst:" : "popListLast:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object evalSha(byte[] script, List<byte[]> keys, List<byte[]> params) throws RedisExcecption {
		return getResource().evalsha(script, keys, params);
	}

	public Object evalSha(byte[] script, int keysCount, byte[]... params) throws RedisExcecption {
		return getResource().evalsha(script, keysCount, params);
	}

	protected String popList(String key, boolean isFirst) throws RedisExcecption {
		T jedis = getResource();
		String result;
		try {
			if (isFirst)
				result = jedis.lpop(key);
			else
				result = jedis.rpop(key);
			if (getLog().isDebugEnabled())
				getLog().debug(isFirst ? "popListFirst:" : "popListLast:" + result);
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

	public Object evalSha(String sha, String key) throws RedisExcecption {
		T jedis = getResource();
		Object result;
		try {
			result = jedis.evalsha(sha, key);
			if (getLog().isDebugEnabled())
				getLog().debug("evalSha:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object evalSha(String sha, int keys, String... params) throws RedisExcecption {
		T jedis = getResource();
		Object result;
		try {
			result = jedis.evalsha(sha, keys, params);
			if (getLog().isDebugEnabled())
				getLog().debug("evalSha:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object evalSha(String sha, List<String> keys, List<String> argv) throws RedisExcecption {
		T jedis = getResource();
		Object result;
		try {
			result = jedis.evalsha(sha, keys, argv);
			if (getLog().isDebugEnabled())
				getLog().debug("evalSha:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean scriptExists(String sha, String key) throws RedisExcecption {
		T jedis = getResource();
		boolean result;
		try {
			result = jedis.scriptExists(key, sha);
			if (getLog().isDebugEnabled())
				getLog().debug("scriptExists:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public void scriptFlush() throws RedisExcecption {
		T jedis = getResource();
		String result;
		try {
			result = jedis.scriptFlush(Long.toString(System.currentTimeMillis()).getBytes());
			if (getLog().isDebugEnabled())
				getLog().debug("scriptFlush:" + result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public void scriptKill() throws RedisExcecption {
		T jedis = getResource();
		String result;
		try {
			result = jedis.scriptKill(Long.toString(System.currentTimeMillis()).getBytes());
			if (getLog().isDebugEnabled())
				getLog().debug("scriptKill:" + result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public String scriptLoad(String key, String script) throws RedisExcecption {
		T jedis = getResource();
		String result;
		try {
			result = jedis.scriptLoad(script, key);
			if (getLog().isDebugEnabled())
				getLog().debug("scriptKill:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
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
		T jedis = getResource();
		Long result;
		try {
			result = jedis.expireAt(key, unixTime);
			if (getLog().isDebugEnabled())
				getLog().debug("expireAt:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean expire(String key, int seconds) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			result = jedis.expireAt(key, seconds);
			if (getLog().isDebugEnabled())
				getLog().debug("expire:" + result);
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
		T jedis = getResource();
		List<String> result;
		try {
			result = jedis.mget(key);
			try {
				return null == result ? null : result;
			} finally {
				if (getLog().isDebugEnabled())
					getLog().debug("get:List<String>:" + result);
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
		T jedis = getResource();
		String result;
		try {
			result = jedis.hmset(key, map);
			if (getLog().isDebugEnabled())
				getLog().debug("incr:" + result);
			return "OK".equalsIgnoreCase(result);
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
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

	public List<String> getMapList(String key, String... subKyes) throws RedisExcecption {
		T jedis = getResource();
		List<String> result;
		try {
			result = jedis.hmget(key, subKyes);
			try {
				return null == result ? null : result;
			} finally {
				if (getLog().isDebugEnabled())
					getLog().debug("getMapList:" + result);
			}
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Map<String, String> getMap(String key) throws RedisExcecption {
		T jedis = getResource();
		Map<String, String> result;
		try {
			result = jedis.hgetAll(key);
			if (getLog().isDebugEnabled())
				getLog().debug("getMapValue:" + result);
			return result;
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
		T jedis = getResource();
		String result;
		try {
			result = jedis.hget(key, mapKey);
			if (getLog().isDebugEnabled())
				getLog().debug("getMapValue:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public boolean mapRemove(String key, String... mapKey) throws RedisExcecption {
		T jedis = getResource();
		Long result;
		try {
			result = jedis.hdel(key, mapKey);
			if (getLog().isDebugEnabled())
				getLog().debug("incr:" + result);
			return result > 0;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public List<String> getListByString(String key, int start, int end) throws RedisExcecption {
		T jedis = getResource();
		List<String> result;
		try {
			result = jedis.lrange(key, start, end);
			if (getLog().isDebugEnabled())
				getLog().debug("incr:" + result);
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
		T jedis = getResource();
		String result;
		try {
			result = jedis.ltrim(key, start, end);
			if (getLog().isDebugEnabled())
				getLog().debug("incr:" + result);
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
		T jedis = getResource();
		String result;
		try {
			result = jedis.lindex(key, index);
			if (getLog().isDebugEnabled())
				getLog().debug("incr:" + result);
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

	public Object eval(String script, int keysCount, String... params) throws RedisExcecption {
		T jedis = getResource();
		Object result;
		try {
			result = jedis.eval(script, keysCount, params);
			if (getLog().isDebugEnabled())
				getLog().debug("eval:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Object eval(String script, List<String> keys, List<String> params) throws RedisExcecption {
		T jedis = getResource();
		Object result;
		try {
			result = jedis.eval(script, keys, params);
			if (getLog().isDebugEnabled())
				getLog().debug("eval:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	/*********************/

	public byte[] getListIndex(byte[] key, int index) throws RedisExcecption {
		T jedis = getResource();
		byte[] result;
		try {
			result = jedis.lindex(key, index);
			if (getLog().isDebugEnabled())
				getLog().debug("incr:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public Map<byte[], byte[]> getMapString(String key) throws RedisExcecption {
		T jedis = getResource();
		Map<byte[], byte[]> result;
		try {
			result = jedis.hgetAll(key.getBytes(getEncoding()));
			if (getLog().isDebugEnabled())
				getLog().debug("getMapObject-size:" + result.size());
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
		Object result;
		try {
			result = this.get(getCacheSerialize().encode(key));
			return null == result ? null : (R) this.get(getCacheSerialize().encode(key));
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
			return this.addListFirst(getCacheSerialize().encode(key), getCacheSerialize().encode(value));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public boolean addListLastOO(Object key, Object value) throws RedisExcecption {
		try {
			return this.addListLast(getCacheSerialize().encode(key), getCacheSerialize().encode(value));
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

	public long removeListValue(byte[] key, byte[] value, int count) throws RedisExcecption {
		T jedis = getResource();
		long result = -1l;
		try {
			result = jedis.lrem(key, count, value);
			if (getLog().isDebugEnabled())
				getLog().debug("removeListValue:" + result);
			return result;
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		} finally {
			destory(jedis);
		}
	}

	public long removeListValue(String key, String value, int count) throws RedisExcecption {
		T jedis = getResource();
		long result = -1l;
		try {
			result = jedis.lrem(key, count, value);
			if (getLog().isDebugEnabled())
				getLog().debug("removeListString:" + result);
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

	/***
	 * redisLua
	 */

	public void initScriptLoader(String... filePaths) throws IOException, RedisExcecption {
		if (null == filePaths)
			initScriptLoader();
		long result = -1l;
		if (getLog().isDebugEnabled())
			getLog().debug("removeListString:" + result);
		String fileName;
		for (String str : filePaths) {
			fileName = str.substring(str.lastIndexOf("/") + 1, str.lastIndexOf("."));
			scriptPath.put(fileName, str);
			scriptFileLoad(fileName, str);
		}

	}

	void scriptFileLoad(String fileName, String path) throws IOException, RedisExcecption {

		T jedis = getResource();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = null;
		byte[] buffer = new byte[1024];
		int length;
		byte[] sha;
		out.reset();
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			while (-1 != (length = in.read(buffer))) {
				out.write(buffer, 0, length);
			}
			sha = jedis.scriptLoad(fileName.getBytes(), out.toByteArray());
			if (null != sha)
				this.sha.put(fileName, new String(sha));
			if (getLog().isDebugEnabled())
				getLog().debug(fileName + ":" + new String(sha));
		} finally {
			destory(jedis);
			if (null != in)
				in.close();
		}

	}

	public void scriptReset(boolean force, String... filePaths) throws IOException, RedisExcecption {
		if (force) {
			initScriptLoader(filePaths);
		} else {
			for (Map.Entry<String, String> en : scriptPath.entrySet()) {
				scriptFileLoad(en.getKey(), en.getValue());
			}
		}
	}

	public void initScriptLoader() throws IOException, RedisExcecption {
		if (null == scan) {
			scan = new ScanSimple();
		}
		try {
			initScriptLoader(scan.doScan(scanPath).get(scanPath).toArray(new String[0]));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public String getScriptSha(String name) {
		return sha.get(name);
	}

	public void scriptClear() throws RedisExcecption {
		T jedis = getResource();
		try {
			sha.clear();
			jedis.scriptFlush(new byte[0]);
		} catch (Exception e) {
		} finally {
			destory(jedis);
		}
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public int getExpireSecond() {
		return expireSecond;
	}

	public void setExpireSecond(int expireSecond) {
		this.expireSecond = expireSecond;
	}

	public String getEncoding() {
		return encoding;
	}

}
