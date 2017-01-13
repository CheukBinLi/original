package com.cheuks.bin.original.web.security;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceImpl implements UserDetailsService {

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// org.springframework.security.util.FilterChainProxy
		UserDetails details = null;
		// try {
		// // 用户名,密码,是否激活,accountnonexpired如果帐户没有过期设置为true
		// // credentialsnonexpired如果证书没有过期设置为true
		// // accountnonlocked如果帐户不锁定设置为true
		// com.aoyu.user.entity.User u = this.getUser(username);
		//
		// // 目前是把角色给写死了
		// details = new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), u.isEnabled(), u.isAccountNonExpired(), u.isCredentialsNonExpired(), u.isAccountNonLocked(), AuthorityUtils.createAuthorityList("ROLE_USER"));
		//
		// } catch (UsernameNotFoundException usernameNotFoundException) {
		// usernameNotFoundException.printStackTrace();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return details;
		return null;
	}

}
