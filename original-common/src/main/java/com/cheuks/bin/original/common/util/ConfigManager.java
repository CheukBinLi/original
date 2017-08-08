package com.cheuks.bin.original.common.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 
 * @Title: original-common
 * @Description: 配置管理器
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月4日 下午2:28:21
 *
 */
public class ConfigManager extends ConverType implements Serializable {

    private static final long serialVersionUID = 5454496277766168461L;

    protected static final Map<String, Object> CONFIG = new ConcurrentHashMap<String, Object>();

    public Object getConfig(String key) throws Throwable {
        return CONFIG.get(key);
    }

    public ConfigManager add(Properties properties) {
        if (null != properties) {
            Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
            Entry<Object, Object> en;
            while (it.hasNext()) {
                en = it.next();
                CONFIG.put(en.getKey().toString(), en.getValue());
            }
        }
        return this;
    }

    public Object add(String key, Object value) throws Throwable {
        return CONFIG.put(key, value);
    }

    public Object remove(String key) {
        return CONFIG.remove(key);
    }

    public ConfigManager clean() {
        CONFIG.clear();
        return this;
    }

    public String[] getKeys() {
        return CONFIG.keySet().toArray(new String[0]);
    }

    public Object[] getValues() {
        return CONFIG.values().toArray(new Object[0]);
    }
}
