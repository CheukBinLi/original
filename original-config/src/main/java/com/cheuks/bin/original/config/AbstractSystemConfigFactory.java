package com.cheuks.bin.original.config;

import java.util.Map;
import java.util.StringTokenizer;

public abstract class AbstractSystemConfigFactory implements SystemConfigFactory {

	public abstract Map<String, Object> getConfig();

	public String getConfig(String configName) {
		return getConfig(configName);
	}

	public String[] getConfig(String configName, String delimiter) {
		String value = getConfig(configName);
		String[] result = null;
		if (null != value) {
			StringTokenizer tokenizer = new StringTokenizer(value, delimiter);
			result = new String[tokenizer.countTokens()];
			int count = 0;
			while (tokenizer.hasMoreTokens())
				result[count++] = tokenizer.nextToken();
		}

		return result;
	}

	public Integer getConfigToInt(String configName) {
		String value = getConfig(configName);
		return null == value ? null : Integer.valueOf(value);
	}

	public Boolean getConfigToBoolean(String configName) {
		String value = getConfig(configName);
		return null == value ? null : Boolean.valueOf(value);
	}

	public Long getConfigToLong(String configName) {
		String value = getConfig(configName);
		return null == value ? null : Long.valueOf(value);
	}

	public Float getConfigToFloat(String configName) {
		String value = getConfig(configName);
		return null == value ? null : Float.valueOf(value);
	}

	public Short getConfigToShort(String configName) {
		String value = getConfig(configName);
		return null == value ? null : Short.valueOf(value);
	}

}
