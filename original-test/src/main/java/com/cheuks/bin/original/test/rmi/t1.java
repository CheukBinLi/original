package com.cheuks.bin.original.test.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class t1 {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		Class helloImpl = Class.forName("com.cheuks.bin.original.test.rmi.HelloImpl");
		Object o = helloImpl.newInstance();
		Method sayHi = Hello.class.getDeclaredMethod("sayHi", String.class);
		System.out.println(sayHi.invoke(o, "小明同学"));
	}

}
