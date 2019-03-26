package com.cheuks.bin.original.common.util.design.factory;

public interface Handler<T> {

	String getType();

	default void init() {
	}

	default boolean isSupport(T t) {
		return false;
	}

}
