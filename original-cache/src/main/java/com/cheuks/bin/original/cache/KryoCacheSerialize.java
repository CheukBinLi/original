package com.cheuks.bin.original.cache;

import java.io.ByteArrayOutputStream;

import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoCacheSerialize implements CacheSerialize {

	private static final Kryo kryo = new KryoEx();
	static {
		// kryo.setRegistrationRequired(false);
		// kryo.register(Exception.class, new JavaSerializer());
	}

	public byte[] encode(Object o) throws Throwable {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Output output = new Output(out);
		kryo.writeObject(output, o);
		output.flush();
		output.close();
		return out.toByteArray();
	}

	public Object decode(byte[] o) throws Throwable {
		return kryo.readClassAndObject(new Input(o));
	}

	public <T> T decodeT(byte[] o) throws Throwable {
		Object result = decode(o);
		return null == result ? null : (T) result;
	}

}
