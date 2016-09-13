package com.cheuks.bin.original.common.dbmanager.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.cheuks.bin.original.common.dbmanager.dao.BaseDao;
import com.cheuks.bin.original.common.util.ObjectFill;

public abstract class AbstractService<entity, ID extends Serializable> extends ObjectFill implements BaseService<entity, ID> {

	public abstract BaseDao<entity, ID> getService();

	public entity saveCustom(entity obj) throws Throwable {
		return getService().saveCustom(obj);
	}

	public List<entity> getList(Map<String, Object> params, boolean isLike, int page, int size) throws Throwable {
		if (null == params || params.size() == 0)
			return getService().getList(page, size);
		if (isLike)
			params.put("like", 1);
		return getService().getList(params, page, size);
	}

	public entity getByPk(ID id) throws Throwable {
		return getService().get(id);
	}

	public entity save(entity obj) throws Throwable {
		return getService().save(obj);
	}

	public void saveOrUpdate(entity obj) throws Throwable {
		getService().saveOrUpeate(obj);
	}

	public void update(entity obj) throws Throwable {
		getService().update(obj);
	}

	public void update(ID id, Map<String, Object> params) throws Throwable {
		entity e = getService().get(id);
		e = fillObject(e, params);
		getService().update(e);
	}

	public void delete(entity obj) throws Throwable {
		getService().delete(obj);
	}

	public int executeUpdate(String queryName, Map<String, Object> params, boolean isHql, boolean isFromat) throws Throwable {
		return getService().executeUpdate(queryName, params, isHql, isFromat);
	}

	public int getCount(Map<String, Object> params) throws Throwable {
		return Integer.valueOf(getService().uniqueResult("count", true, params).toString());
	}

}
