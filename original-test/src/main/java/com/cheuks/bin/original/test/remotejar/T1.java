package com.cheuks.bin.original.test.remotejar;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class T1 {

	public static void main(String[] args) throws Throwable {
		File f1 = new File("D:/Desktop/remote1.jar");
		File f2 = new File("D:/Desktop/remote2.jar");
		URL remoteClass1 = f1.toURI().toURL();
		URL remoteClass2 = f2.toURI().toURL();
		URLClassLoader remote1 = new URLClassLoader(new URL[]{remoteClass1}, ClassLoader.getSystemClassLoader());
		URLClassLoader remote2 = new URLClassLoader(new URL[]{remoteClass2}, ClassLoader.getSystemClassLoader());
//		RemoteJob<String, String> remoteJob1 = (RemoteJob<String, String>) remote1.loadClass("com.cheuks.bin.prototype.remote.jar.impl.RemoteJobImpl").newInstance();
//		RemoteJob<String, String> remoteJob2 = (RemoteJob<String, String>) remote2.loadClass("com.cheuks.bin.prototype.remote.jar.impl.RemoteJobImpl").newInstance();
//		// Class<?> c = ClassLoader.getSystemClassLoader().loadClass("com.cheuks.bin.prototype.remote.jar.impl.RemoteJobImpl");
//		System.err.println(remoteJob1.process("叼你"));
//		remote1.close();
//		System.err.println(remoteJob1.process("叼你"));
//		System.err.println(remoteJob2.process("叼你"));
		// System.err.println(null == c);
	}

}
