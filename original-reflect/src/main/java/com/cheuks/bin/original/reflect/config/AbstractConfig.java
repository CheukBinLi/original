package com.cheuks.bin.original.reflect.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public abstract class AbstractConfig implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	protected final static Map<String, String> PROPERTIES = new HashMap<String, String>();

	// public abstract void destory();

	public abstract AbstractConfig makeConfig(Element element, ParserContext parserContext);

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	// private static final Pattern PATTERN_NAME = Pattern.compile("[\\-._0-9a-zA-Z]+");
	//
	// private static final Pattern PATTERN_MULTI_NAME = Pattern.compile("[,\\-._0-9a-zA-Z]+");
	//
	// private static final Pattern PATTERN_METHOD_NAME = Pattern.compile("[a-zA-Z][0-9a-zA-Z]*");
	//
	// private static final Pattern PATTERN_PATH = Pattern.compile("[/\\-$._0-9a-zA-Z]+");
	//
	// private static final Pattern PATTERN_NAME_HAS_SYMBOL = Pattern.compile("[:*,/\\-._0-9a-zA-Z]+");
	//
	// private static final Pattern PATTERN_KEY = Pattern.compile("[*,\\-._0-9a-zA-Z]+");

}
