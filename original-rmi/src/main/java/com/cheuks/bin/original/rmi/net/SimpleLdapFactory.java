package com.cheuks.bin.original.rmi.net;

import com.cheuks.bin.original.common.rmi.RmiContent;

/***
 * 
 * @Title: original-rmi
 * @Description: 服务目录
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年5月1日 上午9:03:25
 *
 */
public class SimpleLdapFactory implements RmiContent {

	private final static SimpleLdapFactory INSTANCE = new SimpleLdapFactory();

	public final static SimpleLdapFactory instance() {
		return INSTANCE;
	}

	public String getServersDirectory() {
		return RMI_LDAP_ROOT + RMI_LDAP_SERVERS;
	}

	public String getProviderDirectory(String applicationName) {
		return RMI_LDAP_ROOT + null == applicationName ? (applicationName + "/") : "" + RMI_LDAP_PROVIDER;
	}

	public String getConsumerDirectory(String applicationName) {
		return RMI_LDAP_ROOT + null == applicationName ? (applicationName + "/") : "" + RMI_LDAP_CONSUMER;
	}

	public String getLoadDirectory(String applicationName) {
		return RMI_LDAP_ROOT + null == applicationName ? (applicationName + "/") : "" + RMI_LDAP_LOAD;
	}

	public String getLedderDirectory(String applicationName) {
		return RMI_LDAP_ROOT + null == applicationName ? (applicationName + "/") : "" + RMI_LDAP_LEDDER;
	}

	public String getProviderDirectory() {
		return getProviderDirectory(null);
	}

	public String getConsumerDirectory() {
		return getConsumerDirectory(null);
	}
	//
	//	public String getLoadDirectory() {
	//		return getLoadDirectory(null);
	//	}
	//
	//	public String getLedderDirectory() {
	//		return getLedderDirectory(null);
	//	}

}
