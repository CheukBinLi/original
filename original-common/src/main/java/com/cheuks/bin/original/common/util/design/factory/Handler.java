package com.cheuks.bin.original.common.util.design.factory;

public interface Handler {

	String getType();

	default void init() {
	}

	default boolean isSupport(Class<?> c) {
		return false;
	}

}
