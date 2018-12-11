package com.cheuks.bin.original.oauth.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

public class OauthAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		System.err.println("decide");
		configAttributes.forEach(item -> {
			System.err.println(item.getAttribute());
		});
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
