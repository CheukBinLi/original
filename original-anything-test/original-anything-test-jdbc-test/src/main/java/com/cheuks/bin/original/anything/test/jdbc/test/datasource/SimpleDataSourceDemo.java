package com.cheuks.bin.original.anything.test.jdbc.test.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class SimpleDataSourceDemo implements DataSource {

	private PrintWriter printWriter = new PrintWriter(System.out);

	private List<DataSource> dataSources = new ArrayList<DataSource>();

	public PrintWriter getLogWriter() throws SQLException {
		return this.printWriter;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		this.printWriter = out;
	}

	public final int getLoginTimeout() throws SQLException {
		throw new SQLFeatureNotSupportedException("unsupported getLoginTimeout()");
	}

	public final void setLoginTimeout(final int seconds) throws SQLException {
		throw new SQLFeatureNotSupportedException("unsupported setLoginTimeout(int seconds)");
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

	@SuppressWarnings("unchecked")
	public final <T> T unwrap(final Class<T> iface) throws SQLException {
		if (isWrapperFor(iface)) {
			return (T) this;
		}
		throw new SQLException(String.format("[%s] cannot be unwrapped as [%s]", getClass().getName(), iface.getName()));
	}

	public final boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return iface.isInstance(this);
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return getConnection();
	}

	public Connection getConnection() throws SQLException {
		return null;
	}

}
