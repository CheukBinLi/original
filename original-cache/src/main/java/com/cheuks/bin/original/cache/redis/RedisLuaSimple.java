package com.cheuks.bin.original.cache.redis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.cache.redis.RedisExcecption;
import com.cheuks.bin.original.common.cache.redis.RedisFactory;
import com.cheuks.bin.original.common.cache.redis.RedisLua;
import com.cheuks.bin.original.common.util.scan.Scan;
import com.cheuks.bin.original.common.util.scan.ScanSimple;

public class RedisLuaSimple implements RedisLua {

	private static final Logger LOG = LoggerFactory.getLogger(RedisLua.class);

	private RedisFactory redisFactory;

	private Scan scan;

	private String scanPath = "lua.*$lua";

	private final Map<String, String> sha = new ConcurrentHashMap<String, String>();
	private final Map<String, String> scriptPath = new ConcurrentHashMap<String, String>();

	public void initLoader(String... filePaths) throws IOException, RedisExcecption {
		if (null == filePaths)
			initLoader();
		if (null == redisFactory)
			redisFactory = new JedisStandAloneFactory();
		String fileName;
		for (String str : filePaths) {
			fileName = str.substring(str.lastIndexOf("/") + 1, str.lastIndexOf("."));
			scriptPath.put(fileName, str);
			scriptLoad(fileName, str);
		}
	}

	void scriptLoad(String fileName, String path) throws IOException, RedisExcecption {
		if (null == redisFactory)
			redisFactory = new JedisStandAloneFactory();
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
			sha = redisFactory.scriptLoad(fileName.getBytes(), out.toByteArray());
			if (null != sha)
				this.sha.put(fileName, new String(sha));
			if (LOG.isDebugEnabled())
				LOG.debug(fileName + ":" + new String(sha));
		} finally {
			if (null != in)
				in.close();
		}
	}

	public void reset(boolean force, String... filePaths) throws IOException, RedisExcecption {
		if (force) {
			initLoader(filePaths);
		} else {
			for (Map.Entry<String, String> en : scriptPath.entrySet()) {
				scriptLoad(en.getKey(), en.getValue());
			}
		}
	}

	public void initLoader() throws IOException, RedisExcecption {
		if (null == redisFactory)
			redisFactory = new JedisStandAloneFactory();
		else if (null == scan) {
			scan = new ScanSimple();
		}
		try {
			initLoader(scan.doScan(scanPath).get(scanPath).toArray(new String[0]));
		} catch (Throwable e) {
			throw new RedisExcecption(e);
		}
	}

	public String getSha(String name) {
		return sha.get(name);
	}

	public void clear() throws RedisExcecption {
		sha.clear();
		redisFactory.scriptFlush();
	}

	public RedisFactory getRedisFactory() {
		return redisFactory;
	}

	public RedisLua setRedisFactory(RedisFactory redisFactory) {
		this.redisFactory = redisFactory;
		return this;
	}

	public Scan getScan() {
		return scan;
	}

	public RedisLua setScan(Scan scan) {
		return this;
	}

	public RedisLuaSimple() {
		super();
	}

	public RedisLuaSimple(RedisFactory redisFactory, Scan scan) {
		super();
		this.redisFactory = redisFactory;
		this.scan = scan;
	}

	public RedisLuaSimple(RedisFactory redisFactory, Scan scan, String scanPath) {
		super();
		this.redisFactory = redisFactory;
		this.scan = scan;
		this.scanPath = scanPath;
	}

}
