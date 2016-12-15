package com.cheuks.bin.original.test.kryo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.cheuks.bin.original.cache.KryoEx;
import com.cheuks.bin.original.test.TestModel;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

public class T1 {

	@Test
	public static void a() {
		Kryo kryo = new KryoEx();

		CollectionSerializer list = new CollectionSerializer();
		// list.setElementClass(Object.class, kryo.getSerializer(Object.class));
		// list.setElementsCanBeNull(false);
		//
		// FieldSerializer fieldSerializer = new FieldSerializer(kryo,
		// TestModel.class);
		// fieldSerializer.getField("other").setClass(ArrayList.class, list);
		// kryo.register(TestModel.class, fieldSerializer);

		// list.setElementClass(TestModel.class, new JavaSerializer());
		// kryo.register(TestModel.class, new JavaSerializer());
		// kryo.register(ArrayList.class, new JavaSerializer());
		// kryo.register(ArrayList.class, list);

		kryo.register(Arrays.asList("").getClass(), new JavaSerializer());

		// kryo.register(ArrayList.class, list);
		// kryo.setDefaultSerializer(AnotherGen);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Output output = new Output(out);
		TestModel tm = new TestModel();
		Map<String, String> params = new HashMap<String, String>();
		params.put("1", "1");
		params.put("2", "2");
		params.put("3", "3");
		params.put("4", "4");
		params.put("5", "5");
		params.put("6", "6");
		params.put("7", "7");
		params.put("8", "8");
		tm.setId(711).setName("seven-eleven").setOk(true).setPay(11.921f)
				.setOther(Arrays.asList("小明", "小红", "小蓝", "小黄", "小紫")).setParam(params);
		kryo.writeObject(output, tm);
		output.flush();
		output.close();

		TestModel tm2 = kryo.readObject(new Input(out.toByteArray()), TestModel.class);
		System.out.println(tm2.getOther());
		System.out.println(tm2.getParam());
	}

	public static void main(String[] args) {
		a();
	}

}
