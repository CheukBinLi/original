package com.cheuks.bin.original.anything.test.jdbc.test.sharding.Impl;

import java.sql.Connection;
import java.sql.SQLException;

import com.cheuks.bin.original.anything.test.jdbc.test.sharding.AbstractDataSource;

public class ShardingDataSource extends AbstractDataSource {

	@Override
	public Connection getConnection() throws SQLException {
		return null;
	}

}
