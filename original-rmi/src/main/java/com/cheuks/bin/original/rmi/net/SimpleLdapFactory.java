package com.cheuks.bin.original.rmi.net;

import com.cheuks.bin.original.common.rmi.RmiContant;

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
public class SimpleLdapFactory implements RmiContant {

	private final static SimpleLdapFactory INSTANCE = new SimpleLdapFactory();

	public final static SimpleLdapFactory instance() {
		return INSTANCE;
	}

	public String getRootDirectory() {
		return LDAP_ROOT;
	}
	public String getRmiRootDirectory() {
		return LDAP_ROOT + RMI_LDAP_RMI_ROOT;
	}
	public String getServiceDirectory() {
		return LDAP_ROOT + RMI_LDAP_RMI_ROOT + RMI_LDAP_RMI_SERVICE;
	}

	public String getProviderDirectory(String applicationName) {
		return LDAP_ROOT + RMI_LDAP_RMI_ROOT + RMI_LDAP_RMI_PROVIDER + (null == applicationName ? "" : "/" + applicationName);
	}

	public String getConsumerDirectory(String applicationName) {
		return LDAP_ROOT + RMI_LDAP_RMI_ROOT + RMI_LDAP_RMI_CONSUMER + (null == applicationName ? "" : "/" + applicationName);
	}

	public String getLoadDirectory(String applicationName) {
		return LDAP_ROOT + RMI_LDAP_RMI_ROOT + RMI_LDAP_RMI_LOAD + (null == applicationName ? "" : "/" + applicationName);
	}

	public String getLedderDirectory(String applicationName) {
		return LDAP_ROOT + RMI_LDAP_RMI_ROOT + RMI_LDAP_RMI_LEDDER + (null == applicationName ? "" : "/" + applicationName);
	}

	public String getProviderDirectory() {
		return getProviderDirectory(null);
	}

	public String getConsumerDirectory() {
		return getConsumerDirectory(null);
	}

	public String getLoadDirectory() {
		return getLoadDirectory(null);
	}

	public String getLedderDirectory() {
		return getLedderDirectory(null);
	}

	/***
	 * 拼接目录
	 * 
	 * @param directory
	 * @return
	 */
	public String joinPath(String... directory) {
		if (null == directory || directory.length < 1)
			return "";
		StringBuilder sb = new StringBuilder();
		if (!directory[0].startsWith("/")) {
			sb.append("/");
		}
		for (String path : directory) {
			sb.append(path).append("/");
		}
		return sb.substring(0, sb.length() - 1);
	}

}
