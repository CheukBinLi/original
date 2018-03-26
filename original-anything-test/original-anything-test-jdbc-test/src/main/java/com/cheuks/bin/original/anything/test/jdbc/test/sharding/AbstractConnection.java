package com.cheuks.bin.original.anything.test.jdbc.test.sharding;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executor;

import com.cheuks.bin.original.anything.test.jdbc.test.sharding.Impl.WrapperAdapter;

import lombok.Getter;

@Getter
public abstract class AbstractConnection extends WrapperAdapter implements Connection {

	protected Map<String, Connection> CONNECTIONS = new ConcurrentSkipListMap<String, Connection>();

	private boolean autoCommit = true;

	private boolean close;

	private boolean readOnly = true;

	private int transactionIsolation;

	public Class<?> getTargetClass() {
		return Connection.class;
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		throw new SQLFeatureNotSupportedException("nativeSQL");
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.autoCommit = autoCommit;
		recordMethodsInvocation(getTargetClass(), "setAutoCommit", new Class[]{boolean.class}, autoCommit);
		for (Connection each : CONNECTIONS.values()) {
			each.setAutoCommit(autoCommit);
		}

	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return this.autoCommit;
	}

	@Override
	public void commit() throws SQLException {
		SQLException exception = null;
		for (Connection each : CONNECTIONS.values()) {
			try {
				each.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				checkAndAppendException(exception, e);
			}
		}
		throwSQLExceptionIfNecessary(exception);
	}

	@Override
	public void rollback() throws SQLException {

	}

	@Override
	public void close() throws SQLException {
		this.close = true;
		SQLException exception = null;
		for (Connection each : CONNECTIONS.values()) {
			try {
				each.close();
			} catch (SQLException e) {
				checkAndAppendException(exception, e);
			}
		}
		throwSQLExceptionIfNecessary(exception);
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.close;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return null;
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		this.readOnly = readOnly;
		SQLException exception = null;
		recordMethodsInvocation(getTargetClass(), "setReadOnly", new Class<?>[]{boolean.class}, readOnly);
		for (Connection each : CONNECTIONS.values()) {
			try {
				each.setReadOnly(readOnly);
			} catch (SQLException e) {
				checkAndAppendException(exception, e);
			}
		}
		throwSQLExceptionIfNecessary(exception);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return this.readOnly;
	}

	/***
	 * 设置database </br>
	 * use database
	 */
	@Override
	public void setCatalog(String catalog) throws SQLException {
		throw new SQLFeatureNotSupportedException("multiple database can't supported setCatalog");
	}

	/***
	 * 查看当前使用的database
	 */
	@Override
	public String getCatalog() throws SQLException {
		throw new SQLFeatureNotSupportedException("multiple database can't supported getCatalog");
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		this.transactionIsolation = level;
		recordMethodsInvocation(getTargetClass(), "setTransactionIsolation", new Class<?>[]{int.class}, level);
		for (Connection each : CONNECTIONS.values()) {
			each.setTransactionIsolation(level);
		}
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		// return CONNECTIONS.values().iterator().next().getTransactionIsolation();
		return this.transactionIsolation;
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
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return null;
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public int getHoldability() throws SQLException {
		return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		this.close = true;
		SQLException exception = null;
		for (Connection each : CONNECTIONS.values()) {
			try {
				each.rollback();
			} catch (SQLException e) {
				checkAndAppendException(exception, e);
			}
		}
		throwSQLExceptionIfNecessary(exception);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
