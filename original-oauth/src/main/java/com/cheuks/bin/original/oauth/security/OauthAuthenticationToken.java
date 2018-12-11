package com.cheuks.bin.original.oauth.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class OauthAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = -4093717898232314164L;

	public OauthAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}
	
}
