package com.cheuks.bin.original.test.elasticsearch;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TestModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Random random = new Random();
	private static final AtomicInteger ID = new AtomicInteger(1);

	private int id;
	private String name;
	private String remake;
	private String[] types;
	private List<String> randStr;

	public TestModel(int id, String name, String remake, String[] types, List<String> randStr) {
		super();
		this.id = id;
		this.name = name;
		this.remake = remake;
		this.types = types;
		this.randStr = randStr;
	}

	public TestModel() {
		this(ID.getAndAdd(1), randChar(5), randChar(10), null, null);

	}

	private static final String randChar(int size) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append((char) random.nextInt(10000000));
		}
		return sb.toString();
	}

	public int getId() {
		return id;
	}

	public TestModel setId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public TestModel setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemake() {
		return remake;
	}

	public TestModel setRemake(String remake) {
		this.remake = remake;
		return this;
	}

	public String[] getTypes() {
		return types;
	}

	public TestModel setTypes(String[] types) {
		this.types = types;
		return this;
	}

	public List<String> getRandStr() {
		return randStr;
	}

	public TestModel setRandStr(List<String> randStr) {
		this.randStr = randStr;
		return this;
	}

	public static Random getRandom() {
		return random;
	}

}
