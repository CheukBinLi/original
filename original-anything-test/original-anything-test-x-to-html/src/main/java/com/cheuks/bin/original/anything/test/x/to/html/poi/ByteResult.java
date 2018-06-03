package com.cheuks.bin.original.anything.test.x.to.html.poi;

import java.io.OutputStream;
import java.io.Writer;

public class ByteResult {
	public static final String FEATURE = "http://javax.xml.transform.stream.StreamResult/feature";
	private String systemId;
	private OutputStream outputStream;
	private Writer writer;

	public ByteResult() {
	}

	public ByteResult(OutputStream arg0) {
		this.setOutputStream(arg0);
	}

	public ByteResult(Writer arg0) {
		this.setWriter(arg0);
	}

	public ByteResult(String arg0) {
		this.systemId = arg0;
	}

	public void setOutputStream(OutputStream arg0) {
		this.outputStream = arg0;
	}

	public OutputStream getOutputStream() {
		return this.outputStream;
	}

	public void setWriter(Writer arg0) {
		this.writer = arg0;
	}

	public Writer getWriter() {
		return this.writer;
	}

	public void setSystemId(String arg0) {
		this.systemId = arg0;
	}

	public String getSystemId() {
		return this.systemId;
	}
}
