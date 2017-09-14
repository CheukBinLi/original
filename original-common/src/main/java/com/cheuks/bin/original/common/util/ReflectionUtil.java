package com.cheuks.bin.original.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

@SuppressWarnings("unchecked")
public class ReflectionUtil {

    private static ReflectionUtil INSTANCE;

    protected final Set<Class<?>> WrapperClass = new HashSet<Class<?>>(Arrays.asList(String.class, Integer.class, Boolean.class, Character.class, Short.class, Long.class, Float.class, Byte.class));

    protected final List<?> CollectionType = Arrays.asList(Collection.class, List.class, Set.class, Integer.class, int.class, Long.class, long.class, Float.class, float.class, Byte.class, byte.class, Character.class, char.class, String.class, Boolean.class, boolean.class, Double.class, double.class, Short.class, short.class);

    public static final ReflectionUtil instance() {
        if (null == INSTANCE) {
            synchronized (ReflectionUtil.class) {
                if (null == INSTANCE) {
                    INSTANCE = new ReflectionUtil();
                }
            }
        }
        return INSTANCE;
    }

    /***
     * 验证是否是封装类
     * 
     * @param type
     * @return
     */
    public boolean isWrapperClass(Class<?> type) {
        return WrapperClass.contains(type);
    }

    /***
     * 
     * @param clazz
     * @param isAccessible
     * @param hasSetting 校验是否存在 etting方法
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public Map<String, Field> scanClassField4Map(Class<?> clazz, boolean isAccessible, boolean hasSetting, boolean hasAliasAnnotation) throws NoSuchFieldException, SecurityException {
        if (null == clazz)
            return null;

        LinkedList<Class<?>> classes = new LinkedList<Class<?>>();
        classes.add(clazz);
        Class<?> tempClass;
        Alias alias;// 别名
        Class<?> currentClass = clazz;
        List<Field> fields = new ArrayList<Field>();
        List<Method> methods = new ArrayList<Method>();

        // 向上遍历父类
        while (true) {
            if (null == (tempClass = clazz.getSuperclass()) || tempClass == currentClass)
                break;
            classes.addLast(currentClass = tempClass);
        }
        for (int i = 0, len = classes.size(); i < len; i++) {
            tempClass = classes.removeLast();
            fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            if (hasSetting) {
                methods.addAll(Arrays.asList(tempClass.getDeclaredMethods()));
            }
        }

        Map<String, Field> result = new LinkedHashMap<String, Field>();
        Set<String> settingMethodName = null;
        settingMethodName = new HashSet<String>();
        for (Method m : methods) {
            if (m.getName().startsWith("set")) {
                settingMethodName.add(m.getName().substring(3).toLowerCase());
            }
        }
        for (Field f : fields) {
            if (Modifier.isTransient(f.getModifiers()) || Modifier.isStatic(f.getModifiers()))
                continue;
            if (!hasSetting || settingMethodName.contains(f.getName().toLowerCase())) {
                if (isAccessible)
                    f.setAccessible(true);
                if (hasAliasAnnotation) {
                    alias = f.getAnnotation(Alias.class);
                    if (null != alias && alias.value().length() > 0) {
                        result.put(alias.value(), f);
                    }
                }
                result.put(f.getName(), f);
            }
        }
        return result;
    }

    public List<Field> scanClassField4List(Class<?> clazz, boolean isAccessible, boolean hasSetting) throws NoSuchFieldException, SecurityException {
        if (null == clazz)
            return null;

        LinkedList<Class<?>> classes = new LinkedList<Class<?>>();
        classes.add(clazz);
        Class<?> tempClass;
        Class<?> currentClass = clazz;
        List<Field> fields = new ArrayList<Field>();
        List<Method> methods = new ArrayList<Method>();

        // 向上遍历父类
        while (true) {
            if (null == (tempClass = clazz.getSuperclass()) || tempClass == currentClass)
                break;
            classes.addLast(currentClass = tempClass);
        }
        for (int i = 0, len = classes.size(); i < len; i++) {
            tempClass = classes.removeLast();
            fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            if (hasSetting) {
                methods.addAll(Arrays.asList(tempClass.getDeclaredMethods()));
            }
        }

        List<Field> result = new ArrayList<Field>();
        Set<String> settingMethodName = null;
        if (hasSetting) {
            settingMethodName = new HashSet<String>();
            for (Method m : methods) {
                if (m.getName().startsWith("set")) {
                    settingMethodName.add(m.getName().substring(3).toLowerCase());
                }
            }
        }
        for (Field f : fields) {
            if (Modifier.isTransient(f.getModifiers()) || Modifier.isStatic(f.getModifiers()))
                continue;
            if (!hasSetting || settingMethodName.contains(f.getName().toLowerCase())) {
                if (isAccessible)
                    f.setAccessible(true);
                result.add(f);
            }
        }
        return result;
    }

    public List<Field> searchCollection(Field field, boolean isAccessible) throws NoSuchFieldException, SecurityException {
        List<Class<?>> interfaces = Arrays.asList(field.getType().getInterfaces());
        List<Field> result = null;
        if (!interfaces.containsAll(CollectionType)) {
            ParameterizedType type;
            try {
                type = (ParameterizedType) field.getGenericType();
            } catch (Exception e) {
                return result;
            }
            result = new ArrayList<Field>();
            for (Type t : type.getActualTypeArguments())
                result.addAll(scanClassField4List((Class<?>) t, isAccessible, true));
        }
        return result;
    }
}
