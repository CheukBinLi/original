package com.cheuks.bin.original.common.util.conver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Filter {

	static final Map<String, String> EMPTY_MAP = new HashMap<String, String>(1);
	static final Set<String> EMPTY_SET = new HashSet<String>(1);
	private Class<?> clazz;
	private boolean onlyIncude;
	private Set<String> excepts;//过滤
	private Map<String, String> includes;//包含 

	public final boolean exceptsIsEmpty() {
		return EMPTY_SET == this.excepts;
	}

	public final boolean includesIsEmpty() {
		return EMPTY_MAP == this.includes;
	}

	public static Filter build(Class<?> clazz) {
		return new Filter(clazz);
	}

	public Filter(Class<?> clazz) {
		this.clazz = clazz;
		this.excepts = EMPTY_SET;
		this.includes = EMPTY_MAP;
	}

	public synchronized Filter addInclude(String... include) {
		if (null == include || include.length < 1) {
			return this;
		}
		if (EMPTY_MAP == this.includes)
			this.includes = new HashMap<String, String>();
		int index;
		for (String item : include) {
			if ((index = item.indexOf(":")) > 0) {
				this.includes.put(item.substring(0, index), item.substring(index + 1, item.length()));
			} else {
				this.includes.put(item, null);
			}
		}
		return this;
	}

	public synchronized Filter addExcept(String... excepts) {
		if (null == excepts || excepts.length < 1) {
			return this;
		}
		if (EMPTY_SET == this.excepts)
			this.excepts = new HashSet<String>();
		for (String item : excepts) {
			this.excepts.add(item);
		}
		return this;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public Set<String> getExcepts() {
		return excepts;
	}

	public Map<String, String> getIncludes() {
		return includes;
	}

	public boolean isOnlyIncude() {
		return onlyIncude;
	}

	public Filter setOnlyIncude(boolean onlyIncude) {
		this.onlyIncude = onlyIncude;
		return this;
	}

}