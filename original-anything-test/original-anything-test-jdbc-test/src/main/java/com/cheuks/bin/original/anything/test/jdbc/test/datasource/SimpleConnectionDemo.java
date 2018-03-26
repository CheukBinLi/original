package com.cheuks.bin.original.anything.test.jdbc.test.datasource;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executor;

public class SimpleConnectionDemo implements Connection {

	// private Collection<Connection> cacheConnection = new LinkedList<Connection>();

	private Map<String, Connection> cacheConnection = new ConcurrentSkipListMap<>();

	private boolean close;

	private boolean readOnly = true;

	private boolean autoCommit = true;

	private int transactionIsolation = 1;

	@Override
	public final CallableStatement prepareCall(final String sql) throws SQLException {
		throw new SQLFeatureNotSupportedException("prepareCall");
	}

	@Override
	public final CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		throw new SQLFeatureNotSupportedException("prepareCall");
	}

	@Override
	public final CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
		throw new SQLFeatureNotSupportedException("prepareCall");
	}

	@Override
	public final String nativeSQL(final String sql) throws SQLException {
		throw new SQLFeatureNotSupportedException("nativeSQL");
	}

	@Override
	public final Savepoint setSavepoint() throws SQLException {
		throw new SQLFeatureNotSupportedException("setSavepoint");
	}

	@Override
	public final Savepoint setSavepoint(final String name) throws SQLException {
		throw new SQLFeatureNotSupportedException("setSavepoint name");
	}

	@Override
	public final void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		throw new SQLFeatureNotSupportedException("releaseSavepoint");
	}

	@Override
	public final void rollback(final Savepoint savepoint) throws SQLException {
		throw new SQLFeatureNotSupportedException("rollback");
	}

	@Override
	public final void abort(final Executor executor) throws SQLException {
		throw new SQLFeatureNotSupportedException("abort");
	}

	@Override
	public final String getCatalog() throws SQLException {
		throw new SQLFeatureNotSupportedException("getCatalog");
	}

	@Override
	public final void setCatalog(final String catalog) throws SQLException {
		throw new SQLFeatureNotSupportedException("setCatalog");
	}

	@Override
	public final String getSchema() throws SQLException {
		throw new SQLFeatureNotSupportedException("getSchema");
	}

	@Override
	public final void setSchema(final String schema) throws SQLException {
		throw new SQLFeatureNotSupportedException("setSchema");
	}

	@Override
	public final Map<String, Class<?>> getTypeMap() throws SQLException {
		throw new SQLFeatureNotSupportedException("getTypeMap");
	}

	@Override
	public final void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		throw new SQLFeatureNotSupportedException("setTypeMap");
	}

	@Override
	public final int getNetworkTimeout() throws SQLException {
		throw new SQLFeatureNotSupportedException("getNetworkTimeout");
	}

	@Override
	public final void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
		throw new SQLFeatureNotSupportedException("setNetworkTimeout");
	}

	@Override
	public final Clob createClob() throws SQLException {
		throw new SQLFeatureNotSupportedException("createClob");
	}

	@Override
	public final Blob createBlob() throws SQLException {
		throw new SQLFeatureNotSupportedException("createBlob");
	}

	@Override
	public final NClob createNClob() throws SQLException {
		throw new SQLFeatureNotSupportedException("createNClob");
	}

	@Override
	public final SQLXML createSQLXML() throws SQLException {
		throw new SQLFeatureNotSupportedException("createSQLXML");
	}

	@Override
	public final Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
		throw new SQLFeatureNotSupportedException("createArrayOf");
	}

	@Override
	public final Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
		throw new SQLFeatureNotSupportedException("createStruct");
	}

	@Override
	public final boolean isValid(final int timeout) throws SQLException {
		throw new SQLFeatureNotSupportedException("isValid");
	}

	@Override
	public final Properties getClientInfo() throws SQLException {
		throw new SQLFeatureNotSupportedException("getClientInfo");
	}

	@Override
	public final String getClientInfo(final String name) throws SQLException {
		throw new SQLFeatureNotSupportedException("getClientInfo name");
	}

	@Override
	public final void setClientInfo(final String name, final String value) throws SQLClientInfoException {
		throw new UnsupportedOperationException("setClientInfo name value");
	}

	@Override
	public final void setClientInfo(final Properties properties) throws SQLClientInfoException {
		throw new UnsupportedOperationException("setClientInfo properties");
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <T> T unwrap(final Class<T> iface) throws SQLException {
		if (isWrapperFor(iface)) {
			return (T) this;
		}
		throw new SQLException(String.format("[%s] cannot be unwrapped as [%s]", getClass().getName(), iface.getName()));
	}

	@Override
	public final boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return iface.isInstance(this);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
	}

	@Override
	public final int getHoldability() throws SQLException {
		return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	@Override
	public final void setHoldability(final int holdability) throws SQLException {
	}

	@Override
	public void close() throws SQLException {
		List<SQLException> exceptions = new ArrayList<SQLException>();
		close = true;
		for (Connection connection : cacheConnection.values()) {
			try {
				connection.close();
			} catch (SQLException e) {
				exceptions.add(e);
			}
		}
		// 错误抛出
		if (exceptions.isEmpty())
			return;
		SQLException sqlException = new SQLException();
		for (SQLException e : exceptions)
			sqlException.setNextException(e);
		throw sqlException;
	}

	@Override
	public void commit() throws SQLException {
		List<SQLException> exceptions = new ArrayList<SQLException>();
		for (Connection connection : cacheConnection.values()) {
			try {
				connection.commit();
			} catch (SQLException e) {
				exceptions.add(e);
			}
		}
		// 错误抛出
		if (exceptions.isEmpty())
			return;
		SQLException sqlException = new SQLException();
		for (SQLException e : exceptions)
			sqlException.setNextException(e);
		throw sqlException;

	}

	@Override
	public Statement createStatement() throws SQLException {
		return new SimpleStatementDemo(this);
	}

	@Override
	public Statement createStatement(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return this.autoCommit;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return null;
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return this.transactionIsolation;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return close;
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return readOnly;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, String[] arg1) throws SQLException {
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rollback() throws SQLException {
		List<SQLException> exceptions = new ArrayList<SQLException>();
		for (Connection connection : cacheConnection.values()) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				exceptions.add(e);
			}
		}
		// 错误抛出
		if (exceptions.isEmpty())
			return;
		SQLException sqlException = new SQLException();
		for (SQLException e : exceptions)
			sqlException.setNextException(e);
		throw sqlException;
	}

	@Override
	public void setAutoCommit(boolean arg0) throws SQLException {
		this.autoCommit = arg0;
	}

	@Override
	public void setReadOnly(boolean arg0) throws SQLException {
		this.readOnly = arg0;
	}

	@Override
	public void setTransactionIsolation(int arg0) throws SQLException {
		this.transactionIsolation = arg0;
	}
}
