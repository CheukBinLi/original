package com.cheuks.bin.original.common.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SoftConcurrentHashMap<K extends Object, V> {

	private final ReferenceQueue<V> queue = new ReferenceQueue<V>();

	private final ConcurrentHashMap<K, softObject<K, V>> table = new ConcurrentHashMap<K, softObject<K, V>>();

	private volatile boolean isExpunge = false;

	Map<K, softObject<K, V>> getTable() {
		if (!isExpunge) {
			isExpunge = true;
			expungeStaleEntries();
			isExpunge = false;
		}
		return table;
	}

	@SuppressWarnings("unchecked")
	void expungeStaleEntries() {
		for (Object x; (x = queue.poll()) != null;) {
			softObject<K, V> o = (softObject<K, V>) x;
			table.remove(o.getKey());
		}
	}

	static class softObject<K, V> extends SoftReference<V> {
		private final K key;

		public softObject(K key, V referent, ReferenceQueue<? super V> q) {
			super(referent, q);
			this.key = key;
		}

		public K getKey() {
			return key;
		}

	}

	public V get(Object key) {
		softObject<K, V> result = getTable().get(key);
		return null == result ? null : result.get();
	}

	public V put(K key, V value) {
		softObject<K, V> result = getTable().put(key, new softObject<K, V>(key, value, queue));
		return null == result ? null : result.get();
	}

	public Set<java.util.Map.Entry<K, softObject<K, V>>> entrySet() {
		expungeStaleEntries();
		return getTable().entrySet();
	}

	public int size() {
		return getTable().size();
	}

	public boolean isEmpty() {
		return getTable().isEmpty();
	}

	public boolean containsKey(Object key) {
		return getTable().containsKey(key);
	}

	public V remove(Object key) {
		softObject<K, V> result = getTable().remove(key);
		return null != result ? result.get() : null;
	}

	public Set<K> keySet() {
		return getTable().keySet();
	}

	public Collection<softObject<K, V>> values() {
		return getTable().values();
	}
}
