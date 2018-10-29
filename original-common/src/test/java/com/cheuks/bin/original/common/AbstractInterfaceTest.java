package com.cheuks.bin.original.common;

public abstract class AbstractInterfaceTest implements InterfaceTest {

	public abstract void end();

	@Override
	public void doThing() {
		System.err.println("AbstractInterfaceTest");
	}

}
