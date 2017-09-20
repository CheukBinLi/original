package com.cheuks.bin.original.rmi.t;

import com.cheuks.bin.original.common.annotation.rmi.RmiConsumerAnnotation;

@RmiConsumerAnnotation
public interface test2I {

	int a(String a, int b, char c, long d, Boolean e);

	int a1(String a, int b, char c, long d);

	int a2(String a, int b, char c);

	int a3(String a, int b);

	StringBuilder a4(String a);

	int a5();

	TestModel getTestModel(TestModel test);

}