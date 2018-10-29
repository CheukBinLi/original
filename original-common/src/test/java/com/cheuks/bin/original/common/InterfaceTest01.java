package com.cheuks.bin.original.common;

import java.util.Set;

import com.cheuks.bin.original.common.util.reflection.ClassInfo;
import com.cheuks.bin.original.common.util.reflection.ReflectionUtil;

public class InterfaceTest01 extends AbstractInterfaceTest {

	@Override
	public void end() {
		System.err.println("end");
	}

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		Set<ClassInfo> classes = ReflectionUtil.instance().findImplementation(InterfaceTest01.class.getClassLoader(), InterfaceTest.class);
		classes.forEach(item -> {
			System.err.println(item.getClazz().getName() + " isAbstract:" + item.isAbstract());
		});
	}

}
