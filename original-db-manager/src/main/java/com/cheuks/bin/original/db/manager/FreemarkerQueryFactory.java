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

import com.cheuks.bin.original.common.util.Scan;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class FreemarkerQueryFactory extends AbstractQueryFactory {

	private static final Logger LOG = LoggerFactory.getLogger(FreemarkerQueryFactory.class);

	private final Map<String, Template> FORMAT_XQL = new ConcurrentHashMap<String, Template>();
	private final Map<String, String> UNFORMAT_XQL = new ConcurrentHashMap<String, String>();
	private final Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_0);
	private StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

	public FreemarkerQueryFactory() {
		super();
		freemarkerConfiguration.setTemplateLoader(stringTemplateLoader);
	}

	public void put(String name, String XQL, Object additional) throws Throwable {
		if (LOG.isDebugEnabled())
			LOG.debug("name: " + name + " isFormat: " + additional + " XQL: " + XQL);
		if (null == name || null == XQL)
			return;
		if ((Boolean) additional) {
			stringTemplateLoader.putTemplate(name, XQL);
			FORMAT_XQL.put(name, freemarkerConfiguration.getTemplate(name));
		} else {
			UNFORMAT_XQL.put(name, XQL);
		}

	}

	public String get(String name, Map<String, Object> params, Object additional) throws Throwable {
		String result = null;
		if ((Boolean) additional) {
			Template tp = FORMAT_XQL.get(name);
			if (null == tp)
				return null;
			StringWriter sw = new StringWriter();
			tp.process(params, sw);
			result = sw.toString();
		} else {
			result = UNFORMAT_XQL.get(name);
		}
		if (LOG.isDebugEnabled())
			LOG.debug("name:%s: isFormat:%b XQL:%s", name, additional, result);
		return result;
	}

	@PostConstruct
	protected void scan() {
		super.scan();
	}

	@Override
	protected Logger log() {
		return LOG;
	}

}
