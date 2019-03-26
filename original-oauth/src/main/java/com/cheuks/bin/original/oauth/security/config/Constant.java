package com.cheuks.bin.original.oauth.security.config;

import com.cheuks.bin.original.common.util.SignUtil;
import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.oauth.model.Role;
import com.cheuks.bin.original.oauth.model.User;
import com.cheuks.bin.original.oauth.model.UserDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public interface Constant {

	String AUTHORIZATION = "Authorization";

	String ANONYMOUS = "ANONYMOUS";

	String LOGIN_USER_NAME = "userName";
	String LOGIN_PASSWORD = "password";
	String LOGIN_SOURCE = "source";

	UserDetail ANONYMOUS_USER_DETAIL = new UserDetail(
			new User().setRoles(
							new Role().appendGranted(ANONYMOUS)
						), SignUtil.generateNonceStr(10));
	
	/***
	 * 分割TOKEN
	 * 
	 * @param token
	 * @return
	 */
	default TokenInfo analysisToken(String token, String delimiter) {
		if (StringUtil.isEmpty(token))
			return null;
		String[] tokens = token.split(delimiter);
		if (tokens.length < 1)
			return null;
		return new TokenInfo(tokens[0], tokens[1]);
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TokenInfo {
		String type;
		String token;
	}

}
