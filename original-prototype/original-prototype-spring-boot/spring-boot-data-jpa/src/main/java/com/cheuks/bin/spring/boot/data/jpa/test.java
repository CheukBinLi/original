package com.cheuks.bin.spring.boot.data.jpa;

import java.util.concurrent.locks.ReentrantLock;

public class test {

	private final Object lock = new Object();

	private void A() throws InterruptedException {
		synchronized (lock) {
			System.err.println("A run .");
			lock.wait();
			System.err.println("A wait .");
		}
	}

	private void B() {
		synchronized (lock) {
			System.out.println("B run .");
			lock.notify();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		final test t = new test();
		ReentrantLock l = new ReentrantLock();
		l.tryLock();
		Thread a = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					t.A();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		a.start();
		Thread b = new Thread(new Runnable() {

			@Override
			public void run() {
				t.B();
			}
		});
		Thread.sleep(5000);
		b.setDaemon(true);
		b.start();
	}

}
