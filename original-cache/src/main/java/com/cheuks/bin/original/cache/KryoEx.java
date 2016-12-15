package com.cheuks.bin.original.cache;

import java.util.Arrays;

import com.esotericsoftware.kryo.ClassResolver;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ReferenceResolver;
import com.esotericsoftware.kryo.StreamFactory;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.util.DefaultClassResolver;
import com.esotericsoftware.kryo.util.DefaultStreamFactory;
import com.esotericsoftware.kryo.util.MapReferenceResolver;

public class KryoEx extends Kryo {

	public KryoEx() {
		this(new DefaultClassResolver(), new MapReferenceResolver(), new DefaultStreamFactory());
	}

	public KryoEx(ClassResolver classResolver, ReferenceResolver referenceResolver, StreamFactory streamFactory) {
		super(classResolver, referenceResolver, streamFactory);
		addDefaultSerializer(Arrays.asList("").getClass(), new JavaSerializer());
	}

	public KryoEx(ClassResolver classResolver, ReferenceResolver referenceResolver) {
		this(classResolver, referenceResolver, new DefaultStreamFactory());
	}

	public KryoEx(ReferenceResolver referenceResolver) {
		this(new DefaultClassResolver(), referenceResolver, new DefaultStreamFactory());
	}

}
