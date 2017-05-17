package com.cheuks.bin.original.common.rmi;

public class RmiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RmiException() {
		super();
	}

	public RmiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RmiException(String message, Throwable cause) {
		super(message, cause);
	}

	public RmiException(String message) {
		super(message);
	}

	public RmiException(Throwable cause) {
		super(cause);
	}

}
