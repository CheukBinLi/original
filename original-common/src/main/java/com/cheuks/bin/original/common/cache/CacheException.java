package com.cheuks.bin.original.common.cache;

public class CacheException extends Throwable {

	private static final long serialVersionUID = 1L;

	public CacheException() {
		super();
	}

	public CacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheException(String message) {
		super(message);
	}

	public CacheException(Throwable cause) {
		super(cause);
	}

}
