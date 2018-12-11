package com.cheuks.bin.original.common.exception;

public class OverdueException extends LogicException {

	private static final long serialVersionUID = 4424991742241095543L;

	public OverdueException() {
		super();
	}

	public OverdueException(String code, String... msg) {
		super(code, msg);
	}

	public OverdueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public OverdueException(String message, Throwable cause) {
		super(message, cause);
	}

	public OverdueException(String message) {
		super(message);
	}

	public OverdueException(Throwable cause) {
		super(cause);
	}

}
