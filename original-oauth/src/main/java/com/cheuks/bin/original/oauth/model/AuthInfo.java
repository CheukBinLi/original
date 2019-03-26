package com.cheuks.bin.original.oauth.model;

import java.io.Serializable;
import java.util.Set;

import com.cheuks.bin.original.common.util.SignUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthInfo implements Serializable {

	private static final long serialVersionUID = -4616317180995430675L;
	
	public static final AuthInfo EMPTY_AUTH_INFO = new AuthInfo();

	private String id;// 用户ID
	private String tenant;// 租户ID
	private String source;// 来源应用例如:token type
	private final String nonceStr;// 随机
	private Set<String> role;// 权限角色

	public AuthInfo setId(String id) {
		this.id = id;
		return this;
	}

	public AuthInfo setSource(String source) {
		this.source = source;
		return this;
	}

	public AuthInfo setRole(Set<String> role) {
		this.role = role;
		return this;
	}

	public AuthInfo setTenant(String tenant) {
		this.tenant = tenant;
		return this;
	}

	public AuthInfo() {
		this(10);
	}

	public AuthInfo(int nonceStrLength) {
		this(SignUtil.generateNonceStr(10));
	}

}
