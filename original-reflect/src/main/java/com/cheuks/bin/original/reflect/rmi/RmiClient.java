package com.cheuks.bin.original.reflect.rmi;

public interface RmiClient {

	public Object rmiInvoke(String methodName, Object... params) throws Throwable;

}
