package com.cheuks.bin.original.db.manager;

import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.ConcurrentHashMap;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Resource;
import org.beetl.core.ResourceLoader;
import org.beetl.core.exception.BeetlException;

public class ConcurrentMapResourceLoader extends ConcurrentHashMap<String, String> implements ResourceLoader {

	private static final long serialVersionUID = 2661219122322367241L;
	protected boolean autoCheck;

	public boolean isModified(Resource key) {
		return !autoCheck;
	}

	public void init(GroupTemplate gt) {
	}

	public String getResourceId(Resource resource, String key) {
		return key;
	}

	public Resource getResource(final String key) {
		return new Resource(key, this) {
			public Reader openReader() {
				String val = get(key);
				if (val == null) {
					BeetlException ex = new BeetlException(BeetlException.TEMPLATE_LOAD_ERROR);
					ex.resourceId = key;
					throw ex;
				}
				return new StringReader(val);
			}

			public boolean isModified() {
				return !autoCheck;
			}
		};
	}

	public boolean exist(String key) {
		return containsKey(key);
	}

	public void close() {
	}

	public void setAutoCheck(boolean autoCheck) {
		this.autoCheck = autoCheck;
	}
}
