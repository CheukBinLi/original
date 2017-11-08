package com.cheuks.bin.original.common.util.scan;

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
 * scanPath="mapper.*query$xml*"
 * <p>
 * 完整路径*通配 $=.
 * </p>
 * <p>
 * 注意一定要以目录第一节点为起始
 * <p>
 * ----------例:org.spring.fw.util.cc.x.class
 * <p>
 * ----------org.*$class 或者 org.*cc.* 或者 org.*cc.x$class
 * </p>
 */
public class ScanSimple extends AbstractScan {

	private final Logger LOG = LoggerFactory.getLogger(ScanSimple.class);

	@Override
	protected Logger LOG() {
		return LOG;
	}

	public final Map<String, Set<String>> doScan(String path) throws IOException, InterruptedException, ExecutionException {
		if (LOG.isDebugEnabled())
			LOG.debug("scan start...");
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		if (null == path)
			return result;
		String[] originalPaths = path.split(",");
		path = path.replace(".", "/");
		path = path.replace(File.separator, "/").replace("$", ".");
		String[] paths = null;
		paths = path.split(",");
		String[] fullPaths = paths;
		// 后期换并发模式
		for (int i = 0, len = paths.length; i < len; i++) {
			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(paths[i].contains("*") ? paths[i].split("/")[0] : paths[i]);
			Set<URL> scanResult = new LinkedHashSet<URL>();
			while (urls.hasMoreElements()) {
				scanResult.add(urls.nextElement());
			}
			result.put(originalPaths[i], classMatchFilter(fullPaths[i], scanResult));
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("scan finish. \n" + result.toString());
		}
		return result;
	}

	protected final Set<String> classMatchFilter(final String path, Set<URL> paths) throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		final String pathPattern = "^(/.*/|.*/)?" + path.replace("*", "(.*)?").replace("(.*)?(.*)?", "(.*)?").replace("(.*)?/(.*)?", "(/.*|.*/)?").replace("/.*/.*", "/.*") + "(/.*)?$";
		// packageName
		// final int startIndex = (new File(Thread.currentThread().getContextClassLoader().getResource("").getPath())).getPath().replace(File.separator, "/").length() + 1;
		Set<URL> jarClassPaths = new HashSet<URL>();
		Set<URL> fileClassPaths = new HashSet<URL>();
		Set<String> result = new HashSet<String>();
		List<Future<Set<String>>> futures = new ArrayList<Future<Set<String>>>();
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
		// 过滤
		futures.add(executorService.submit(new ScanSimple.FileFilter(jarClassPaths, pathPattern, null, countDownLatch) {
			@Override
			public Set<String> doFilter(Set<URL> url, String pathPattern, String startIndex) throws IOException {
				return jarTypeFilter(pathPattern, url);
			}
		}));
		futures.add(executorService.submit(new ScanSimple.FileFilter(fileClassPaths, pathPattern, path.substring(0, path.contains("*") ? path.indexOf("*") : path.length()), countDownLatch) {
			@Override
			public Set<String> doFilter(Set<URL> url, String pathPattern, String startIndex) {
				Iterator<URL> it = url.iterator();
				Set<String> result = new HashSet<String>();
				while (it.hasNext())
					result.addAll(fileTypeFilter(new File(it.next().getPath()), pathPattern, startIndex));
				return result;
			}
		}));
		if (jarClassPaths.isEmpty())
			countDownLatch.countDown();
		if (fileClassPaths.isEmpty())
			countDownLatch.countDown();
		
		countDownLatch.await();

		result.addAll(futures.get(0).get());
		result.addAll(futures.get(1).get());

		try {
			return result;
		} finally {
			executorService.shutdown();
		}
	}

	@SuppressWarnings("resource")
	protected final Set<String> jarTypeFilter(String pathPattern, Set<URL> urls) throws IOException {
		Set<String> result = new HashSet<String>();
		Iterator<URL> it = urls.iterator();
		URL u;
		while (it.hasNext()) {
			u = it.next();
			JarFile jarFile = new JarFile(new File(u.getPath().substring(0, u.getPath().lastIndexOf("!")).replaceAll("file:", "")));
			Enumeration<JarEntry> jars = jarFile.entries();
			while (jars.hasMoreElements()) {
				JarEntry jarEntry = jars.nextElement();
				if (jarEntry.getName().matches(pathPattern)) {
					// result.add(jarEntry.getName().replace("/", "."));
					result.add(jarEntry.getName());
				}
			}
		}
		return result;
	}

	protected final Set<String> fileTypeFilter(File file, String pathPattern, String startIndex) {
		// Map<String, String> result = new WeakHashMap<String, String>();
		Set<String> result = new HashSet<String>();
		try {
			String filePath;
			if (file.isFile()) {
				if ((filePath = file.getPath().replace(File.separator, "/")).matches(pathPattern)) {
					// 文件添加返回
					result.add(filePath.substring(filePath.indexOf(startIndex)));
				}
				return result;
			} else if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					// 目录递归
					result.addAll(fileTypeFilter(f, pathPattern, startIndex));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	abstract class FileFilter implements Callable<Set<String>> {

		private String pathPattern;
		private Set<URL> urls;
		private String startIndex;
		private final CountDownLatch countDownLatch;

		public FileFilter(Set<URL> urls, String pathPattern, final CountDownLatch countDownLatch) {
			super();
			this.pathPattern = pathPattern;
			this.urls = urls;
			this.countDownLatch = countDownLatch;
		}

		public FileFilter(Set<URL> urls, String pathPattern, String startIndex, final CountDownLatch countDownLatch) {
			super();
			this.pathPattern = pathPattern;
			this.startIndex = startIndex;
			this.urls = urls;
			this.countDownLatch = countDownLatch;
		}

		public abstract Set<String> doFilter(Set<URL> url, String pathPattern, String startIndex) throws Exception;

		public Set<String> call() throws Exception {
			Set<String> result = doFilter(urls, pathPattern, startIndex);
			if (null != countDownLatch)
				countDownLatch.countDown();
			return result;
		}

	}

	public static void main(String[] args) throws Throwable {

		// Map<String, Set<String>> result = new ScanSimple().doScan("META-INF.maven.*xml,com.cheuks.*");
		AbstractScan scan = new ScanSimple();
		scan.setScanPath("META-INF.maven.*xml,com.cheuks.*,mapper.*query$xml*,org/apache/*/spi/**Root*$class,com.cheuks.*,org.springframework.orm.*");
		Set<String> result = scan.getResource("org/apache/*/spi/*/*Root*$class");
		Set<String> result2 = scan.getResource("com.cheuks.*");
		Set<String> result3 = scan.getResource("org.springframework.orm.*");
		// Set<String> result = scan.getResource("org.apache.*.spi.*Root*$class");
		// Set<String> result = scan.getResource("mapper.*query$xml*");
		Set<String> result6 = scan.getResource("META-INF.maven.*xml");
		// Map<String, Set<String>> result1 = scan.resource;
		System.out.println(result);
		System.out.println(result2);
		System.out.println(result3);
		System.out.println(result6);
		// System.out.println(result1.toString());
	}

}
