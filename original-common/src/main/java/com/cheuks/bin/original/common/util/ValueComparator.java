package com.cheuks.bin.original.common.util;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator<K, V extends Number> implements Comparator<K> {

	Map<K, V> base;

	// 这里需要将要比较的map集合传进来
	public ValueComparator(Map<K, V> base) {
		this.base = base;
	}

	public int compare(K a, K b) {
		if (base.get(a).longValue() >= base.get(b).longValue()) {
			return -1;
		} else {
			return 1;
		}
	}

}
