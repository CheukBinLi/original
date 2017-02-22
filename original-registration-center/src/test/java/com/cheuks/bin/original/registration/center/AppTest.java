package com.cheuks.bin.original.registration.center;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import com.cheuks.bin.original.common.registrationcenter.RegistrationEventListener;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
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

	public static void main(String[] args) throws Throwable {
		ZookeeperRegistrationFactory zrf = new ZookeeperRegistrationFactory();
		zrf.setServerList("192.168.3.12:2181");
		zrf.init();
		String directory = zrf.createService("/service", new RegistrationEventListener<PathChildrenCacheEvent>() {

			public void nodeChanged(PathChildrenCacheEvent params, Object... obj) throws Exception {
				System.err.println("createService:" + new String(params.getData().getData()));
			}
		});
		final String a = "";
		synchronized (a) {
			a.wait();
		}
	}
}
