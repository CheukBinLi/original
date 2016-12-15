package com.cheuks.bin.original.reflect.rmi.handle;

import java.util.Map;

import javassist.CtClass;
import javassist.CtMethod;

public class RmiClientSupprotMethod implements MethodHandle {

	public String decorationmethod(CtClass clazz, CtMethod method, Map<String, Object> configuration) {
		// invoke(String className, String version, String methodName, Object...
		// params)
		return String.format("rmiInvoke(\"%s\",$$)", method.getName());
	}

}
