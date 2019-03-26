package com.cheuks.bin.original.oauth.security.token.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.cheuks.bin.original.common.util.conver.CollectionUtil;
import com.cheuks.bin.original.oauth.model.AuthInfo;
import com.cheuks.bin.original.oauth.model.User;
import com.cheuks.bin.original.oauth.model.UserDetail;
import com.cheuks.bin.original.oauth.security.OauthAuthenticationToken;

public class BaseTokenHandler extends AbstractTokenHandler implements TokenHandler {

	@Override
	public String getType() {
		return "BASE";
	}

	@Override
	public Authentication analysisToken(TokenInfo info, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		AuthInfo auth = decode(info.getToken());
//		if (!auth.getNonceStr().equals(checkOut(auth))) {
//			throw new NonceExpiredException("token is expired.");
//		}
//		return new OauthAuthenticationToken(new User().setUserName("base"), new AuthInfo());
		return new OauthAuthenticationToken(
				new UserDetail(
						new User()
							.setUserName("base")
							.setSource(auth.getSource()),
							auth.getNonceStr()
							),
				AuthInfo.EMPTY_AUTH_INFO);
	}

	@Override
	public UserDetail login(String user, String pass,String verificationCode) throws Throwable {
		AuthInfo info = new AuthInfo("-1", "-1", "BASE", verificationCode, CollectionUtil.setBuilder(false).append(ANONYMOUS).build());
		UserDetail userDetail = new UserDetail(getUser(info),info.getNonceStr());
		String token=encode(info);
		//记录随机码，只做单点校验:防止加密算法外露，权限角色被随意增加
		set(info, info.getNonceStr());
		userDetail.setOutput(CollectionUtil.mapBuilder().append("token", token).append("name", "哇哈哈").build());
		return userDetail;
	}

}
