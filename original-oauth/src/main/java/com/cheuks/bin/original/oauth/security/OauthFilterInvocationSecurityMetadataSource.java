package com.cheuks.bin.original.oauth.security;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class OauthFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//		List<ConfigAttribute> result = new LinkedList<ConfigAttribute>();
//		result.add(new OauthConfigAttribute("ROLE_GUEST"));
//		return result;
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

}
