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

	DBAdapter<Session> setSessionFactory(String name);

	Session getSession();

	/***
	 * 列表
	 * 
	 * @param c
	 *            实体类文件
	 * @return
	 * @throws Throwable
	 */
	<T> List<T> getList(Class<?> c) throws Throwable;

	/***
	 * 带分页列表
	 * 
	 * @param c
	 * @param page
	 * @param size
	 * @return
	 * @throws Throwable
	 */
	<T> List<T> getList(Class<?> c, int page, int size) throws Throwable;

	/***
	 * 通过ID查询
	 * 
	 * @param clazz
	 * @param obj
	 * @return
	 * @throws Throwable
	 */
	<T> T get(Class<T> clazz, Serializable id) throws Throwable;

	/***
	 * 通过ID查询
	 * 
	 * @param clazz
	 * @param obj
	 * @return
	 * @throws Throwable
	 */
	<T> T load(Class<T> clazz, Serializable id) throws Throwable;

	/***
	 * 通过ID删除
	 * 
	 * @param obj
	 * @throws Throwable
	 */
	void delete(Object obj) throws Throwable;

	/***
	 * 批量删除
	 * 
	 * @param list
	 * @return
	 * @throws Throwable
	 */
	int deleteList(List<?> list) throws Throwable;

	/***
	 * 更新
	 * 
	 * @param o
	 * @throws Throwable
	 */
	void update(Object o) throws Throwable;

	/***
	 * 批量更新
	 * 
	 * @param o
	 * @throws Throwable
	 */
	int updateList(List<?> list) throws Throwable;

	/***
	 * 批量插入
	 * 
	 * @param list
	 * @return
	 * @throws Throwable
	 */
	int saveList(List<?> list) throws Throwable;

	/***
	 * 插入
	 * 
	 * @param t
	 * @return
	 * @throws Throwable
	 */
	<T> T save(T t) throws Throwable;

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

	int executeUpdate(String xql, boolean isCustomSQL) throws Throwable;

	int executeUpdate(String xql, Map<String, Object> params, boolean isCustomSQL) throws Throwable;

	int executeUpdate(String queryName, Map<String, Object> params, boolean isCustomSQL, boolean isFromat) throws Throwable;

	<T> T execute(String xql, Map<String, Object> params, boolean isCustomSQL, boolean isFromat) throws Throwable;

	<T> T execute(String xql, List<Object> params, boolean isCustomSQL, boolean isFromat) throws Throwable;

	<T> List<T> getList(String xql, Map<String, Object> params, boolean isCustomSQL, boolean isFromat) throws Throwable;

	<T> List<T> getList(String xql, List<Object> params, boolean isCustomSQL, boolean isFromat) throws Throwable;

	public int generateInsertSQL(Serializable entity) throws Throwable;

	public String queryNameFormat(Class<?> entry, String queryName);

	public String queryNameFormat(String entryName, String queryName);

}
