package com.cheuks.bin.prototype.remote.jar.impl;

import com.cheuks.bin.prototype.remote.jar.inf.RemoteJob;

public class RemoteJobImpl implements RemoteJob<String, String> {

	public String process(String v) throws Throwable {
		return "小喇叭";
	}

}
