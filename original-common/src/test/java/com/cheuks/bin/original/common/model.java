package com.cheuks.bin.original.common;

import java.util.ArrayList;
import java.util.List;

import com.cheuks.bin.original.common.util.xml.ClassToXml;

public class model {

	private String a;
	private List<AppTest> b;
	private int c;

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public List<AppTest> getB() {
		return b;
	}

	public void setB(List<AppTest> b) {
		this.b = b;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public model(String a, List<AppTest> b, int c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public static void main(String[] args) throws Throwable {
		List<AppTest> a=new ArrayList<>();
		a.add(new AppTest("A"));
		a.add(new AppTest("B"));
		a.add(new AppTest("C"));
		model m=new model("a", a, 110);
		System.err.println(ClassToXml.newInstance().toXml(m));
	}
	
}
