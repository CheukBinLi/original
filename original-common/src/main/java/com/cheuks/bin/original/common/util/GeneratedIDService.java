package com.cheuks.bin.original.common.util;

import java.io.Serializable;

public class GeneratedIDService implements Serializable {

	private static final long serialVersionUID = 138618414531262855L;

	private static final GeneratedIDService newInstance = new GeneratedIDService();

	public static final GeneratedIDService newInstance() {
		return newInstance;
	}

	private long lastTime;

	private int timeBits;
	private int machineBits = 13;
	private int sequenceBits = 10;
	private volatile int machineID = 1;
	private long sequence;
	private long maxSequence = -1L ^ (-1L << (timeBits - machineBits));//1024
	
	private static final long START_TME = 1566973459506L;

	/***
	 * 单机版
	 * @return
	 */
	public synchronized long nextID() {
		long currentTime = timeGen();
		if (currentTime == lastTime) {
			sequence = ++sequence & maxSequence;
			if (sequence == maxSequence) {
				currentTime = nextSecond(currentTime);
			}
		} else {
			sequence = 0L;
		}
		lastTime = currentTime;
		return (currentTime - START_TME) << timeBits | machineID << sequenceBits | sequence;
	}

	/***
	 * 分布式版本
	 * @param machineId
	 * @return
	 */
	public synchronized long nextID(long machineId) {
		long currentTime = timeGen();
		if (currentTime == lastTime) {
			sequence = ++sequence & maxSequence;
			if (sequence == maxSequence) {
				currentTime = nextSecond(currentTime);
			}
		} else {
			sequence = 0L;
		}
		lastTime = currentTime;
		return (currentTime - START_TME) << timeBits | machineId << sequenceBits | sequence;
	}

	long nextSecond(long currentTime) {
		long newTime;
		while ((newTime = timeGen()) == currentTime) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		return newTime;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}
	
	private GeneratedIDService() {
		super();
		this.timeBits = this.machineBits + this.sequenceBits;
	}

	public static void main(String[] args) {
		//		System.out.println(System.currentTimeMillis());
		//		System.out.println((System.currentTimeMillis() & 0x1FFFFFFF));
		//		System.out.println((99975801 & 0xfFFFFFFF));
		//		System.err.println(GeneratedIDService.newInstance.maxSequence);
		int i = 0;
		while (i < 10000) {
			System.err.println(GeneratedIDService.newInstance.nextID(1));
			System.out.println(GeneratedIDService.newInstance.nextID(99));
			i++;
		}
		//		System.err.println(GeneratedIDService.newInstance.nextID(9));
		//		System.err.println(GeneratedIDService.newInstance.nextID(9));
	}
}
