package com.cheuks.bin.original.configuration.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public interface DecodeAndEncode<T> {

	Object decode(final byte[] data) throws IOException;

	Object decodeByStream(final InputStream in) throws IOException;

	T decodeToObject(final byte[] data) throws IOException;

	byte[] encode(final Serializable obj) throws IOException;

	OutputStream encodeToStream(final Serializable obj) throws IOException;

}
