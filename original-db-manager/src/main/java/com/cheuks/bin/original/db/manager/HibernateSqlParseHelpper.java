package com.cheuks.bin.original.db.manager;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.util.SoftConcurrentHashMap;

public class HibernateSqlParseHelpper {

	private static final Logger LOG = LoggerFactory.getLogger(HibernateSqlParseHelpper.class);

	private static final HibernateSqlParseHelpper newInstance = new HibernateSqlParseHelpper();

	private static final SoftConcurrentHashMap<String, Object> cache = new SoftConcurrentHashMap<String, Object>();

	public static final HibernateSqlParseHelpper newInstance() {
		return newInstance;
	}

	@Deprecated
	public final String lastIdentityId() {
		return "SELECT @@IDENTITY";
	}

	public static class SqlParseHelpper {
		private String sql;
		private Map<String, Object> params;

		public SqlParseHelpper(String sql, Map<String, Object> params) {
			super();
			this.sql = sql;
			this.params = params;
		}

		public String getSql() {
			return sql;
		}

		public SqlParseHelpper setSql(String sql) {
			this.sql = sql;
			return this;
		}

		public Map<String, Object> getParams() {
			return params;
		}

		public SqlParseHelpper setParams(Map<String, Object> params) {
			this.params = params;
			return this;
		}
	}

	public final SqlParseHelpper insert(Serializable entity) {
		Field[] tempfields = null;
		Map<String, Field> fields = getCache(entity.getClass().getName());
		Map<String, Object> params = new HashMap<String, Object>();
		if (null == fields) {
			tempfields = entity.getClass().getDeclaredFields();
			fields = new HashMap<String, Field>();

			for (Field f : tempfields) {
				if (!isModifier(f))
					continue;
				f.setAccessible(true);
				fields.put(f.getName(), f);
				putCache(entity.getClass().getName(), fields);
			}
		}
		Field field;
		String result = getCache(entity.getClass().getName() + ".insert");
		StringBuilder sb = new StringBuilder().append(null == result ? result : "");
		StringBuilder value = new StringBuilder();
		if (null == result) {
			sb = new StringBuilder();
			sb.append("INSERT INTO ");
			value.append(" value(");
			// 注解表名
			Entity tempEntity = entity.getClass().getDeclaredAnnotation(Entity.class);
			if (null == tempEntity)
				return null;
			if (tempEntity.name().length() < 1) {
				Table tempTable = entity.getClass().getDeclaredAnnotation(Table.class);
				sb.append(tempTable.name().length() > 1 ? tempTable.name() : entity.getClass().getSimpleName());
			} else
				sb.append(tempEntity.name());
			sb.append("(");
		}
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
		return new SqlParseHelpper(sb.toString(), params);
	}

	protected boolean isModifier(Field field) {
		if (Modifier.isStatic(field.getModifiers()))
			return false;
		else if (Modifier.isTransient(field.getModifiers()))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getCache(String key) {
		Object o = cache.get(key);
		return null == o ? null : (T) o;
	}

	public static void putCache(String key, Object value) {
		cache.put(key, value);
	}

}
