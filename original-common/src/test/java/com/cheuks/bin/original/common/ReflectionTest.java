package com.cheuks.bin.original.common;

import java.util.List;

public class ReflectionTest {

	// @Test
	// public void test() throws NoSuchFieldException, SecurityException {
	//
	// List<FieldList> list = Reflection.newInstance().getSettingField(A.class, true);
	// System.out.println(list);
	//
	// }

	class A {
		private int id;
		private String name;
		private List<B> bs;

		public int getId() {
			return id;
		}

		public A setId(int id) {
			this.id = id;
			return this;
		}

		public String getName() {
			return name;
		}

		public A setName(String name) {
			this.name = name;
			return this;
		}

		public List<B> getBs() {
			return bs;
		}

		public A setBs(List<B> bs) {
			this.bs = bs;
			return this;
		}

	}

	class B {
		private int id;
		private String sb;

		public int getId() {
			return id;
		}

		public B setId(int id) {
			this.id = id;
			return this;
		}

		public String getSb() {
			return sb;
		}

		public B setSb(String sb) {
			this.sb = sb;
			return this;
		}

	}

}
