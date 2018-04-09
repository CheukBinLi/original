package com.cheuks.bin.original.prototype.classloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestClassLoader {

	public static void main(String[] args) throws Throwable {
		Thread.sleep(1000);
		MyClassLoader myClassLoader = new MyClassLoader(new URL[0], ClassLoader.class.getClassLoader());
		myClassLoader.addURL(new URL("file:D:/Repository/com/cheuks/bin/original-common/0.0.1-SNAPSHOT/original-common-0.0.1-SNAPSHOT.jar"));
		System.err.println(Arrays.toString(myClassLoader.getURLs()));
		Class valueComparator = myClassLoader.loadClass("com.cheuks.bin.original.common.util.FileUtil");
		System.err.println(valueComparator.getName());
		myClassLoader.release();
		valueComparator = null;
		myClassLoader = null;
		System.err.println("cleaning");
		Thread.sleep(5000);
		System.err.println("cleaning");
		System.gc();
		Thread.sleep(5000);
		System.err.println("cleaning");
		System.gc();
		Thread.sleep(5000);
		System.err.println("cleaning");
		myClassLoader = new MyClassLoader(new URL[0], ClassLoader.class.getClassLoader());
		myClassLoader.addURL(new URL("file:D:/Repository/com/cheuks/bin/original-common/0.0.1-SNAPSHOT/original-common-0.0.1-SNAPSHOT.jar"));
		valueComparator = myClassLoader.loadClass("com.cheuks.bin.original.common.util.FileUtil");
		System.err.println(valueComparator.getName());
		valueComparator = null;
		System.gc();
		Thread.sleep(5000);
		System.err.println("cleaning");
		CountDownLatch countDownLatch = new CountDownLatch(1);
		System.gc();
		// countDownLatch.await();
	}

}
