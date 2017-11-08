package com.cheuks.bin.original.db.manager.hibernate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.cheuks.bin.original.common.dbmanager.QueryFactory;
import com.cheuks.bin.original.common.util.scan.Scan;

public abstract class AbstractQueryFactory implements QueryType, QueryFactory {

	protected abstract Logger log();

	private String index;
	private Scan scan;

	public abstract void put(String name, String XQL, Object additional) throws Throwable;

	public abstract String get(String name, Map<String, Object> params, Object additional) throws Throwable;

	protected void scan() {
		try {
			Set<String> o = null;
			o = getScan().getResource(getIndex());
			if (null == o)
				return;
			xmlExplain(o);
		} catch (Throwable e) {
			log().error("fial by scan", e);
		}
	}

	public void xmlExplain(Set<String> urls) throws ParserConfigurationException, SAXException, IOException {
		Iterator<String> it = urls.iterator();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		XMLReader xmlReader = parser.getXMLReader();
		xmlReader.setEntityResolver(new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				return new InputSource(this.getClass().getClassLoader().getResourceAsStream("META-INF/dtd/Query.dtd"));
			}
		});
		while (it.hasNext()) {
			String str = it.next();
			InputSource is = new InputSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(str));
			is.setEncoding("utf-8");
			xmlReader.setContentHandler(new xmlHandler());
			xmlReader.parse(is);
		}
	}

	class xmlHandler extends DefaultHandler {
		// private boolean isHQL = false;
		private boolean format;
		private boolean alias;
		private String packageName;
		private String name;
		private String joinRef;
		private String joinTag;
		private boolean isJoin;
		LinkedList<String> currentTag = new LinkedList<String>();

		// private String fullName;
		// private List<String> aliasName = new ArrayList<String>();
		Map<String, String> aliases = new HashMap<String, String>();
		Map<String, String> joins = new HashMap<String, String>();
		private String value;

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (currentTag.size() > 0) {
				String tag = currentTag.removeLast();
				if (!tag.equals(qName))
					currentTag.addLast(tag);
			}
			super.endElement(uri, localName, qName);
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			currentTag.addLast(qName);
			if (qName.equals(QUERY_LIST)) {
				packageName = attributes.getValue(PACKAGE);
			} else if (qName.equals(QUERY)) {
				// isHQL = attributes.getValue(TYPE).equals("HQL");
				name = attributes.getValue(NAME);
				format = Boolean.valueOf(attributes.getValue(FREEMARK_FORMAT));
				alias = Boolean.valueOf(attributes.getValue(ALIAS));
				joinRef = attributes.getValue(JOIN_REF);
				joinTag = attributes.getValue(JOIN_TAG);
				isJoin = null != joinRef;
			} else if (qName.equals(ALIAS)) {
				aliases.put(attributes.getValue(ALIAS), attributes.getValue(NAME));
			} else if (qName.equals(JOIN)) {
				name = attributes.getValue(NAME);
			}
			super.startElement(uri, localName, qName, attributes);
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			value = new String(ch, start, length).replaceAll("(\n|\t)", "");
			if (value.length() > 0) {
				try {
					if (currentTag.getLast().equals(QUERY)) {
						value = isJoin ? join(value, joinRef, joinTag) : value;
						put(String.format("%s.%s", packageName, name).toLowerCase(), alias ? alias(value) : value, format);
					} else if (currentTag.getLast().equals(JOIN)) {
						value = value.replaceAll("\\<!\\[CDATA\\[", "").replaceAll("\\]\\]\\>", "");
						joins.put(name, value);
					}
				} catch (Throwable e) {
					log().error(null, e);
				}
			}
		}

		private String alias(String str) {
			if (alias)
				for (Entry<String, String> en : aliases.entrySet()) {
					str = str.replaceAll(en.getKey(), en.getValue());
				}
			return str;
		}

		private String join(String str, String joinRef, String joinTag) {
			str = str.replace(joinTag, joins.get(joinRef));
			return str;
		}
	}

	public String getIndex() {
		return index;
	}

	public QueryFactory setIndex(String index) {
		this.index = index;
		return this;
	}

	public Scan getScan() {
		return scan;
	}

	public QueryFactory setScan(Scan scan) {
		this.scan = scan;
		return this;
	}
}
