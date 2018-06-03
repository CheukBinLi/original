package com.cheuks.bin.original.common;

public class T {

	private Thread thread;

	private volatile boolean isClose = false;

	public T() {
		super();
		thread = new Thread(new Runnable() {

			public void run() {
				System.err.println("T is start.");
				try {
					while (!isClose) {
						System.err.println("T is running.");
						Thread.sleep(5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

}
