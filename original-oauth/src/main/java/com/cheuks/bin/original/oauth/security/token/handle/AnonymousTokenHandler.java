package com.cheuks.bin.original.oauth.security.token.handle;

import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.oauth.model.AuthInfo;
import com.cheuks.bin.original.oauth.model.User;
import com.cheuks.bin.original.oauth.model.UserDetail;
import com.cheuks.bin.original.oauth.security.OauthAuthenticationToken;
import com.cheuks.bin.original.oauth.security.token.TokenManager;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnonymousTokenHandler extends AbstractTokenHandler implements TokenHandler {

    @Override
    public String getType() {
        return ANONYMOUS;
    }

    @Override
    public boolean isSupport(String t) {
        return StringUtil.isEmpty(t);
    }

    @Override
    public String getSecret() {
        return this.secret;
    }

    @Override
    public int getOrder() {
        return 9999;
    }

    @Override
    public String encode(AuthInfo authInfo) throws Throwable {
        // 游客authInfo
        return super.encode(authInfo);
    }

    @Override
    public AuthInfo decode(String token) throws Throwable {
        // token 90%为null
        return super.decode(token);
    }

    @Override
    public User getUser(AuthInfo authInfo) throws Throwable {
        return getUserDetail("");
    }

    @Override
    public User getUserDetail(String token) throws Throwable {
        return TokenManager.buildGuestUserInfo("0", getType());
    }

    @Override
    public Authentication analysisToken(TokenInfo info, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return new OauthAuthenticationToken(ANONYMOUS_USER_DETAIL, TokenManager.buildGuestAuthInfo("0", getType()));
    }

    @Override
    public UserDetail login(String user, String pass, String verificationCode) throws Throwable {
        return null;
    }

    @Override
    public boolean singleSignOnCheck(OauthAuthenticationToken authInfo) throws Throwable {
        return true;
    }

}
