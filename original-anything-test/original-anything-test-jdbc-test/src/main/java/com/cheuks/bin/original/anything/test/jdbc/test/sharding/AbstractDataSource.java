package com.cheuks.bin.original.anything.test.jdbc.test.sharding;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.cheuks.bin.original.anything.test.jdbc.test.sharding.Impl.WrapperAdapter;

public abstract class AbstractDataSource extends WrapperAdapter implements DataSource {

	private PrintWriter printWriter = new PrintWriter(System.out);

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return this.printWriter;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.printWriter = out;
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return getConnection();
	}

}
