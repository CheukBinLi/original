package com.cheuks.bin.original.web.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class DaoAuthenticationProviderImpl extends DaoAuthenticationProvider {

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		System.out.println("additionalAuthenticationChecks");
		super.additionalAuthenticationChecks(userDetails, authentication);
	}

	@Override
	protected void doAfterPropertiesSet() throws Exception {
		System.out.println("doAfterPropertiesSet");
		super.doAfterPropertiesSet();
	}

	@Override
	public void setPasswordEncoder(Object passwordEncoder) {
		System.out.println("setPasswordEncoder");
		super.setPasswordEncoder(passwordEncoder);
	}

	@Override
	public void setSaltSource(SaltSource saltSource) {
		System.out.println("setSaltSource");
		super.setSaltSource(saltSource);
	}

	@Override
	protected SaltSource getSaltSource() {
		System.out.println("getSaltSource");
		return super.getSaltSource();
	}

	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		System.out.println("setUserDetailsService");
		super.setUserDetailsService(userDetailsService);
	}

	@Override
	protected UserDetailsService getUserDetailsService() {
		System.out.println("getUserDetailsService");
		return super.getUserDetailsService();
	}

}
