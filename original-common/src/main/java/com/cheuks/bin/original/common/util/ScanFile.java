package com.cheuks.bin.original.common.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * scanPath="*.query.xml" 文件目录迭代匹配
 */
public class ScanFile extends AbstractScan {

	private final Logger LOG = LoggerFactory.getLogger(ScanFile.class);

	@Override
	protected Logger LOG() {
		return LOG;
	}

	public final Map<String, Set<String>> doScan(String path) throws IOException, InterruptedException, ExecutionException {
		Map<String, Set<String>> result;
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("");
		Set<URL> scanResult = new LinkedHashSet<URL>();
		while (urls.hasMoreElements()) {
			scanResult.add(urls.nextElement());
		}
		result = classMatchFilter(path, scanResult);
		if (LOG.isDebugEnabled()) {
			LOG.debug(result.toString());
		}
		return result;
	}

	protected final Map<String, Set<String>> classMatchFilter(String path, Set<URL> paths) throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		try {
			String[] pathPattern = null;
			String[] tempPathPattern = path.split(",");
			Map<String, String> pathPatterns = new HashMap<String, String>();
			path = path.replace("*", ".*").replace("/**", "(/.*)?").replace(File.separator, "/");
			pathPattern = path.split(",");
			for (int i = 0, len = pathPattern.length; i < len; i++) {
				pathPatterns.put(tempPathPattern[i], String.format("^(/|.*/|.*)?%s$", pathPattern[i]));
			}

			final int startIndex = (new File(Thread.currentThread().getContextClassLoader().getResource("").getPath())).getPath().replace(File.separator, "/").length() + 1;
			Set<URL> jarClassPaths = new HashSet<URL>();
			Set<URL> fileClassPaths = new HashSet<URL>();
			Map<String, Set<String>> result = new HashMap<String, Set<String>>();
			List<Future<Map<String, Set<String>>>> futures = new ArrayList<Future<Map<String, Set<String>>>>();
			final CountDownLatch countDownLatch = new CountDownLatch(2);
			Iterator<URL> urls = paths.iterator();
			URL u;
			while (urls.hasNext()) {
				u = urls.next();
				if ("jar".equals(u.getProtocol()))
					jarClassPaths.add(u);
				else
					fileClassPaths.add(u);

			}
			futures.add(executorService.submit(new ScanFile.FileFilter(jarClassPaths, pathPatterns, 0, countDownLatch) {
				@Override
				public Map<String, Set<String>> doFilter(Set<URL> url, Map<String, String> pathPattern, int startIndex) throws IOException {
					return jarTypeFilter(pathPattern, url);
				}
			}));
			futures.add(executorService.submit(new ScanFile.FileFilter(fileClassPaths, pathPatterns, startIndex, countDownLatch) {
				@Override
				public Map<String, Set<String>> doFilter(Set<URL> url, Map<String, String> pathPattern, int startIndex) {
					Iterator<URL> it = url.iterator();
					Map<String, Set<String>> result = new HashMap<String, Set<String>>();
					while (it.hasNext())
						combination(result, fileTypeFilter(new File(it.next().getPath()), pathPattern, startIndex));
					return result;
				}
			}));
			countDownLatch.await();

			result.putAll(futures.get(0).get());
			// result.addAll(futures.get(1).get());
			combination(result, futures.get(1).get());
			return result;
		} finally {
			executorService.shutdown();
		}
	}

	@SuppressWarnings("resource")
	protected final Map<String, Set<String>> jarTypeFilter(Map<String, String> pathPattern, Set<URL> urls) throws IOException {
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		Map<String, Set<String>> tempResult = new HashMap<String, Set<String>>();
		Iterator<URL> it = urls.iterator();
		URL u;
		while (it.hasNext()) {
			u = it.next();
			JarFile jarFile = new JarFile(new File(u.getPath().substring(0, u.getPath().lastIndexOf("!")).replaceAll("file:", "")));
			Enumeration<JarEntry> jars = jarFile.entries();
			while (jars.hasMoreElements()) {
				JarEntry jarEntry = jars.nextElement();
				for (Entry<String, String> en : pathPattern.entrySet())
					if (jarEntry.getName().matches(en.getValue())) {
						combination(result, en.getKey(), jarEntry.getName());
					}
			}
		}
		return result;
	}

	protected final Map<String, Set<String>> fileTypeFilter(File file, Map<String, String> pathPattern, int startIndex) {
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		Set<String> values = new HashSet<String>();
		String tempPath = null;
		if (file.isFile()) {
			tempPath = file.getPath().replace(File.separator, "/");
			for (Entry<String, String> en : pathPattern.entrySet())
				if (tempPath.matches(en.getValue())) {
					combination(result, en.getKey(), file.getPath().substring(startIndex));
				}
			return result;
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				combination(result, fileTypeFilter(f, pathPattern, startIndex));
			}
		}
		return result;
	}

	// 结果组合
	protected Map<String, Set<String>> combination(Map<String, Set<String>> main, Map<String, Set<String>> vice) {
		Set<String> tempValues;
		for (Entry<String, Set<String>> en : vice.entrySet()) {
			tempValues = main.get(en.getKey());
			if (null == tempValues)
				main.put(en.getKey(), en.getValue());
			else
				tempValues.addAll(en.getValue());
		}
		return main;
	}

	// 结果组合
	protected Map<String, Set<String>> combination(Map<String, Set<String>> main, String key, String value) {
		Set<String> tempValues = main.get(key);
		if (null == tempValues) {
			Set<String> values = new HashSet<String>();
			values.add(value);
			main.put(key, values);
		} else
			main.get(key).add(value);
		return main;
	}

	abstract class FileFilter implements Callable<Map<String, Set<String>>> {
		private Map<String, String> pathPattern;
		private Set<URL> urls;
		private int startIndex;
		private final CountDownLatch countDownLatch;

		public FileFilter(Set<URL> urls, Map<String, String> pathPattern, final CountDownLatch countDownLatch) {
			super();
			this.pathPattern = pathPattern;
			this.urls = urls;
			this.countDownLatch = countDownLatch;
		}

		public FileFilter(Set<URL> urls, Map<String, String> pathPattern, int startIndex, final CountDownLatch countDownLatch) {
			super();
			this.pathPattern = pathPattern;
			this.startIndex = startIndex;
			this.urls = urls;
			this.countDownLatch = countDownLatch;
		}

		public abstract Map<String, Set<String>> doFilter(Set<URL> url, Map<String, String> pathPattern, int startIndex) throws Exception;

		public Map<String, Set<String>> call() throws Exception {
			Map<String, Set<String>> result = doFilter(urls, pathPattern, startIndex);
			if (null != countDownLatch)
				countDownLatch.countDown();
			return result;
		}

	}

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		Object o = new ScanFile().doScan("*.xml");
		System.out.println(o);
		System.out.println("X");
	}
}
