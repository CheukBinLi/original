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

public class RedisLuaSimple implements RedisLua {

	private static final Logger LOG = LoggerFactory.getLogger(RedisLua.class);

	private RedisFactory redisFactory;

	private final Map<String, String> sha = new ConcurrentHashMap<String, String>();

	public void initLoader(String... filePaths) throws IOException, RedisExcecption {
		if (null == redisFactory)
			redisFactory = new JedisStandAloneFactory();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = null;
		byte[] buffer = new byte[1024];
		int length;
		String fileName;
		byte[] sha;
//		String script;
		for (String str : filePaths) {
			out.reset();
			try {
				fileName = str.substring(str.lastIndexOf("/") + 1, str.lastIndexOf("."));
				in = Thread.currentThread().getContextClassLoader().getResourceAsStream(str);
				while (-1 != (length = in.read(buffer))) {
					out.write(buffer, 0, length);
				}
				// script = new String(out.toByteArray());
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
	}

	public String getSha(String name) {
		return sha.get(name);
	}

	public void clear() throws RedisExcecption {
		sha.clear();
		redisFactory.scriptFlush();
	}
}
