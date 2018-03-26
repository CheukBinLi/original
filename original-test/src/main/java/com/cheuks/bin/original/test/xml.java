package com.cheuks.bin.original.test;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.cheuks.bin.original.common.util.xml.XmlReaderAll;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class xml {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, ParserConfigurationException, SAXException, IOException {

		Document document;

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><A><a a=\"&lt;o67ndyTS\">xxx</a></A>";

		A a = XmlReaderAll.newInstance().padding(xml.getBytes(), A.class);
		System.out.println(a.getA());

	}

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class A {
		private String a;

	}

}
