package com.cheuks.bin.original.anything.test.sql.parser.constant;

public enum DataBaseType {

		MYSQL("mysql"),
		ORACLE("oracle");

	private String name;

	public String getName() {
		return this.name;
	}

	private DataBaseType(String name) {
		this.name = name;
	}

}
