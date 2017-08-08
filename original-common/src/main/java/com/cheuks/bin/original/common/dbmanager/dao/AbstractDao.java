package com.cheuks.bin.original.common.dbmanager.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.cheuks.bin.original.common.dbmanager.BaseEntity;
import com.cheuks.bin.original.common.dbmanager.DBAdapter;
import com.cheuks.bin.original.common.util.GeneratedIDService;

public abstract class AbstractDao<entity extends BaseEntity, ID extends Serializable> implements BaseDao<entity, ID> {

    public abstract Class<entity> getEntityClass();

    public abstract DBAdapter<?> getDBAdapter();

    public Long generateId() {
        return GeneratedIDService.newInstance().nextID();
    }

    public final String entityName = getEntityClass().getName();

    public final String createQueryName(String queryName) {
        return getDBAdapter().queryNameFormat(entityName, queryName);
    }

    public entity saveCustom(entity e) throws Throwable {
        return save(e);
    }

    public entity get(ID id) throws Throwable {
        return getDBAdapter().get(getEntityClass(), id);
    }

    public List<entity> getList(int page, int size) throws Throwable {
        return getDBAdapter().getList(getEntityClass(), page, size);
    }

    public List<entity> getList(Map<String, Object> params, int page, int size) throws Throwable {
        return getDBAdapter().getListByXqlQueryName(createQueryName("list"), true, true, params, page, size);
    }

    public int getCount(Map<String, Object> params) throws Throwable {
        Object o = getDBAdapter().uniqueResult(createQueryName("count"), true, true, params);
        return Integer.valueOf(o.toString());
    }

    public <T> List<T> getList(String queryName, Map<String, Object> params, boolean isFromat, int page, int size) throws Throwable {
        return getDBAdapter().getListByXqlQueryName(createQueryName(queryName), true, isFromat, params, page, size);
    }

    public List<entity> getListEntity(String queryName, Map<String, Object> params, boolean isFromat, int page, int size) throws Throwable {
        return getDBAdapter().getListByXqlQueryName(createQueryName(queryName), true, isFromat, params, page, size);
    }

    public entity save(entity o) throws Throwable {
        return getDBAdapter().save(o);
    }

    public int saveList(List<entity> list) throws Throwable {
        return getDBAdapter().saveList(list);
    }

    public void saveOrUpeate(entity o) throws Throwable {
        getDBAdapter().saveOrUpdate(o);
    }

    public void update(entity o) throws Throwable {
        getDBAdapter().update(o);
    }

    public void delete(entity o) throws Throwable {
        getDBAdapter().delete(o);
    }

    public boolean delete(Map<String, Object> params) throws Throwable {
        List<entity> list = getList(params, -1, -1);
        if (list.size() < 1 || list.size() > 1)
            return false;
        getDBAdapter().delete(list.get(0));
        return true;
    }

    public boolean deleteLogic(Map<String, Object> params) throws Throwable {
        List<entity> list = getList(params, -1, -1);
        if (list.size() < 1 || list.size() > 1)
            return false;
        getDBAdapter().update(list.get(0).setLogicStatus(BaseEntity.DELETE));
        return true;
    }

    public boolean deleteLogicById(Serializable id) throws Throwable {
        entity obj = getDBAdapter().get(getEntityClass(), id);
        if (null == obj)
            return false;
        getDBAdapter().update(obj.setLogicStatus(BaseEntity.DELETE));
        return true;
    }

    public Object uniqueResult(String queryName, boolean isFormat, Map<String, Object> params) throws Throwable {
        return getDBAdapter().uniqueResult(createQueryName(queryName), true, isFormat, params);
    }

    public int executeUpdate(String queryName, Map<String, Object> params, boolean isHql, boolean isFromat) throws Throwable {
        return getDBAdapter().executeUpdate(createQueryName(queryName), params, isHql, isFromat);
    }
}
