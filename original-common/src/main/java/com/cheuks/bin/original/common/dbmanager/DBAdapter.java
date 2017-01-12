package com.cheuks.bin.original.common.dbmanager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/***
 * *
 * 
 * Copyright 2016 CHEUK.BIN.LI Individual All
 * 
 * ALL RIGHT RESERVED
 * 
 * CREATE ON 2016年4月28日
 * 
 * EMAIL:20796698@QQ.COM
 * 
 * 
 * @author CHEUK.BIN.LI
 * 
 * @see 数据源协同适配器
 *
 */
public interface DBAdapter<Session> {

	public DBAdapter<Session> setSessionFactory(String name);

	public Session getSession();

	/***
	 * 列表
	 * 
	 * @param c
	 *            实体类文件
	 * @return
	 * @throws Throwable
	 */
	public <T> List<T> getList(Class<?> c) throws Throwable;

	/***
	 * 带分页列表
	 * 
	 * @param c
	 * @param page
	 * @param size
	 * @return
	 * @throws Throwable
	 */
	public <T> List<T> getList(Class<?> c, int page, int size) throws Throwable;

	/***
	 * HQL列表查询
	 * 
	 * @param hql
	 *            hql
	 * @param params
	 *            参数(?)
	 * @return
	 * @throws Throwable
	 */
	public <T> List<T> getListByHQL(String hql, Object... params) throws Throwable;

	/***
	 * 
	 * 带分页HQL列表查询
	 * 
	 * @param hql
	 *            hql
	 * @param page
	 *            页码
	 * @param size
	 *            条目
	 * @param params
	 *            参数( ? )
	 * @return
	 * @throws Throwable
	 */
	public <T> List<T> getListByHQL(String hql, int page, int size, Object... params) throws Throwable;

	/***
	 * SQL列表查询
	 * 
	 * @param sql
	 *            sql
	 * @param params
	 *            参数( ? )
	 * @return
	 * @throws Throwable
	 */
	public <T> List<T> getListBySQL(String sql, Object... params) throws Throwable;

	/***
	 * 
	 * 带分页SQL列表查询
	 * 
	 * @param ql
	 *            sql
	 * @param page
	 *            页码
	 * @param size
	 *            条目
	 * @param params
	 *            参数( ? )
	 * @return
	 * @throws Throwable
	 */
	public <T> List<T> getListBySQL(String sql, int page, int size, Object... params) throws Throwable;

	public Object uniqueResult(String xql, boolean isHql, Object... params) throws Throwable;

	public Object uniqueResult(String queryName, boolean isHql, boolean isFormat, Map<String, Object> params) throws Throwable;

	/***
	 * query模板查询
	 * 
	 * @param queryName
	 *            查询语句名
	 * @param params
	 *            参数( :name )
	 * @return
	 * @throws Throwable
	 */
	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, boolean isFormat, Map<String, Object> params) throws Throwable;

	/***
	 * 
	 * @param queryName
	 *            模板查询
	 * @param params
	 *            参数( 问题号 ? :数组传入 )
	 * @return
	 * @throws Throwable
	 */
	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, Object... params) throws Throwable;

	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, int page, int size, Object... params) throws Throwable;

	/***
	 * 
	 * query模板查询 * @param queryName 查询语句名
	 * 
	 * @param params
	 *            参数( :name )
	 * @param page
	 * @param size
	 * @return
	 * @throws Throwable
	 */
	public <T> List<T> getListByXqlQueryName(String queryName, boolean isHQL, boolean isFormat, Map<String, Object> params, int page, int size) throws Throwable;

	/***
	 * 通过ID查询
	 * 
	 * @param clazz
	 * @param obj
	 * @return
	 * @throws Throwable
	 */
	public <T> T get(Class<T> clazz, Serializable id) throws Throwable;

	/***
	 * 通过ID查询
	 * 
	 * @param clazz
	 * @param obj
	 * @return
	 * @throws Throwable
	 */
	public <T> T load(Class<T> clazz, Serializable id) throws Throwable;

	/***
	 * 通过ID删除
	 * 
	 * @param obj
	 * @throws Throwable
	 */
	public void delete(Object obj) throws Throwable;

	/***
	 * 批量删除
	 * 
	 * @param list
	 * @return
	 * @throws Throwable
	 */
	public int deleteList(List<?> list) throws Throwable;

	/***
	 * 更新
	 * 
	 * @param o
	 * @throws Throwable
	 */
	public void update(Object o) throws Throwable;

	/***
	 * 批量更新
	 * 
	 * @param o
	 * @throws Throwable
	 */
	public int updateList(List<?> list) throws Throwable;

	/***
	 * 批量插入
	 * 
	 * @param list
	 * @return
	 * @throws Throwable
	 */
	public int saveList(List<?> list) throws Throwable;

	/***
	 * 插入
	 * 
	 * @param t
	 * @return
	 * @throws Throwable
	 */
	public <T> T save(T t) throws Throwable;

	/***
	 * 插入
	 * 
	 * @param t
	 *            对象
	 * @param ReplicationMode
	 *            插入模式()
	 * @return
	 * @throws Throwable
	 */
	public <T> T replicate(T t, String ReplicationMode) throws Throwable;

	/***
	 * 插入或更新
	 * 
	 * @param t
	 * @throws Throwable
	 */
	public void saveOrUpdate(Object t) throws Throwable;

	int executeUpdate(String xql, boolean isHql) throws Throwable;

	int executeUpdate(String xql, Map<String, Object> params, boolean isHql) throws Throwable;

	int executeUpdate(String queryName, Map<String, Object> params, boolean isHql, boolean isFromat) throws Throwable;

	public int generateInsertSQL(Serializable entity) throws Throwable;

	public String queryNameFormat(Class<?> entry, String queryName);

	public String queryNameFormat(String entryName, String queryName);

}
