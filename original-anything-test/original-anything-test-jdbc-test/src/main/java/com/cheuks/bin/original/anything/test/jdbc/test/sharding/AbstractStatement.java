package com.cheuks.bin.original.anything.test.jdbc.test.sharding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import com.cheuks.bin.original.anything.test.jdbc.test.sharding.Impl.WrapperAdapter;

public abstract class AbstractStatement extends WrapperAdapter implements Statement {

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub
		
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
	public void setCursorName(String name) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
