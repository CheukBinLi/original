package com.cheuks.bin.original.common.util.net;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public class HttpResponseModel {

	private int responseCode;
	private Map<String, List<String>> headers;
	private ByteArrayOutputStream data;

	public HttpResponseModel(int responseCode, final Map<String, List<String>> headers, final ByteArrayOutputStream data) {
		super();
		this.responseCode = responseCode;
		this.headers = headers;
		this.data = data;
	}

	public HttpResponseModel() {
		super();
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public HttpResponseModel setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
		return this;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public HttpResponseModel setResponseCode(int responseCode) {
		this.responseCode = responseCode;
		return this;
	}

	public ByteArrayOutputStream getData() {
		return data;
	}

	public HttpResponseModel setData(final ByteArrayOutputStream data) {
		this.data = data;
		return this;
	}

}
