package com.cheuks.bin.original.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ObjectPoolManager<T, V> {

	private final Map<String, AbstractObjectPool<T, V>> POOL = new ConcurrentSkipListMap<String, AbstractObjectPool<T, V>>();

	private int tryAgain = 10;

	public AbstractObjectPool<T, V> getPool(String poolName) throws Throwable {
		return POOL.get(poolName);
	}

	/***
	 * 添加池对象
	 * 
	 * @param abstractObjectPool
	 * @return 返回旧有对像，不存在旧有对象返回null
	 * @throws Throwable
	 */
	public AbstractObjectPool<T, V> addPool(AbstractObjectPool<T, V> abstractObjectPool) throws Throwable {
		return POOL.put(abstractObjectPool.getPoolName(), abstractObjectPool);
	}

	public String[] getPoolNames() {
		return POOL.keySet().toArray(new String[0]);
	}

	public List<AbstractObjectPool<T, V>> getPoolValues() {
		return new ArrayList<AbstractObjectPool<T, V>>(POOL.values());
	}

	public AbstractObjectPool<T, V> remove(String poolName) {
		return POOL.remove(poolName);
	}

	public T borrowObject(String poolName) throws Throwable {
		int tryCount = tryAgain;
		while (true) {
			AbstractObjectPool<T, V> pool = POOL.get(poolName);
			if (null != pool) {
				return pool.borrowObject();
			} else if (tryCount-- < 0)
				throw new NullPointerException("can't found objectPool is [" + poolName + "],you can check it.");
			Thread.sleep(500);
		}
	}

	public void returnObject(String poolName, T t) throws Throwable {
		AbstractObjectPool<T, V> result = POOL.get(poolName);
		if (null == result) {
			throw new NullPointerException("can't found objectPool is [" + poolName + "],fail by return object,you can check it.");
		}
		result.returnObject(t);
	}

}
