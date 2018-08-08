package com.cheuks.bin.original.cache.redis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.cache.KryoCacheSerialize;
import com.cheuks.bin.original.common.cache.redis.RedisExcecption;
import com.cheuks.bin.original.common.cache.redis.RedisFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterFactory extends AbstractJedisCluster<JedisCluster> {

	private static transient final Logger LOG = LoggerFactory.getLogger(JedisClusterFactory.class);

	private int maxIdle = 50;// 空闲

	private int maxTotal = 300;

	private int maxWaitMillis = 5000;

	private int soTimeOut = 500;

	private int connectionTimeout = 30000;

	private int maxRedirections = 8;// 最大重定向
	// private String serverList;

	private String host = "192.168.1.200:2000,192.168.1.201:2001,192.168.1.202:2002,192.168.1.203:2003,192.168.1.204:2004,192.168.1.205:2005";

	private boolean testOnBorrow;// ping

	private JedisPoolConfig config;

	protected static JedisCluster jedisCluster;

	protected Set<HostAndPort> hosts;

	private static volatile boolean isInit;

	synchronized void init() {
		if (isInit)
			return;
		isInit = true;
		if (LOG.isInfoEnabled())
			LOG.info("ClusterJedisManager:init");
		hosts = new HashSet<HostAndPort>();
		config = new JedisPoolConfig();
		config.setMaxIdle(this.maxIdle);
		config.setMaxTotal(this.maxTotal);
		config.setMaxWaitMillis(this.maxWaitMillis);
		config.setTestOnBorrow(testOnBorrow);
		config.setMaxTotal(this.maxTotal);
		if (null == this.host) {
			LOG.warn("host is null.");
			return;
		}
		StringTokenizer ip = new StringTokenizer(host, ",");
		String[] split;
		while (ip.hasMoreTokens()) {
			split = ip.nextToken().split(":");
			if (null == split || split.length < 2)
				continue;
			hosts.add(new HostAndPort(split[0], Integer.valueOf(split[1])));
		}

		jedisCluster = new JedisCluster(hosts, getConnectionTimeout(), soTimeOut, maxRedirections, config);
		if (null == getCacheSerialize())
			// setCacheSerialize(new DefaultCacheSerialize());
			setCacheSerialize(new KryoCacheSerialize());
		if (LOG.isInfoEnabled())
			LOG.info("ClusterJedisManager:complete");
	}

	public JedisClusterFactory() {
		super();
	}

	@Override
	public JedisCluster getResource() {
		if (!isInit)
			synchronized (this) {
				init();
			}
		return jedisCluster;
	}

	@Override
	public void destory(JedisCluster jedis) {
	}

	@Override
	public Logger getLog() {
		return LOG;
	}

	public JedisClusterFactory setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
		return this;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public void setMaxRedirections(int maxRedirections) {
		this.maxRedirections = maxRedirections;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public JedisPoolConfig getConfig() {
		return config;
	}

	public void setConfig(JedisPoolConfig config) {
		this.config = config;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public int getMaxRedirections() {
		return maxRedirections;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public Set<HostAndPort> getHosts() {
		return hosts;
	}

	public int getSoTimeOut() {
		return soTimeOut;
	}

	public void setSoTimeOut(int soTimeOut) {
		this.soTimeOut = soTimeOut;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public JedisClusterFactory setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	public void setHost(String serverList) {
		this.host = serverList;
	}

	public void setPassword(String password) {
		LOG.warn("cluster not support password auth");
	}

	public void setPort(int port) {
		LOG.warn("cluster not support port auth,format port in host : 192.168.1.1:8080,192.168.1.2:8080");
	}

	public static void main(String[] args) throws RedisExcecption {
		RedisFactory rm = new JedisClusterFactory();
		// rm.set("AA".getBytes(), "xx".getBytes());
		// rm.setObject("GOOD", 1111);
		// rm.set("GOOD1", "1111");
		// Assert.assertNull("YES", null);
		// rm.incr("投票", 1);
		// rm.incr("投票", 1);
		// rm.incr("投票", 1);
		// System.err.println(rm.get("投票"));
		// rm.incrByMap("MM", "x1", 1);
		String sha = rm.scriptLoad("aa", "local a=KEYS[1]; return a;");
		System.out.println(rm.evalSha(sha, 1, "哈哈"));
		rm.scriptFlush();
		try {
			System.out.println(rm.evalSha(sha, 1, "哈哈"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		sha = rm.scriptLoad("aa", "local a=KEYS[1]; return a;");
		System.out.println(rm.evalSha(sha, 1, "哈哈1"));

		rm.setOO(123, Arrays.asList("1", "2", "3").toArray());
	}
}
