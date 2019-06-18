package com.cheuks.bin.original.common.util.reflection;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class SimpleClassLoader extends URLClassLoader {

	public SimpleClassLoader(java.net.URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public SimpleClassLoader(java.net.URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public SimpleClassLoader(java.net.URL[] urls) {
		super(urls);
	}
	
	public SimpleClassLoader() {
		super(new URL[]{});
	}

	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}

	@SuppressWarnings("deprecation")
	public void addURL(File... jars) throws MalformedURLException {
		if (null == jars)
			return;
		for (File item : jars) {
			super.addURL(item.toURL());
		}
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}

}
