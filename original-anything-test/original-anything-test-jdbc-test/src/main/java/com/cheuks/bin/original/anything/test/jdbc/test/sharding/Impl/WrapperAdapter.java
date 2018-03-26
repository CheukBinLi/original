package com.cheuks.bin.original.anything.test.jdbc.test.sharding.Impl;

import java.sql.SQLException;
import java.sql.Wrapper;

import lombok.NonNull;

/***
 * 
 * @Title: original-anything-test-jdbc-test
 * @Description: 包装类适配器
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2018年1月2日 下午6:07:14
 *
 */
public class WrapperAdapter implements Wrapper {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (isWrapperFor(iface)) {
			return (T) this;
		}
		throw new SQLException(String.format("[%s] cannot be unwrapped as [%s]", getClass().getName(), iface.getName()));
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return iface.isInstance(this);
	}

	/***
	 * 回放操作
	 * 
	 * @param target
	 */
	public void replayMethodsInvocation(final Object target) {
	}

	/***
	 * 记录操作
	 * 
	 * @param clazz
	 * @param methodName
	 *            方法名
	 * @param paramsType
	 *            参数类型
	 * @param params
	 *            参数
	 */
	public void recordMethodsInvocation(final Class<?> clazz, final String methodName, final Class<?>[] paramsType, final Object... params) {
	}

	/***
	 * 追加SQLException错误信息
	 * 
	 * @param target
	 * @param e
	 * @return
	 */
	public SQLException checkAndAppendException(SQLException target, final SQLException e) {
		if (null == e)
			return target;

		if (null == target)
			target = new SQLException();

		target.setNextException(e);
		return target;
	}

	/***
	 * 抛出方法
	 * 
	 * @param e
	 * @throws SQLException
	 */
	public void throwSQLExceptionIfNecessary(SQLException e) throws SQLException {
		if (null != e)
			throw e;
	}

}
