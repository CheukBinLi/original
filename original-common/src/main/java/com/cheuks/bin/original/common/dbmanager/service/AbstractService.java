package com.cheuks.bin.original.common.dbmanager.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.cheuks.bin.original.common.dbmanager.BasePage;
import com.cheuks.bin.original.common.dbmanager.dao.BaseDao;
import com.cheuks.bin.original.common.util.GeneratedIDService;
import com.cheuks.bin.original.common.util.ObjectFill;

public abstract class AbstractService<entity, ID extends Serializable> extends ObjectFill implements BaseService<entity, ID> {

    public abstract BaseDao<entity, ID> getDao();

    public Long generateId() {
        return GeneratedIDService.newInstance().nextID();
    }

    public entity saveCustom(entity obj) throws Throwable {
        return getDao().saveCustom(obj);
    }

    public List<entity> getList(Map<String, Object> params, int page, int size) throws Throwable {
        if (null == params || params.size() == 0)
            return getDao().getList(page, size);
        return getDao().getList(params, page, size);
    }

    public List<entity> getList(Map<String, Object> params) throws Throwable {
        int page = params.containsKey("pageNumber") ? Integer.valueOf(params.get("pageNumber").toString()) : 1;
        int size = params.containsKey("pageSize") ? Integer.valueOf(params.get("pageSize").toString()) : 10;
        return getList(params, page, size);
    }

    public BasePage<entity> getpage(Map<String, Object> params, int page, int size) throws Throwable {
        int count = getCount(params);
        List<entity> list = getDao().getList(params, page, size);
        int pages = (page < 0 || size < 0) ? 1 : count / size + (count % size == 0 ? 0 : 1);
        return new BasePage<entity>(list, page < 0 ? 1 : page, size < 0 ? count : size, count, pages);
    }

    public BasePage<entity> getpage(Map<String, Object> params) throws Throwable {
        int page = params.containsKey("pageNumber") ? Integer.valueOf(params.get("pageNumber").toString()) : 1;
        int size = params.containsKey("pageSize") ? Integer.valueOf(params.get("pageSize").toString()) : 10;
        return getpage(params, page, size);
    }

    public entity getByPk(ID id) throws Throwable {
        return getDao().get(id);
    }

    public entity save(entity obj) throws Throwable {
        return getDao().save(obj);
    }

    public void saveOrUpdate(entity obj) throws Throwable {
        getDao().saveOrUpeate(obj);
    }

    public void update(entity obj) throws Throwable {
        getDao().update(obj);
    }

    public void update(ID id, Map<String, Object> params) throws Throwable {
        entity e = getDao().get(id);
        e = fillObject(e, params);
        getDao().update(e);
    }

    public void delete(entity obj) throws Throwable {
        getDao().delete(obj);
    }

    public boolean delete(Map<String, Object> params) throws Throwable {
        return getDao().delete(params);
    }

    public boolean deleteLogic(Map<String, Object> params) throws Throwable {
        return getDao().deleteLogic(params);
    }

    public boolean deleteLogicById(Serializable id) throws Throwable {
        return getDao().deleteLogicById(id);
    }

    public int executeUpdate(String queryName, Map<String, Object> params, boolean isHql, boolean isFromat) throws Throwable {
        return getDao().executeUpdate(queryName, params, isHql, isFromat);
    }

    public int getCount(Map<String, Object> params) throws Throwable {
        return Integer.valueOf(getDao().uniqueResult("count", true, params).toString());
    }

}
