package com.cheuks.bin.original.common.rmi;

public interface RmiClient extends RmiContent {

	public Object rmiInvoke(String methodName, Object... params) throws RmiException;

}
