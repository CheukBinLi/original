package com.cheuks.bin.original.db;

import org.hibernate.dialect.MySQLDialect;

public class MySQLDialectUtf8 extends MySQLDialect {

	@Override
	public String getTableTypeString() {
		return "ENGINE=InnoDB CHARSET=utf8";
	}

}
