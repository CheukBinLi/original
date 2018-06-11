package com.cheuks.bin.original.common.util;

import java.lang.reflect.Field;
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
@SuppressWarnings("rawtypes")
public class ReflectionCache {

	protected static final Map<String, Map<String, Field>> FIELD_CACHE = new ConcurrentSkipListMap<String, Map<String, Field>>();

	protected static final Map<String, List<Field>> FIELD_LIST_CACHE = new ConcurrentSkipListMap<String, List<Field>>();

	private static ReflectionCache INSTANCE;

	protected ReflectionCache() {
	}

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

	/***
	 * 获取字段集<br>
	 * 注意：字段如果有@Alias 注解，会根据注解内容，增加多一个字段别名。
	 * 
	 * @param c
	 * @param hasSetting
	 *            是否过滤只存在有 setXX 方法的字段
	 * @param hasAliasAnnotation
	 *            是否扫描@Alias注销
	 * @param keepBoth
	 *            Alias如果存在，是否保留原字段和别名字段多个对象
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public Map<String, Field> getFields(Class<?> c, boolean hasSetting, boolean hasAliasAnnotation, boolean keepBoth, Class... ignore) throws NoSuchFieldException, SecurityException {
		Map<String, Field> fields = FIELD_CACHE.get(c.getName());
		if (null == fields) {
			synchronized (this) {
				if (null == fields) {
					fields = ReflectionUtil.instance().scanClassField4Map(c, true, hasSetting, hasAliasAnnotation, keepBoth, ignore);
					FIELD_CACHE.put(c.getName(), fields);
				}
			}
		}
		return fields;
	}

	public Map<String, Field> getFields(Class<?> c, boolean hasSetting, boolean hasAliasAnnotation, Class... ignore) throws NoSuchFieldException, SecurityException {
		return getFields(c, hasSetting, hasAliasAnnotation, true, ignore);
	}

	/***
	 * * 获取字段集<br>
	 * 
	 * @param c
	 * @param hasSetting
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public List<Field> getFields4List(Class<?> c, boolean hasSetting, Class... ignore) throws NoSuchFieldException, SecurityException {
		List<Field> fields = FIELD_LIST_CACHE.get(c.getName());
		if (null == fields) {
			synchronized (this) {
				if (null == fields) {
					fields = ReflectionUtil.instance().scanClassField4List(c, true, hasSetting, ignore);
					FIELD_LIST_CACHE.put(c.getName(), fields);
				}
			}
		}
		return fields;
	}

	/***
	 * 
	 * 获取字段集<br>
	 * 注意：字段如果有@Alias 注解，会根据注解内容，增加多一个字段别名。
	 * 
	 * @param c
	 * @param field
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public Field getFieldByMap(Class<?> c, String field, Class... ignore) throws NoSuchFieldException, SecurityException {
		return getFields(c, true, true, ignore).get(field);
	}

}