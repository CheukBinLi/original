package com.cheuks.bin.original.prototype.mesos.executor;

import org.apache.mesos.MesosExecutorDriver;

public class T1 {

	public static void main(String[] args) {
		MesosExecutorDriver driver = new MesosExecutorDriver(new SimpleExecutor());
		System.out.println("run");
		try {
			System.err.println(driver.run());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("finish");
	}

}
