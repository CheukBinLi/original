package com.cheuks.bin.original.db.manager.mybatis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.dbmanager.DBAdapter;

@SuppressWarnings({ "unused", "unchecked" })
public abstract class AbstractMybatisDBAdapter implements DBAdapter<SqlSession> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractMybatisDBAdapter.class);

	private Map<String, SqlSessionFactory> dataSource = new ConcurrentHashMap<String, SqlSessionFactory>();

	protected <T> T converyType(Object o) {
		return null == o ? null : (T) o;
	}

	public Map<String, SqlSessionFactory> getDataSource() {
		return dataSource;
	}

	public final AbstractMybatisDBAdapter setDataSource(Map<String, SqlSessionFactory> dataSource) {
		this.dataSource.putAll(dataSource);
		return this;
	}

	public <T> List<T> getList(Class<?> c) throws Throwable {
		return getSession().selectList(queryNameFormat(c, "list"));
	}

	public <T> List<T> getList(Class<?> c, int page, int size) throws Throwable {
		return getSession().selectList(queryNameFormat(c, "list"));
	}

	public <T> List<T> getListByCustomSQL(String hql, Object... params) throws Throwable {
		// return getSession().selectList(hql, params);
		throw new Throwable("not supprot");
	}

	public <T> List<T> getListByCustomSQL(String hql, int page, int size, Object... params) throws Throwable {
		throw new Throwable("not supprot");
	}

	public <T> List<T> getListBySQL(String sql, Object... params) throws Throwable {
		throw new Throwable("not supprot");
	}

	public <T> List<T> getListBySQL(String sql, int page, int size, Object... params) throws Throwable {
		throw new Throwable("not supprot");
	}

	public Object uniqueResult(String xql, boolean isHql, Object... params) throws Throwable {
		return null;
	}

	public Object uniqueResult(String queryName, boolean isHql, boolean isFormat, Map<String, Object> params) throws Throwable {
		return null;
	}

	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, boolean isFormat, Map<String, Object> params) throws Throwable {
		return null;
	}

	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, Object... params) throws Throwable {
		return null;
	}

	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, int page, int size, Object... params) throws Throwable {
		return null;
	}

	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, boolean isFormat, Map<String, Object> params, int page, int size) throws Throwable {
		return null;
	}

	public <T> T get(Class<T> clazz, Serializable id) throws Throwable {
		return null;
	}

	public <T> T load(Class<T> clazz, Serializable id) throws Throwable {
		return null;
	}

	public void delete(Object obj) throws Throwable {

	}

	public int deleteList(List<?> list) throws Throwable {
		return 0;
	}

	public void update(Object o) throws Throwable {

	}

	public int updateList(List<?> list) throws Throwable {
		return 0;
	}

	public int saveList(List<?> list) throws Throwable {
		return 0;
	}

	public <T> T save(T t) throws Throwable {
		return null;
	}

	public <T> T replicate(T t, String ReplicationMode) throws Throwable {
		return null;
	}

	public void saveOrUpdate(Object t) throws Throwable {

	}

	public int executeUpdate(String xql, boolean isHql) throws Throwable {
		return 0;
	}

	public int executeUpdate(String xql, Map<String, Object> params, boolean isHql) throws Throwable {
		return 0;
	}

	public int executeUpdate(String queryName, Map<String, Object> params, boolean isHql, boolean isFromat) throws Throwable {
		return 0;
	}

	public int generateInsertSQL(Serializable entity) throws Throwable {
		// return 0;
		throw new Throwable("not supprot");
	}

	public String queryNameFormat(Class<?> entry, String queryName) {
		return entry.getName() + "." + queryName;
	}

	public String queryNameFormat(String entryName, String queryName) {
		return entryName + "." + queryName;
	}

}
