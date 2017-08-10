package com.cheuks.bin.original.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/***
 * 
 * @Title: original-common
 * @Description: 反射缓存
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日 下午3:02:18
 *
 */
public class ReflectionCache {

    protected static final Map<String, Map<String, Field>> FIELD_CACHE = new ConcurrentSkipListMap<String, Map<String, Field>>();

    private static ReflectionCache INSTANCE;

    protected ReflectionCache() {}

    public static ReflectionCache newInstance() {
        if (null == INSTANCE) {
            synchronized (ReflectionCache.class) {
                if (null == INSTANCE) {
                    INSTANCE = new ReflectionCache();
                }
            }
        }
        return INSTANCE;
    }

    public Map<String, Map<String, Field>> getFieldCache() {
        return FIELD_CACHE;
    }

    public Map<String, Field> getFields(Class<?> c) throws NoSuchFieldException, SecurityException {
        Map<String, Field> fields = FIELD_CACHE.get(c.getName());
        if (null == fields) {
            synchronized (this) {
                if (null == fields) {
                    fields = ReflectionUtil.instance().scanClassField4Map(c, true, false, true);
                    FIELD_CACHE.put(c.getName(), fields);
                }
            }
        }
        return fields;
    }

    public List<Field> getFields4List(Class<?> c, boolean hasSetting, boolean hasAliasAnnotation) throws NoSuchFieldException, SecurityException {
        Map<String, Field> fields = FIELD_CACHE.get(c.getName());
        if (null == fields) {
            synchronized (this) {
                if (null == fields) {
                    fields = ReflectionUtil.instance().scanClassField4Map(c, true, hasSetting, hasAliasAnnotation);
                    FIELD_CACHE.put(c.getName(), fields);
                }
            }
        }
        return new ArrayList<Field>(fields.values());
    }

    public Field getField(Class<?> c, String field) throws NoSuchFieldException, SecurityException {
        return getFields(c).get(field);
    }

}
