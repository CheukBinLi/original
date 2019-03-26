package com.cheuks.bin.original.oauth.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.cheuks.bin.original.oauth.model.UserDetail;

public class OauthAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = -4093717898232314164L;

	public OauthAuthenticationToken(UserDetail principal, Object credentials) {
		super(principal, credentials);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<GrantedAuthority> getAuthorities() {
		UserDetail userDetail = (UserDetail) getPrincipal();
		if (null == userDetail)
			return null;
		return (Collection<GrantedAuthority>) userDetail.getAuthorities();
		// return super.getAuthorities();
	}

	
	public UserDetail getUserDetail() {
		return (UserDetail) getPrincipal();
	}
}
