package com.cheuks.bin.original.oauth.model;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable, UserDetails {

	private static final long serialVersionUID = -5367065879692865023L;

	public interface AccountStatus {
		int NORMAL = 0;
		int DISABLE = 1;//停用
		int LOCKED = 2;//锁定
	}

	private String userName;
	private String pwssword;
	private String type;
	private String tenant;
	private int status;
	private long expired;

	private transient Role roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.pwssword;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.status == 10;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.status == AccountStatus.LOCKED;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.expired < 1 || System.currentTimeMillis() < this.expired;
	}

	@Override
	public boolean isEnabled() {
		return this.status == AccountStatus.NORMAL;
	}

	public String getUserName() {
		return userName;
	}

	public User setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public User setPwssword(String pwssword) {
		this.pwssword = pwssword;
		return this;
	}

	public User setType(String type) {
		this.type = type;
		return this;
	}

	public User setTenant(String tenant) {
		this.tenant = tenant;
		return this;
	}

	public User setStatus(int status) {
		this.status = status;
		return this;
	}

	public User setExpired(long expired) {
		this.expired = expired;
		return this;
	}

	public User setRoles(Role roles) {
		this.roles = roles;
		return this;
	}

	public User(String pwssword, String userName) {
		super();
		this.pwssword = pwssword;
		this.userName = userName;
	}

}
