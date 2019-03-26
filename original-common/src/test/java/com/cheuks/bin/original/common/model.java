package com.cheuks.bin.original.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cheuks.bin.original.common.util.xml.ClassToXml;
import com.cheuks.bin.original.common.util.xml.XmlReaderAll;

public class model {

	private String aAppApplication;
	private List<AppTest> b;
	private int c;

	public String getaAppApplication() {
		return aAppApplication;
	}

	public void setaAppApplication(String aAppApplication) {
		this.aAppApplication = aAppApplication;
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
	
	

	public model() {
		super();
	}

	public model(String a, List<AppTest> b, int c) {
		super();
		this.aAppApplication = a;
		this.b = b;
		this.c = c;
	}

	public static void main(String[] args) throws Throwable {
		List<AppTest> a = new ArrayList<>();
		a.add(new AppTest("A"));
		a.add(new AppTest("B"));
		a.add(new AppTest("C"));
		model m = new model("a", a, 110);
		System.err.println(ClassToXml.newInstance().toXml(m, true));
		
		String xml="<xml>" + 
				"<a_app_application><![CDATA[a]]></a_app_application><b><test><![CDATA[TEST]]></test><test><![CDATA[TEST]]></test><test><![CDATA[TEST]]></test></b><c><![CDATA[110]]></c>" + 
				"" + 
				"</xml>";
		model m1=XmlReaderAll.paddingModel(xml.getBytes(), model.class);
		System.err.println(Arrays.toString(m1.getB().toArray()));
	}

}
