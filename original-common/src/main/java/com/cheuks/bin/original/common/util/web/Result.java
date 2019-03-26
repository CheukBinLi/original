package com.cheuks.bin.original.common.util.web;

import java.io.Serializable;

/***
 * *
 * 
 * CREATE ON 2018年06月04 下午4:10:02
 *
 * EMAIL:20796698@QQ.COM
 *
 * @author CHEUK.BIN.LI
 * 
 * @see 结束回调
 *
 */
public class Result<T extends Object> implements Serializable {

	private static final long serialVersionUID = -3379359587196074790L;

	private String code;
	private String msg;
	private T data;

	public Result<T> setCode(String code) {
		this.code = code;
		return this;
	}

	public Result<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public T getData() {
		return data;
	}

	public Result(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Result() {
		super();
	}

}
