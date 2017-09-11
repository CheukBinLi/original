package com.cheuks.bin.original.common.rmi;

public interface RmiContant {

	/***
	 * 私有栈头标签
	 */
	int RMI_SERVICE_TYPE_HEADER_TAG = 0xFAFAFA;
	/***
	 * 连接完成发送负载标记
	 */
	int RMI_SERVICE_TYPE_LOAD = 0xAAABC;
	/***
	 * RMI请求类型
	 */
	int RMI_SERVICE_TYPE_REQUEST = 0xAAAAA;
	/***
	 * RMI回应类型
	 */
	int RMI_SERVICE_TYPE_RESPONSE = 0xAAAAB;
	/***
	 * RMI连接测试类型
	 */
	int RMI_SERVICE_TYPE_HEAR_BEAT = 0XAAAC;

	String RMI_CONFIG_ELEMENT_REGISTRY = "registry";
	String RMI_CONFIG_ELEMENT_SCAN = "scan";
	String RMI_CONFIG_ELEMENT_PROTOCOL = "protocol";
	String RMI_CONFIG_ELEMENT_SERVICE = "service";
	String RMI_CONFIG_ELEMENT_REFERENCE = "reference";
	String RMI_CONFIG_ATTRIBUTE_APPLICATION_NAME = "applicationName";

	String RMI_CONFIG_BEAN_SERVICE_GROUP = "rmiServiceGroupConfig";
	String RMI_CONFIG_BEAN_REFERENCE_GROUP = "rmiReferenceGroupConfig";
	String RMI_CONFIG_BEAN_REGISTRY = "rmiRegistryConfig";
	String RMI_CONFIG_BEAN_SCAN = "rmiscanConfig";
	String RMI_CONFIG_BEAN_PROTOCOL = "rmiProtocolConfig";

	String LDAP_ROOT = "/original";
	String RMI_LDAP_RMI_ROOT = "/rmi";
	String RMI_LDAP_RMI_SERVICE = "/service";
	String RMI_LDAP_RMI_PROVIDER = "/provider";
	String RMI_LDAP_RMI_CONSUMER = "/consumer";
	String RMI_LDAP_RMI_LOAD = "/load";
	String RMI_LDAP_RMI_LEDDER = "/ledder";
}
