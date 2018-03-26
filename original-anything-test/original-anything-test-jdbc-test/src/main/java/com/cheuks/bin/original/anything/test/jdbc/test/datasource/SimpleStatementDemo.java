package com.cheuks.bin.original.anything.test.jdbc.test.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;

@Getter
public class SimpleStatementDemo implements Statement {

	private boolean close;

	private SimpleConnectionDemo connection;

	public SimpleStatementDemo(final SimpleConnectionDemo connection) {
		this.connection = connection;
	}

	protected Collection<? extends Statement> getRoutedStatements() {
		return null;
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
	public final int getFetchDirection() throws SQLException {
		throw new SQLFeatureNotSupportedException("getFetchDirection");
	}

	@Override
	public final void setFetchDirection(final int direction) throws SQLException {
		throw new SQLFeatureNotSupportedException("setFetchDirection");
	}

	@Override
	public final void addBatch(final String sql) throws SQLException {
		throw new SQLFeatureNotSupportedException("addBatch sql");
	}

	@Override
	public void clearBatch() throws SQLException {
		throw new SQLFeatureNotSupportedException("clearBatch");
	}

	@Override
	public int[] executeBatch() throws SQLException {
		throw new SQLFeatureNotSupportedException("executeBatch");
	}

	@Override
	public final void closeOnCompletion() throws SQLException {
		throw new SQLFeatureNotSupportedException("closeOnCompletion");
	}

	@Override
	public final boolean isCloseOnCompletion() throws SQLException {
		throw new SQLFeatureNotSupportedException("isCloseOnCompletion");
	}

	@Override
	public final void setCursorName(final String name) throws SQLException {
		throw new SQLFeatureNotSupportedException("setCursorName");
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() throws SQLException {
		this.close = true;
		List<SQLException> exceptions = new ArrayList<SQLException>();
		for (Statement statement : getRoutedStatements()) {
			try {
				statement.close();
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
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		List<SQLException> exceptions = new ArrayList<SQLException>();
		for (Statement statement : getRoutedStatements()) {
			try {
				statement.setEscapeProcessing(enable);
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
	public int getQueryTimeout() throws SQLException {
		return getRoutedStatements().iterator().next().getQueryTimeout();
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		List<SQLException> exceptions = new ArrayList<SQLException>();
		for (Statement statement : getRoutedStatements()) {
			try {
				statement.setQueryTimeout(seconds);
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
	public void cancel() throws SQLException {
		List<SQLException> exceptions = new ArrayList<SQLException>();
		for (Statement statement : getRoutedStatements()) {
			try {
				statement.cancel();
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
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean execute(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		long result = 0;
		for (Statement each : getRoutedStatements()) {
			if (each.getUpdateCount() > -1) {
				result += each.getUpdateCount();
			}
		}
		if (result > Integer.MAX_VALUE) {
			result = Integer.MAX_VALUE;
		}
		return result > 0 ? Long.valueOf(result).intValue() : -1;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		List<SQLException> exceptions = new ArrayList<SQLException>();
		for (Statement statement : getRoutedStatements()) {
			try {
				statement.setFetchSize(rows);
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
	public int getFetchSize() throws SQLException {
		return getRoutedStatements().iterator().next().getFetchSize();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		return getRoutedStatements().iterator().next().getResultSetType();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.connection;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		return false;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.close;
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		List<SQLException> exceptions = new ArrayList<SQLException>();
		for (Statement statement : getRoutedStatements()) {
			try {
				statement.setPoolable(poolable);
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
	public boolean isPoolable() throws SQLException {
		return getRoutedStatements().iterator().next().isPoolable();
	}

}
