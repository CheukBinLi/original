package com.cheuks.bin.original.common.util;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 抽象池
 * 
 * @author ben
 *
 * @param <T>对象池对象类型
 * @param <V>辅助规定
 */
public abstract class AbstractObjectPool<T, V> implements ObjectPool<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractObjectPool.class);

	private String poolName;
	private final BlockingDeque<T> QUEUE;
	private final ConcurrentMap<T, Long> BORROW_QUEUE;
	private volatile boolean interrupt = false;
	private volatile long timeout = 600000;// 对象10分钟不归还，失效，销毁
	private volatile long invalidateDetectionInterval = 300000;// 5分钟检测一次

	/***
	 * 回调过期对象个数，重建对像
	 * 
	 * @param count
	 *            过期对象个数
	 * @throws Exception
	 */
	public abstract void invalidateReBuildObject(int count) throws Exception;

	private final Thread invalidateDetection = new Thread(new Runnable() {
		public void run() {
			Entry<T, Long> tempResource;
			long now;
			Iterator<Entry<T, Long>> it;
			int invalidateCount;
			try {
				while (!interrupt) {
					Thread.sleep(invalidateDetectionInterval);
					invalidateCount = 0;
					now = System.currentTimeMillis();
					it = BORROW_QUEUE.entrySet().iterator();
					while (it.hasNext()) {
						tempResource = it.next();
						if (now >= tempResource.getValue()) {
							try {
								invalidateObject(tempResource.getKey());
								invalidateCount++;
							} catch (Exception e) {
								LOG.error(null, e);
							} finally {
								BORROW_QUEUE.remove(tempResource.getKey());
							}
						}
					}
					invalidateReBuildObject(invalidateCount);
				}
			} catch (InterruptedException ie) {
				LOG.warn("invalidateDetection thread is interrupt.");
			} catch (Exception e) {
				LOG.error(null, e);
			}
		}
	});

	public T borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
		// T result;
		T result = QUEUE.takeFirst();
		// while (null == (result = QUEUE.pop())) {
		// Thread.sleep(5);
		// }
		BORROW_QUEUE.put(result, System.currentTimeMillis() + timeout);
		return result;
	}

	public void returnObject(T t) throws Exception {
		if (null != t) {
			BORROW_QUEUE.remove(t);
			QUEUE.addLast(t);
		}
	}

	public synchronized void removeObject(T t) throws Exception {
		if (null != t) {
			QUEUE.remove(t);
			BORROW_QUEUE.remove(t);
		}
	}

	public int getNumIdle() {
		return QUEUE.size();
	}

	public int getNumActive() {
		return BORROW_QUEUE.size();
	}

	public void reset() throws Exception {
		synchronized (this) {
			for (T t : QUEUE) {
				destroyObject(t);
			}
			for (Entry<T, Long> en : BORROW_QUEUE.entrySet()) {
				destroyObject(en.getKey());
			}
		}
	}

	public void addObject(T t) throws Exception, IllegalStateException, UnsupportedOperationException {
		QUEUE.addLast(t);
	}

	public AbstractObjectPool(int poolSize) {
		super();
		invalidateDetection.start();
		if (poolSize < 0) {
			QUEUE = new LinkedBlockingDeque<T>();
			BORROW_QUEUE = new ConcurrentHashMap<T, Long>();
		} else {
			QUEUE = new LinkedBlockingDeque<T>(poolSize + 10);
			BORROW_QUEUE = new ConcurrentHashMap<T, Long>(poolSize + 20);
		}
	}

	public void shutdown() {
		interrupt = true;
		invalidateDetection.interrupt();
	}

	public AbstractObjectPool() {
		this(-1);
		this.poolName = "default_" + System.currentTimeMillis();
	}

	public String getPoolName() {
		return poolName;
	}

	public AbstractObjectPool<T, V> setPoolName(String poolName) {
		this.poolName = poolName;
		return this;
	}

	public long getTimeout() {
		return timeout;
	}

	public AbstractObjectPool<T, V> setTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}

	public long getInvalidateDetectionInterval() {
		return invalidateDetectionInterval;
	}

	public AbstractObjectPool<T, V> setInvalidateDetectionInterval(long invalidateDetectionInterval) {
		this.invalidateDetectionInterval = invalidateDetectionInterval;
		return this;
	}

}
