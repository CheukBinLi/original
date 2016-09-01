package com.cheuks.bin.original.common.dbmanager.service;

import java.util.List;
import java.util.Map;

public interface BaseService2<entity, ID> {

	entity getByPk(ID id) throws Throwable;

	List<entity> getList() throws Throwable;

	entity save(Map<String, Object> params) throws Throwable;

	void update(Map<String, Object> params) throws Throwable;

	void delete(ID id) throws Throwable;

}
