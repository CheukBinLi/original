package com.cheuks.bin.original.common.rmi;

public interface RmiInvokeClient extends RmiContant {

    public Object rmiInvoke(String applicationName, String methodName, Object... params) throws RmiException;

}
