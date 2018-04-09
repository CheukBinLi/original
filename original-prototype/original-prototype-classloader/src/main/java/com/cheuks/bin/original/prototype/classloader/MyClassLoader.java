package com.cheuks.bin.original.prototype.classloader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import sun.misc.*;

public class MyClassLoader extends URLClassLoader {

	private volatile static Method addClassMethod;
	{
		if (null == addClassMethod)
			synchronized (MyClassLoader.class) {
				if (null == addClassMethod)
					try {
						System.err.println(URLClassLoader.class.getSuperclass().getSuperclass().getName());
						// addClassMethod = URLClassLoader.class.getDeclaredMethod("addClass",
						// Class.class);
						addClassMethod = URLClassLoader.class.getSuperclass().getSuperclass().getDeclaredMethod("addClass", Class.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
	}

	public void release() throws Throwable {
		Field ucpField = this.getClass().getSuperclass().getDeclaredField("ucp");
		ucpField.setAccessible(true);
		URLClassPath ucp = (URLClassPath) ucpField.get(this);
		Method getLoaderMethod = URLClassPath.class.getDeclaredMethod("getLoader", int.class);
		getLoaderMethod.setAccessible(true);
		Method closeMethod = null;
		Object loader;
		for (int i = 0, len = ucp.getURLs().length; i < len; i++) {
			loader = getLoaderMethod.invoke(ucp, i);
			if (null == closeMethod) {
				closeMethod = loader.getClass().getDeclaredMethod("close");
				closeMethod.setAccessible(true);
			}
			closeMethod.invoke(loader);
		}
		this.close();
	}

	public final void addClass(final Class<?> clazz) throws Exception {
		addClassMethod.invoke(this, clazz);
	}

	public MyClassLoader(URL[] urls) {
		super(urls);
	}

	public MyClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public MyClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	protected void addURL(URL url) {
		super.addURL(url);
	}

}
