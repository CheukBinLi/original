package com.cheuks.bin.original.common.util;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * xml数据填充
 * 
 * @author Ben-Book
 *
 */
@SuppressWarnings("unchecked")
public class XmlReaderAll extends DefaultHandler {

	private static final Map<Class<?>, Map<String, Field>> cache = new ConcurrentHashMap<Class<?>, Map<String, Field>>();

	protected static XmlReaderAll NewInstance() {
		return new XmlReaderAll();
	}

	public static <T> T paddingModel(byte[] bytes, Class<T> obj) throws NoSuchFieldException, SecurityException, ParserConfigurationException, SAXException, IOException, InstantiationException, IllegalAccessException {
		return new XmlReaderAll().padding(bytes, obj);
	}

	public synchronized <T> T padding(byte[] bytes, Class<T> obj) throws ParserConfigurationException, SAXException, IOException, NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException {
		link.clear();
		Object result = obj.newInstance();
		link.addFirst(new Node(null, null, result));
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		XmlReaderAll handler = this;
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		InputSource is = new InputSource(in);
		is.setEncoding("utf-8");
		parser.parse(is, handler);

		T t = (T) result;
		// 译放obj
		result = null;
		return t;
	}

	static class Node {
		private String tagName;
		private Field field;
		private Object obj;

		public String getTagName() {
			return tagName;
		}

		public Node setTagName(String tagName) {
			this.tagName = tagName;
			return this;
		}

		public Object getObj() {
			return obj;
		}

		public Node setObj(Object obj) {
			this.obj = obj;
			return this;
		}

		public Field getField() {
			return field;
		}

		public Node setField(Field field) {
			this.field = field;
			return this;
		}

		public Node(String tagName, Field field, Object obj) {
			super();
			this.tagName = tagName;
			this.field = field;
			this.obj = obj;
		}
	}

	LinkedList<Node> link = new LinkedList<XmlReaderAll.Node>();

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		Object X = link.getLast().getObj();
		Field f = null;
		Object o = null;
		try {
			try {
				// f = X.getClass().getDeclaredField(qName);
				f = getFieldByCache(X.getClass(), qName);
			} catch (Exception e) {
				// f = X.getClass().getSuperclass().getDeclaredField(qName);
				f = getFieldByCache(X.getClass().getSuperclass(), qName);
			}
			f.setAccessible(true);
			o = f.get(X);
			if (null == o)
				f.set(X, o = Class.forName(f.getType().getName()).newInstance());
		} catch (Exception e) {
			// e.printStackTrace();
		}
		if (null != f || null != o)
			link.addLast(new Node(qName, f, o));
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (!link.isEmpty() && null != link.getLast().getTagName() && link.getLast().getTagName().equals(qName))
			link.removeLast();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (link.size() == 1)
			return;
		Node node = link.removeLast();
		Object o = link.getLast().getObj();
		Field f = node.getField();
		try {
			f.setAccessible(true);
			f.set(o, new String(ch, start, length));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Field getFieldByCache(Class<?> c, String field) throws NoSuchFieldException, SecurityException {
		Map<String, Field> fields = cache.get(c);
		if (null == fields) {
			fields = ReflectionUtil.instance().scanClassField4Map(c, true, false);
		}
		return fields.get(field);
	}

}
