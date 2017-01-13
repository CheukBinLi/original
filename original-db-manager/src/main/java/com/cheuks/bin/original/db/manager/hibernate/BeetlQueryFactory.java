package com.cheuks.bin.original.db.manager.hibernate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class BeetlQueryFactory extends AbstractQueryFactory {

	private static final Logger LOG = LoggerFactory.getLogger(BeetlQueryFactory.class);

	private final Map<String, String> UNFORMAT_XQL = new ConcurrentHashMap<String, String>();
	private final Configuration configuration;
	private final ConcurrentMapResourceLoader resourceLoader;
	private final GroupTemplate groupTemplate;

	public BeetlQueryFactory() throws IOException {
		super();
		new Configuration();
		configuration = Configuration.defaultConfiguration();
		resourceLoader = new ConcurrentMapResourceLoader();
		groupTemplate = new GroupTemplate(resourceLoader, configuration);
	}

	@Override
	protected Logger log() {
		return LOG;
	}

	@Override
	public void put(String name, String XQL, Object additional) throws Throwable {
		if (LOG.isDebugEnabled())
			LOG.debug("name: " + name + " isFormat: " + additional + " XQL: " + XQL);
		if (null == name || null == XQL)
			return;
		if ((Boolean) additional) {
			resourceLoader.put(name, XQL);
		} else {
			UNFORMAT_XQL.put(name, XQL);
		}
	}

	@Override
	public String get(String name, Map<String, Object> params, Object additional) throws Throwable {
		String result = null;
		if ((Boolean) additional) {
			Template tp = groupTemplate.getTemplate(name);
			tp.binding(params);
			result = tp.render();
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

}
