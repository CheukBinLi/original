package com.cheuks.bin.original.common.util;

/***
 * *
 * 
 * Copyright 2016 CHEUK.BIN.LI Individual All
 * 
 * ALL RIGHT RESERVED
 * 
 * CREATE ON 2016年5月3日
 * 
 * EMAIL:20796698@QQ.COM
 * 
 * 
 * @author CHEUK.BIN.LI
 * 
 * @see restful 消息结构
 *
 */
public class JsonMsgModel {

	/** 返回码: */
	private int code;
	/** 失败/自定义消息 */
	private String msg;
	/** 返回数据内容 */
	private Object data;
	/** 附加对象 */
	private Object attachment;

	public JsonMsgModel(int code, String msg, Object data, Object attachment) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.attachment = attachment;
	}

	public JsonMsgModel(int code, String msg, Object data) {
		this(code, msg, data, null);
	}

	public JsonMsgModel(Object data, int code) {
		this(code, null, data, null);
	}

	public JsonMsgModel(int code, String msg) {
		this(code, msg, null, null);
	}

	public JsonMsgModel(int code) {
		this(code, null, null, null);
	}

	public JsonMsgModel() {
		super();
	}

	public int getCode() {
		return code;
	}

	public JsonMsgModel setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public JsonMsgModel setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Object getData() {
		return data;
	}

	public JsonMsgModel setData(Object data) {
		this.data = data;
		return this;
	}

	public Object getAttachment() {
		return attachment;
	}

	public JsonMsgModel setAttachment(Object attachment) {
		this.attachment = attachment;
		return this;
	}

}
