package com.cheuks.bin.original.oauth.util;

import com.cheuks.bin.original.common.util.conver.StringUtil;

/***
 * * * CREATE ON 2018年06月22日 上午10:50:20 EMAIL:20796698@QQ.COM
 *
 * @author CHEUK.BIN.LI
 * @see 逻辑错误抛出，日志不输出
 */
public class LogicException extends RuntimeException {

	private static final long serialVersionUID = 2498363949906134166L;

	private String[] msg;

	public LogicException() {
		super();
	}

	public LogicException(String code, String... msg) {
		super(code);
		this.msg = msg;
	}

	public LogicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LogicException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogicException(String message) {
		super(message);
	}

	public LogicException(Throwable cause) {
		super(cause);
	}

	public String[] getMsg() {
		return msg;
	}

	public LogicException setMsg(String[] msg) {
		this.msg = msg;
		return this;
	}

	public static void isNull(String value, String code, String... msg) throws LogicException {
		if (StringUtil.newInstance().isBlank(value)) {
			throw new LogicException(code, msg);
		}
	}

}
