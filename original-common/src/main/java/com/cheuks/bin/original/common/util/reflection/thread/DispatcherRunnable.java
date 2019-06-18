package com.cheuks.bin.original.common.util.reflection.thread;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DispatcherRunnable<ITEM> implements Runnable {

	static Logger log = LoggerFactory.getLogger(DispatcherRunnable.class);

	private final BlockingDeque<ITEM> JOBS = new LinkedBlockingDeque<>();

	private volatile boolean interrupted = true;

	public abstract void run(ITEM job);

	private long interval = 5000;

	private volatile boolean isActivity = true;

	@Override
	public void run() {
		try {
			ITEM job;
			while (!interrupted) {
				job = JOBS.poll();
				if (null == job) {
					synchronized (JOBS) {
						isActivity = false;
						JOBS.wait(interval);
						isActivity = true;
					}
					continue;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public DispatcherRunnable() {
		super();
	}

	public DispatcherRunnable<ITEM> add(ITEM job) {
		synchronized (JOBS) {
			JOBS.add(job);
			if (!isActivity) {
				JOBS.notify();
			}
		}
		return this;
	}

}
