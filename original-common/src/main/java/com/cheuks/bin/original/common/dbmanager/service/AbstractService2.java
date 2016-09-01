package com.cheuks.bin.original.common.dbmanager.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.cheuks.bin.original.common.dbmanager.dao.AbstractDao;

public abstract class AbstractService2<entity, ID extends Serializable> implements BaseService2<entity, ID> {

	public abstract AbstractDao<entity, ID> getService();

	public List<entity> getList(Map<String, Object> params, int page, int size) throws Throwable {
		if (null == params)
			return getService().getList(page, size);
		return getService().getList(params, page, size);
	}

}
