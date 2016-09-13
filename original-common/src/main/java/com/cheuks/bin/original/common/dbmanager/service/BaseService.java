package com.cheuks.bin.original.common.dbmanager.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<entity, ID extends Serializable> {

	entity getByPk(ID obj) throws Throwable;

	List<entity> getList(Map<String, Object> params, boolean isLike, int page, int size) throws Throwable;

	entity save(entity obj) throws Throwable;

	entity saveCustom(entity obj) throws Throwable;

	void saveOrUpdate(entity obj) throws Throwable;

	void update(entity obj) throws Throwable;

	void update(ID id, Map<String, Object> params) throws Throwable;

	void delete(entity obj) throws Throwable;

	int executeUpdate(String queryName, Map<String, Object> params, boolean isHql, boolean isFromat) throws Throwable;

	int getCount(Map<String, Object> params) throws Throwable;

}
