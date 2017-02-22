package com.cheuks.bin.original.cache;

import com.cheuks.bin.original.common.cache.redis.RedisExcecption;
import com.cheuks.bin.original.common.cache.redis.RedisFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import redis.clients.jedis.Jedis;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public static void main(String[] args) throws RedisExcecption {
//		RedisFactory redisFactory = new JedisStandAloneCacheFactory();
		JedisStandAloneCacheFactory redisFactory = new JedisStandAloneCacheFactory();
		redisFactory.setHost("192.168.3.12");
		redisFactory.set("a1", "a1");
		redisFactory.set("a2", "a2");
		redisFactory.set("a3", "a3");
		redisFactory.set("a0", "a0");

		redisFactory.setMap("a", "a10", "10");
		redisFactory.setMap("a", "a9", "9");
		redisFactory.setMap("a", "a8", "8");
		redisFactory.setMap("a", "a7", "7");
		redisFactory.setMap("a", "a6", "6");
		redisFactory.setMap("a", "a5", "5");
		redisFactory.setMap("a", "a3", "3");
		redisFactory.setMap("a", "a4", "4");

		// Object o = redisFactory.eval("return redis.call('keys','a1*');", 0);
		Object o = redisFactory.eval("return redis.call('keys','a1*');", 0);
		System.out.println(o);
	}

}
