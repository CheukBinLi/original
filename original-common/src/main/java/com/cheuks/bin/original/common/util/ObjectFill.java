package com.cheuks.bin.original.common.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

public class ObjectFill {

    protected final static Map<Class<?>, Map<String, Field>> FIELDS = new ConcurrentHashMap<Class<?>, Map<String, Field>>();

    private volatile SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ObjectFill setDateFormat(String format) {
        dateFormat.applyPattern(format);
        return this;
    }

    public void scanClass(Class<?> c) {
        // FIELDS.put(c, fieldsConvertMap(c.getDeclaredFields()));
        try {
            FIELDS.put(c, ReflectionUtil.instance().scanClassField4Map(c, true, false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // public Map<String, Field> fieldsConvertMap(Field... o) {
    // Map<String, Field> map = new HashMap<String, Field>();
    // for (Field f : o) {
    // f.setAccessible(true);
    // map.put(f.getName(), f);
    // }
    // return map;
    // }

    public final <T> T fillObject(Class<T> t, Map<String, ?> data) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        return fillObject(t.newInstance(), data);
    }

    public final Map<String, Object> objectToMap(Object o) throws IllegalArgumentException, IllegalAccessException {
        return objectToMap(o, false);
    }

    public final Map<String, Object> objectToMap(Object o, boolean useAlias) throws IllegalArgumentException, IllegalAccessException {
        if (!FIELDS.containsKey(o.getClass()))
            scanClass(o.getClass());
        Map<String, Field> fields = FIELDS.get(o.getClass());
        Map<String, Object> result = new HashMap<String, Object>();
        Alias alias;
        String name = null;
        for (Entry<String, Field> en : fields.entrySet()) {
            if (useAlias) {
                alias = en.getValue().getAnnotation(Alias.class);
                if (null != alias) {
                    name = alias.value();
                    name = name.length() > 0 ? name : en.getKey();
                }
            } else {
                name = en.getKey();
            }
            result.put(name, en.getValue().get(o));
        }
        return result;
    }

    public final String objectToUrlParams(Object o, boolean useAlias) throws IllegalArgumentException, IllegalAccessException {
        if (!FIELDS.containsKey(o.getClass()))
            scanClass(o.getClass());
        Map<String, Field> fields = FIELDS.get(o.getClass());
        StringBuilder result = new StringBuilder();
        Alias alias;
        String name = null;
        for (Entry<String, Field> en : fields.entrySet()) {
            if (useAlias) {
                alias = en.getValue().getAnnotation(Alias.class);
                if (null != alias) {
                    name = alias.value();
                    name = name.length() > 0 ? name : en.getKey();
                }
            } else {
                name = en.getKey();
            }
            result.append("&").append(name).append("=").append(en.getValue().get(o));
        }
        return result.length() > 0 ? result.substring(1) : "";
    }

    public final <T> T fillObject(T t, Map<String, ?> data) throws IllegalArgumentException, IllegalAccessException {
        // System.out.println(t.getClass());
        Class<?> c = t.getClass();
        if (!FIELDS.containsKey(c))
            scanClass(c);
        Map<String, Field> fields = FIELDS.get(c);
        Object value;
        Field field;
        for (Entry<String, ?> en : data.entrySet()) {
            value = en.getValue();
            if (null == value || null == (field = fields.get(en.getKey())))
                continue;
            try {
                field.set(t, getValue(field.getType(), value));
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return t;
    }

    private Object getValue(Class<?> c, Object data) {
        String simpleName = c.getSimpleName();
        boolean isArray = data.getClass().isArray();
        try {
            if (c.isArray()) {
                System.err.println("数组末实现");
                return null;
            } else if (simpleName.equalsIgnoreCase("String"))
                return getFirstValue(isArray, data);
            else if (simpleName.equalsIgnoreCase("boolean") || simpleName.equalsIgnoreCase("Boolean"))
                return Boolean.valueOf(getFirstValue(isArray, data));
            else if (simpleName.equalsIgnoreCase("int") || simpleName.equalsIgnoreCase("Integer"))
                return Integer.valueOf(getFirstValue(isArray, data));
            else if (simpleName.equalsIgnoreCase("byte"))
                return Byte.valueOf(getFirstValue(isArray, data));
            else if (simpleName.equalsIgnoreCase("char") || simpleName.equalsIgnoreCase("Character"))
                return Character.valueOf(getFirstValue(isArray, data).charAt(0));
            else if (simpleName.equalsIgnoreCase("double"))
                return Double.valueOf(getFirstValue(isArray, data));
            else if (simpleName.equalsIgnoreCase("long"))
                return Long.valueOf(getFirstValue(isArray, data));
            else if (simpleName.equalsIgnoreCase("short"))
                return Short.valueOf(getFirstValue(isArray, data));
            else if (simpleName.equalsIgnoreCase("float"))
                return Float.valueOf(getFirstValue(isArray, data));
            else if (simpleName.equalsIgnoreCase("Date")) {
                return dateFormat.parse(data.toString());
            }
            return data;
        } catch (Exception e) {
            return null;
        }
    }

    private String getFirstValue(boolean isArray, Object data) {
        return isArray ? ((String[]) data)[0] : data.toString();
    }
}
