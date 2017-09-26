package com.cheuks.bin.original.common.rmi;

public interface RmiContant {

	String INIT_METHOD = "start";
	
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

	String BEAN_RMI_SERVICE_INIT = "rmiServiceInit";

	String BEAN_RMI_INVOKE_CLIENT = "rmiInvokeClient";
	/***
	 * NetworkServer
	 */
	String BEAN_RMI_NETWORK_SERVER = "rmiNetworkServer";
	/***
	 * NetworkClient
	 */
	String BEAN_RMI_NETWORK_CLIENT = "rmiNetworkClient";
	/***
	 * 类型加工对象
	 */
	String BEAN_GENERATE_RMI_BEAN_FACTORY = "generateRmiBeanFactory";
	/***
	 * 调用工厂
	 */
	String BEAN_RMI_BEAN_FACTORY = "rmiBeanFactory";
	/***
	 * 序列化对象
	 */
	String BEAN_CACHE_SERIALIZE = "cacheSerialize";

	/***
	 * loadBalanceFactory
	 */
	String BEAN_LOAD_BALANCE_FACTORY = "loadBalanceFactory";
	/***
	 * objectPoolManager
	 */
	String BEAN_OBJECT_POOL_MANAGER = "objectPoolManager";
	/***
	 * rmi:config
	 */
	String RMI_CONFIG_BEAN_CONFIG_GROUP = "rmiConfigGroup";
	String RMI_CONFIG_BEAN_CONFIG_GROUP_REGISTRY_MODEL = "registryModel";
	String RMI_CONFIG_BEAN_CONFIG_GROUP_PROTOCOL_MODEL = "protocolModel";
	String RMI_CONFIG_BEAN_CONFIG_GROUP_SCAN_MODEL = "scanModel";
	String RMI_CONFIG_BEAN_CONFIG_SERVICE_GROUP = "serviceGroup";
	String RMI_CONFIG_BEAN_CONFIG_REFERENCE_GROUP = "referenceGroup";
	/***
	 * rmi:service-group
	 */
	String RMI_CONFIG_BEAN_SERVICE_GROUP = "rmiServiceGroupConfig";
	/***
	 * rmi:reference-group
	 */
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
