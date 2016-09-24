package com.cheuks.bin.original.db.manager;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.UnknownEntityTypeException;
import org.hibernate.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.cache.CacheFactory;
import com.cheuks.bin.original.common.dbmanager.DBAdapter;
import com.cheuks.bin.original.common.dbmanager.QueryFactory;
import com.cheuks.bin.original.common.util.SoftConcurrentHashMap;

import freemarker.template.TemplateException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractHibernateDBAdapter implements DBAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractHibernateDBAdapter.class);

	private Map<String, SessionFactory> dataSource = new ConcurrentHashMap<String, SessionFactory>();

	protected <T> T converyType(Object o) {
		return null == o ? null : (T) o;
	}

	private QueryFactory queryFactory;

	public abstract Session getSession();

	protected CacheFactory<String, Object> cache = new CacheFactory<String, Object>() {

		SoftConcurrentHashMap<String, Object> cache = new SoftConcurrentHashMap<String, Object>();

		public Collection<Object> values() throws CacheException {
			return null;
		}

		public Object take(String key) throws CacheException {
			return this.cache.get(key);
		}

		public int size() throws CacheException {
			return this.cache.size();
		}

		public Object remove(String key) throws CacheException {
			return this.cache.remove(key);
		}

		public Object put(String key, Object value) throws CacheException {
			return this.cache.put(key, value);
		}

		public Set<String> keys() throws CacheException {
			return null;
		}

		public void clear() throws CacheException {
			this.cache = new SoftConcurrentHashMap<String, Object>();
		}
	};

	public <T> List<T> getList(Class<?> c) throws Throwable {
		return getList(c, -1, -1);
	}

	public <T> List<T> getList(Class<?> c, int page, int size) throws Throwable {
		Query query = getSession().createQuery(String.format("FROM %s a", c.getName()));
		List list = page > 0 ? page(query, page, size).list() : query.list();
		return null == list ? null : list;
	}

	public <T> List<T> getListByHQL(String hql, Object... params) throws Throwable {
		return getListByHQL(hql, -1, -1, params);
	}

	public <T> List<T> getListByHQL(String hql, int page, int size, Object... params) throws Throwable {
		Query query = fillParams(getSession().createQuery(hql), params);
		List list = page > 0 ? page(query, page, page).list() : query.list();
		return null == list ? null : list;
	}

	public <T> List<T> getListBySQL(String sql, Object... params) throws Throwable {
		return getListBySQL(sql, -1, -1, params);
	}

	public <T> List<T> getListBySQL(String sql, int page, int size, Object... params) throws Throwable {
		Query query = fillParams(getSession().createSQLQuery(sql), params);
		List list = page > 0 ? page(query, page, page).list() : query.list();
		return null == list ? null : list;
	}

	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, Object... params) throws Throwable {
		return getListByXqlQueryName(queryName, isHQL, -1, -1, params);
	}

	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, int page, int size, Object... params) throws Throwable {
		String xql = queryFactory.get(queryName, null, false);
		Query query = fillParams(isHQL ? getSession().createQuery(xql) : getSession().createSQLQuery(xql), params);
		List list = page > 0 ? page(query, page, size).list() : query.list();
		return null == list ? null : list;
	}

	public Object uniqueResult(String xql, boolean isHQL, Object... params) {
		Query query = fillParams(isHQL ? getSession().createQuery(xql) : getSession().createSQLQuery(xql), params);
		Object o = query.uniqueResult();
		return null == o ? null : o;
	}

	public Object uniqueResult(String queryName, boolean isHQL, boolean isFormat, Map<String, Object> params) throws Throwable {
		String xql = queryFactory.get(queryName, params, isFormat);
		Query query = fillParams(isHQL ? getSession().createQuery(xql) : getSession().createSQLQuery(xql), params);
		Object o = query.uniqueResult();
		return null == o ? null : o;
	}

	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, boolean isFormat, Map<String, Object> params) throws Throwable {
		return getListByXqlQueryName(queryName, isHQL, isFormat, params, -1, -1);
	}

	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, boolean isFormat, Map<String, Object> params, int page, int size) throws Throwable {
		String xql = queryFactory.get(queryName, params, isFormat);
		// System.err.println("XQL:" + xql);
		Query query = fillParams(isHQL ? getSession().createQuery(xql) : getSession().createSQLQuery(xql), params);
		List list = page > 0 ? page(query, page, size).list() : query.list();
		return null == list ? null : list;
	}

	public <T> T get(Class<T> clazz, Serializable id) throws Throwable {
		try {
			Object o = getSession().get(clazz, id);
			return (T) (null == o ? null : o);
		} catch (UnknownEntityTypeException e) {
			// e.printStackTrace();
		}
		return null;
	}

	public <T> T load(Class<T> clazz, Serializable id) throws Throwable {
		Object o = getSession().load(clazz, id);
		return (T) (null == o ? null : o);
	}

	public void delete(Object obj) throws Throwable {
		getSession().delete(obj);
	}

	public int deleteList(List<?> list) throws Throwable {
		int count = 0;
		Session s = getSession();
		for (int i = 0, len = list.size(); i < len; i++) {
			s.delete(list.get(i));
			if (i % 30 == 0) {
				s.flush();
				s.clear();
			}
			count++;
		}
		return count;
	}

	public void update(Object o) throws Throwable {
		getSession().update(o);
	}

	public int updateList(List<?> list) throws Throwable {
		int count = 0;
		Session s = getSession();
		for (int i = 0, len = list.size(); i < len; i++) {
			s.update(list.get(i));
			if (i % 30 == 0) {
				s.flush();
				s.clear();
			}
			count++;
		}
		return count;
	}

	public int saveList(List<?> list) throws Throwable {
		int count = 0;
		Session s = getSession();
		for (int i = 0, len = list.size(); i < len; i++) {
			s.save(list.get(i));
			if (i % 30 == 0) {
				s.flush();
				s.clear();
			}
			count++;
		}
		return count;
	}

	public <T> T save(final T o) throws Throwable {
		getSession().save(o);
		return o;
	}

	public <T> T replicate(T o, String ReplicationMode) throws Throwable {
		getSession().replicate(o, org.hibernate.ReplicationMode.valueOf(ReplicationMode));
		return o;
	}

	public void saveOrUpdate(Object t) throws Throwable {
		getSession().saveOrUpdate(t);
	}

	public int executeUpdate(String xql, boolean isHql) throws Throwable {
		Query query = isHql ? getSession().createQuery(xql) : getSession().createSQLQuery(xql);
		return query.executeUpdate();
	}

	public int executeUpdate(String xql, Map<String, Object> params, boolean isHql) throws Throwable {
		Query query = fillParams(isHql ? getSession().createQuery(xql) : getSession().createSQLQuery(xql), params);
		return query.executeUpdate();
	}

	public int executeUpdate(String queryName, Map<String, Object> params, boolean isHql, boolean isFromat) throws Throwable {
		String xql = queryFactory.get(queryName, params, isFromat);
		Query query = fillParams(isHql ? getSession().createQuery(xql) : getSession().createSQLQuery(xql), params);
		return query.executeUpdate();

	}

	public int generateInsertSQL(Serializable entity) throws Throwable {
		Field[] tempfields = converyType(cache.take(entity.getClass().getName()));
		Map<String, Field> fields = null;
		Map<String, Object> params = null;
		if (null == tempfields) {
			tempfields = entity.getClass().getDeclaredFields();
			fields = new WeakHashMap<String, Field>();
			params = new HashMap<String, Object>();
			for (Field f : tempfields) {
				if (!isModifier(f))
					continue;
				f.setAccessible(true);
				fields.put(f.getName(), f);
			}
		}
		Field field;
		StringBuilder sb = new StringBuilder();
		StringBuilder value = new StringBuilder();
		sb.append("INSERT INTO ");
		value.append(" value(");
		// 注解表名
		Entity tempEntity = entity.getClass().getDeclaredAnnotation(Entity.class);
		if (null == tempEntity)
			return -1;
		if (tempEntity.name().length() < 1) {
			Table tempTable = entity.getClass().getDeclaredAnnotation(Table.class);
			sb.append(tempTable.name().length() > 1 ? tempTable.name() : entity.getClass().getSimpleName());
		} else
			sb.append(tempEntity.name());
		sb.append("(");
		Object param;
		Column column;
		for (Map.Entry<String, Field> en : fields.entrySet()) {
			field = en.getValue();
			try {
				if (null == field || null == (param = field.get(entity)))
					continue;
				column = field.getDeclaredAnnotation(Column.class);
				sb.append(null != column && column.name().length() > 1 ? column.name() : en.getKey()).append(",");
				value.append(":" + field.getName() + ",");
				params.put(en.getKey(), param);
			} catch (Exception e) {
				LOG.error("insert", e);
			}
		}
		value.deleteCharAt(value.length() - 1).append(")");
		sb.deleteCharAt(sb.length() - 1).append(") ").append(value);

		return executeUpdate(sb.toString(), params, false);
	}

	protected Query fillParams(Query q, Object... o) {
		if (null == o || null == q) {
			return q;
		}
		for (int i = 0, len = o.length; i < len; i++) {
			q.setParameter(i, o[i]);
		}
		return q;
	}

	protected Query fillParams(Query q, Map<String, ?> o) {
		if (null == o || null == q) {
			return q;
		}
		for (Entry<String, ?> en : o.entrySet())
			try {
				q.setParameter(en.getKey(), en.getValue());
			} catch (Exception e) {
			}
		return q;
	}

	protected Query page(Query q, int pageNum, int size) {
		if (pageNum >= 0 && size >= 0) {
			q.setFirstResult(size * (pageNum - 1));
			q.setMaxResults(size);
		}
		return q;
	}

	protected boolean isModifier(Field field) {
		if (Modifier.isStatic(field.getModifiers()))
			return false;
		else if (Modifier.isTransient(field.getModifiers()))
			return false;
		return true;
	}

	public String queryNameFormat(Class<?> entry, String queryName) {
		return String.format("%s.%s", entry.getName(), queryName).toLowerCase();
	}

	public String queryNameFormat(String entryName, String queryName) {
		return String.format("%s.%s", entryName, queryName).toLowerCase();
	}

	public QueryFactory getQueryFactory() {
		return queryFactory;
	}

	public AbstractHibernateDBAdapter setQueryFactory(QueryFactory queryFactory) {
		this.queryFactory = queryFactory;
		return this;
	}

	public Map<String, SessionFactory> getDataSource() {
		return dataSource;
	}

	public final AbstractHibernateDBAdapter setDataSource(Map<String, SessionFactory> dataSource) {
		this.dataSource.putAll(dataSource);
		return this;
	}
}
