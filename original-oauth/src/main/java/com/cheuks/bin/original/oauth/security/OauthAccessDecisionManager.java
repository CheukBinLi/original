package com.cheuks.bin.original.oauth.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.cheuks.bin.original.common.util.conver.CollectionUtil;

public class OauthAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		System.err.println("decide");

		if (!CollectionUtil.isEmpty(configAttributes) && !CollectionUtil.isEmpty(authentication.getAuthorities())) {
			for (ConfigAttribute attribute : configAttributes) {
				for (GrantedAuthority auth : authentication.getAuthorities()) {
					if (null != attribute && attribute.getAttribute().equals(auth.getAuthority())) {
						return;
					}
				}
			}
		}
		throw new AccessDeniedException("no right");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		System.err.println(attribute.getAttribute());
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		System.err.println(clazz.getName());
		return true;
	}

}
