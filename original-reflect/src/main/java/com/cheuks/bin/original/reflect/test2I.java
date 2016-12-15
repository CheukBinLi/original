package com.cheuks.bin.original.reflect;

import com.cheuks.bin.original.annotation.RmiClient;

@RmiClient(serviceImplementation = "OH_SHIT")
public interface test2I {

	int a(String a, int b, char c, long d, Boolean e);

	int a1(String a, int b, char c, long d);

	int a2(String a, int b, char c);

	int a3(String a, int b);

	StringBuilder a4(String a);

	int a5();

}