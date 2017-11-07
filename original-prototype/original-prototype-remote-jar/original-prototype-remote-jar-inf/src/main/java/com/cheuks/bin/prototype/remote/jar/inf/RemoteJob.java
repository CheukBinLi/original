package com.cheuks.bin.prototype.remote.jar.inf;

public interface RemoteJob<T, V> {

	T process(V v) throws Throwable;

}
