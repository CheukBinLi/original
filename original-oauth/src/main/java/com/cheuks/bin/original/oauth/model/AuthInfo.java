package com.cheuks.bin.original.oauth.model;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthInfo implements Serializable {

	private static final long serialVersionUID = -4616317180995430675L;

	private String id;//用户ID
	private String type;//来源应用:toke类型
	private String userName;//用户名
	private String deviceCode;//设备码
	private Set<String> role;//权限角色

	public AuthInfo setId(String id) {
		this.id = id;
		return this;
	}

	public AuthInfo setType(String type) {
		this.type = type;
		return this;
	}

	public AuthInfo setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public AuthInfo setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
		return this;
	}

	public AuthInfo setRole(Set<String> role) {
		this.role = role;
		return this;
	}

}
