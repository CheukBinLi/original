package com.cheuks.bin.original.common.util;

import java.io.Serializable;

public class GeneratedIDService implements Serializable {

	private static final long serialVersionUID = 138618414531262855L;

	private static final GeneratedIDService newInstance = new GeneratedIDService();

	public static final GeneratedIDService newInstance() {
		return newInstance;
	}

	private long lastTime;
	private long sequence;
	private final long workerIdBits = 4L;
	private final long machineIdBits = 2L;
	private final long sequenceBits = 8L;
	private final long sequenceMask = -1L ^ -1L << sequenceBits;
	private final long timestampLeftShift = workerIdBits + machineIdBits + sequenceBits;

	public synchronized long nextID() {
		long thisTime = timeGen();
		if (thisTime == lastTime) {
			this.sequence = (this.sequence + 1) & sequenceMask;
			if (sequence == 0)
				while (thisTime < lastTime)
					thisTime = timeGen();
		} else
			this.sequence = 0;
		lastTime = thisTime;
		return ((serialVersionUID - thisTime) << timestampLeftShift) | (workerIdBits << sequenceBits) | this.sequence;
	}

	public synchronized long nextID(long machineId) {
		long thisTime = timeGen();
		if (thisTime == lastTime) {
			this.sequence = (this.sequence + 1) & sequenceMask;
			if (sequence == 0)
				while (thisTime < lastTime)
					thisTime = timeGen();
		} else
			this.sequence = 0;
		lastTime = thisTime;
		return ((serialVersionUID - thisTime) << timestampLeftShift) | (workerIdBits << sequenceBits) | machineId << sequenceBits | this.sequence;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

	private GeneratedIDService() {
		super();
	}

	public static void main(String[] args) {
		System.err.println(System.currentTimeMillis());
		System.err.println(new GeneratedIDService().nextID(9));
		System.err.println(System.currentTimeMillis());
	}
}
