package com.cheuks.bin.original.configuration.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class DefaultDecodeAndEncode implements DecodeAndEncode<String> {

	public Object decode(byte[] data) {
		return new String(data);
	}

	public Object decodeByStream(InputStream in) {
		return null;
	}

	public String decodeToObject(byte[] data) {
		return new String(data);
	}

	public byte[] encode(Serializable obj) {
		return ((String) obj).getBytes();
	}

	public OutputStream encodeToStream(Serializable obj) throws IOException {
		final byte[] data = encode(obj);
		final OutputStream result = new ByteArrayOutputStream(data.length);
		result.write(data);
		return result;
	}

}
