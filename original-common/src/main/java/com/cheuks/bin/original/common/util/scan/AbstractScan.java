package com.cheuks.bin.original.common.util.scan;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractScan implements Scan {

	private final Logger LOG = LoggerFactory.getLogger(AbstractScan.class);

	private volatile boolean isInit = false;

	protected abstract Logger LOG();

	// protected SoftConcurrentHashMap<String, Set<String>> resource = new SoftConcurrentHashMap<String, Set<String>>();
	protected Map<String, Set<String>> resource = new ConcurrentHashMap<String, Set<String>>();

	private String scanPath;
	private volatile int tryCount = 3;

	private ReentrantLock lock = new ReentrantLock();

	public Set<String> getResource(String path) throws Throwable {
		if (LOG.isDebugEnabled()) {
			if (null == getScanPath())
				LOG.warn("scanPath is null.");
		}
		return getResource(path, tryCount);
	}

	private Set<String> getResource(String path, int count) throws Throwable {
		if (count < 0)
			return null;
		if (!isInit) {
			if (lock.tryLock()) {
				isInit = true;
				try {
					Map<String, Set<String>> scan = doScan(getScanPath());
					synchronized (resource) {
						// for (Entry<String, Set<String>> en : scan.entrySet())
						// resource.put(en.getKey(), en.getValue());
						resource.putAll(scan);
					}
					return scan.get(path);
				} finally {
					lock.unlock();
				}
			}
			Thread.sleep(100);
			return getResource(path, --count);
		}
		return resource.get(path);
	}

	public Map<String, Set<String>> getResource() {
		return resource;
	}

	public String getScanPath() {
		return scanPath;
	}

	public AbstractScan setScanPath(String scanPath) {
		this.scanPath = scanPath;
		return this;
	}

	public int getTryCount() {
		return tryCount;
	}

	public AbstractScan setTryCount(int tryCount) {
		this.tryCount = tryCount;
		return this;
	}
}
