package com.cheuks.bin.original.common.util.reflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.cheuks.bin.original.common.cache.CacheException;
import com.cheuks.bin.original.common.util.conver.StringUtil;

public class ClassLoaderUtil {

	// @SuppressWarnings("static-access")
	// public static ClassLoader createClassLoader(ClassLoader
	// parentClassLoader, URL... jars) {
	// URLClassLoader classLoader = new URLClassLoader(jars, null ==
	// parentClassLoader ?
	// ClassLoaderUtil.class.getClassLoader().getSystemClassLoader() :
	// parentClassLoader);
	// return classLoader;
	// }

	protected static ClassLoader createParentLoader() throws NoSuchMethodException, SecurityException, MalformedURLException {

		SimpleClassLoader classLoader = new SimpleClassLoader();
		String classPath = System.getProperty("java.class.path");
		if (StringUtil.isBlank(classPath)) {
			throw new NoSuchMethodException("cant't found \"java.class.path\" system property");
		}
		String separator = classPath.contains(";") ? ";" : ":";
		String[] classPaths = classPath.split(separator);
		for (String item : classPaths) {
			classLoader.addURL(new File(item));
		}
		return classLoader;
	}

	private static final Set<String> PROTOCOLS = new HashSet<>(Arrays.asList("http", "https", "file"));

	public static ClassLoader createClassLoaderByFile(ClassLoader parentClassLoader, String jarFilePatch) throws Exception {

		final Set<URL> jars = new HashSet<>();

		Files.walkFileTree(Paths.get(jarFilePatch), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				jars.add(file.toFile().toURL());
				return super.visitFile(file, attrs);
			}
		});
		return createClassLoader(parentClassLoader, jars.toArray(new URL[0]));
	}

	public static ClassLoader createClassLoader(ClassLoader parentClassLoader, URL... jars) throws Exception {
		if (null != jars)
			for (URL jar : jars) {
				if (!PROTOCOLS.contains(jar.getProtocol().toLowerCase())) {
					throw new Exception("cant't found jar protocol(http/https/file)");
				}
			}
		if (null == parentClassLoader) {
			parentClassLoader = createParentLoader();
		}
		return new URLClassLoader(jars, parentClassLoader);
	}

	@SuppressWarnings({"static-access"})
	public static ClassLoader createClassLoader(ClassLoader parentClassLoader, String... jars) throws Exception {
		List<URL> urls = new LinkedList<>();
		boolean support;
		if (null != jars)
			for (String jar : jars) {
				support = false;
				for (String protocol : PROTOCOLS) {
					if (jar.startsWith(protocol.toLowerCase())) {
						support = true;
						break;
					}
				}
				if (!support)
					throw new Exception("cant't found jar protocol(http/https/file)");
				urls.add(new URL(jar));
			}
		if (null == parentClassLoader) {
			parentClassLoader = ClassLoaderUtil.class.getClassLoader().getSystemClassLoader();
		}
		return new URLClassLoader(urls.toArray(new URL[0]), parentClassLoader);
	}

	public static void destroy(ClassLoader classLoader) {
		Thread thread = Thread.currentThread();
		synchronized (thread) {
			if (null == classLoader)
				return;
			ClassLoader parent = Thread.currentThread().getContextClassLoader();
			thread.setContextClassLoader(classLoader);

			ThreadGroup currentGroup = thread.getThreadGroup();
			while (currentGroup.getParent() != null) {
				currentGroup = currentGroup.getParent();
			}
			int count = currentGroup.activeCount();
			Thread[] threads = new Thread[count];
			currentGroup.enumerate(threads);
			for (Thread item : threads) {
				try {
					if (null == item.getContextClassLoader() || !item.getContextClassLoader().equals(classLoader))
						continue;
					item.interrupt();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			thread.setContextClassLoader(parent);
		}
	}

	@SuppressWarnings({"unused", "static-access", "unchecked", "rawtypes", "deprecation"})
	public static void main(String[] args) throws CacheException, Exception {
			System.out.printf(URLDecoder.decode("https%3A%2F%2Fai-test.bgyfw.com%3A55871%2Failogic"));
		ClassLoader x = new ClassLoader() {
		};
		System.err.println(System.getProperty("java.class.path"));

		File f1 = new File("D:/repository/maven/com/cheuks/bin/original-cache/0.0.1-SNAPSHOT/original-cache-0.0.1-SNAPSHOT.jar");
		File f2 = new File("D:/Desktop/remote-test.jar");
		System.err.println(f1.toURI().toURL().toString());
		// URLClassLoader cl = new URLClassLoader(new URL[]{f1.toURI().toURL()},
		// ClassLoaderUtil.class.getClassLoader().getSystemClassLoader());
		// Class<?> xxxx =
		// cl.loadClass("com.cheuks.bin.original.cache.DefaultCacheSerialize");
		// CacheSerialize cs = (CacheSerialize)
		// cl.loadClass("com.cheuks.bin.original.cache.DefaultCacheSerialize").newInstance();
		// String a = "abcde";
		// byte[] data = cs.encode(a);
		// System.err.println(cs.decodeT(data, String.class));

		ClassLoader cl = createClassLoader(null, f1.toURI().toURL(), f2.toURL());
		// Thread thread=Thread.currentThread();
		// thread.setContextClassLoader(cl);
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (;;) {
						Thread.sleep(1000);
						System.out.println("xxxxxxxxxxxx");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		// Thread.sleep(5000);
		// destroy(cl);
		// Thread.sleep(5000);
		// destroy(cl);
		System.err.println(ClassLoaderUtil.class.getClass().getClassLoader().getSystemClassLoader());
		Class test = cl.loadClass("a.a.a.threadxx");
		Object instace = test.newInstance();
		Method method = test.getDeclaredMethod("runxxx");
		method.invoke(instace);
		Thread.sleep(5000);
		destroy(cl);
		// Thread.sleep(5000);
		// Thread.sleep(5000);
		// destroy(cl);
		Thread.sleep(5000);
		CountDownLatch c = new CountDownLatch(1);
		c.await();
	}

}
