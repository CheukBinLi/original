package com.cheuks.bin.original.common.util.conver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class CollectionUtil {

	private static final CollectionUtil newInstance = new CollectionUtil();

	public static final CollectionUtil newInstance() {
		return newInstance;
	}

	public <K, V> Map<K, V> removeNullValue(final Map<K, V> collection) {
		Iterator<Entry<K, V>> it = collection.entrySet().iterator();
		Entry<K, V> en;
		while (it.hasNext()) {
			en = it.next();
			if (null == en.getValue())
				it.remove();
		}
		return collection;
	}

	public Map<String, Object> toMap(Object... params) {
		if (null == params || 0 != (params.length % 2))
			return null;
		Map<String, Object> map = new WeakHashMap<String, Object>(params.length * 2);
		for (int i = 0, len = params.length; i < len; i++) {
			map.put((String) params[i++], params[i]);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public <K> Map<K, Object> toMap(boolean isWeak, Object... params) {
		if (null == params || 0 != (params.length % 2))
			return null;
		Map<K, Object> result = isWeak ? new WeakHashMap<K, Object>(params.length * 2) : new HashMap<K, Object>(params.length * 2);
		for (int i = 0, len = params.length; i < len; i++) {
			result.put((K) params[i++], params[i]);
		}
		return result;
	}

	public boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public boolean isEmpty(Collection<?> collection) {
		return null != collection && collection.size() > 0;
	}

	public boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public boolean isEmpty(Map<?, ?> map) {
		return null != map && map.size() > 0;
	}

	public boolean isNotEmpty(Object... o) {
		return !isEmpty(o);
	}

	public boolean isEmpty(Object... o) {
		return null != o && o.length > 0;
	}

	public static void main(String[] args) {

		Map<String, Object> a = newInstance.toMap(true, new Object[] { 1, "1", 2, "2" });
		Map<String, Object> b = newInstance.toMap("1", 1, "2", 2);
		System.out.println(a);
		System.out.println(b);
	}

}
