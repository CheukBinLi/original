package com.cheuks.bin.original.db.manager;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.cheuks.bin.original.common.util.ScanFile;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class QueryFactory implements QueryType {

	private static final Logger LOG = LoggerFactory.getLogger(QueryFactory.class);

	private final Map<String, Template> FORMAT_XQL = new ConcurrentHashMap<String, Template>();
	private final Map<String, String> UNFORMAT_XQL = new ConcurrentHashMap<String, String>();
	private final Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_0);
	private StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

	private String files;
	private ScanFile scanFile;

	public QueryFactory() {
		super();
		freemarkerConfiguration.setTemplateLoader(stringTemplateLoader);
	}

	public synchronized void put(String name, String XQL, boolean isFormat) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		if (null == name || null == XQL)
			return;
		if (isFormat) {
			stringTemplateLoader.putTemplate(name, XQL);
			FORMAT_XQL.put(name, freemarkerConfiguration.getTemplate(name));
		} else {
			UNFORMAT_XQL.put(name, XQL);
		}

	}

	public String getXQL(String name, boolean isFormat, Map<String, Object> params) throws TemplateException, IOException {
		// if (!isScan)
		// scan();
		if (!isFormat)
			return UNFORMAT_XQL.get(name);
		Template tp = FORMAT_XQL.get(name);
		if (null == tp)
			return null;
		StringWriter sw = new StringWriter();
		tp.process(params, sw);
		if (LOG.isDebugEnabled())
			LOG.debug("name:%s: isFormat:%b XQL:%s", name, isFormat, sw.toString());
		return sw.toString();
	}

	@PostConstruct
	private void scan() {
		try {
			Set<String> o = null;
			o = scanFile.doScan(files).get(files);
			xmlExplain(o);
		} catch (Exception e) {
			LOG.error("扫描", e);
		}
	}

	public void xmlExplain(Set<String> urls) throws ParserConfigurationException, SAXException, IOException {
		Iterator<String> it = urls.iterator();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		xmlHandler handler = new xmlHandler();
		XMLReader xmlReader = parser.getXMLReader();
		xmlReader.setEntityResolver(new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				return new InputSource(this.getClass().getClassLoader().getResourceAsStream("dtd/Query.dtd"));
			}
		});
		while (it.hasNext()) {
			String str = it.next();
			InputSource is = new InputSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(str));
			is.setEncoding("utf-8");
			xmlReader.setContentHandler(handler);
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
				} catch (Exception e) {
					LOG.error(null, e);
				}
			}
		}

		private String alias(String str) {
			if (alias)
				for (Entry<String, String> en : aliases.entrySet())
					str = str.replaceAll(en.getKey(), en.getValue());
			return str;
		}

		private String join(String str, String joinRef, String joinTag) {
			str = str.replaceAll(joinTag, joins.get(joinRef));
			return str;
		}
	}

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public ScanFile getScanFile() {
		return scanFile;
	}

	public QueryFactory setScanFile(ScanFile scanFile) {
		this.scanFile = scanFile;
		return this;
	}

}
