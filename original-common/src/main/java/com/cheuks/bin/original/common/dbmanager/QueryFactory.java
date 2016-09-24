package com.cheuks.bin.original.common.dbmanager;

import java.util.Map;

public interface QueryFactory {

	void put(String name, String XQL, Object additional) throws Throwable;

	String get(String name, Map<String, Object> params, Object additional) throws Throwable;

}