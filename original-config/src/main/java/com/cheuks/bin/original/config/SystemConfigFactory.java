package com.cheuks.bin.original.config;

public interface SystemConfigFactory {

	String getConfig(String configName);

	void setConfig(String configName, String value) throws Throwable;

	String[] getConfig(String configName, String splitChar);

	Integer getConfigToInt(String configName);

	Boolean getConfigToBoolean(String configName);

	Long getConfigToLong(String configName);

	Float getConfigToFloat(String configName);

	Short getConfigToShort(String configName);

}
