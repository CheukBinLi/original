package com.cheuks.bin.original.common.util.scan;

import java.util.Map;
import java.util.Set;

public interface Scan {

	public Map<String, Set<String>> doScan(String path) throws Throwable;

	public Set<String> getResource(String path) throws Throwable;

}
