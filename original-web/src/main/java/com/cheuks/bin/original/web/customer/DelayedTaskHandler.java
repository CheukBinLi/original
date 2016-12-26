package com.cheuks.bin.original.web.customer;

import java.util.concurrent.BlockingDeque;

public final class DelayedTaskHandler implements Runnable {
	private final boolean interrupt;
	private final BlockingDeque<Runnable> task;

	@Override
	public void run() {
		Runnable job;
		try {
			while (!interrupt) {
				if (null != (job = task.takeFirst())) {
					Thread.sleep(500);
					job.run();
				}
			}
		} catch (InterruptedException e) {
		}
	}

	public DelayedTaskHandler(final boolean interrupt, final BlockingDeque<Runnable> task) {
		this.interrupt = interrupt;
		this.task = task;
	}
}
