package com.cheuks.bin.original.test.xannotation;

public class AsyncDemo {

	@AsyncAnnotation
	public void async(String a) {
		System.err.println(a);
	}

}
