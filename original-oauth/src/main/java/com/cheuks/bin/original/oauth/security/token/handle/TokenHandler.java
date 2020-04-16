package com.cheuks.bin.original.oauth.security.token.handle;

import com.cheuks.bin.original.common.util.design.factory.Handler;
import com.cheuks.bin.original.oauth.model.AuthInfo;
import com.cheuks.bin.original.oauth.model.User;
import com.cheuks.bin.original.oauth.model.UserDetail;
import com.cheuks.bin.original.oauth.security.OauthAuthenticationToken;
import com.cheuks.bin.original.oauth.security.config.Constant;
import com.cheuks.bin.original.oauth.security.token.FilterChain;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TokenHandler extends Handler<String>, Constant {

    /***
     * handler处理次序
     *
     * @return
     */
    default int getOrder() {
        return 0;
    }

    /***
     * 加密密匙
     * @return
     */
    String getSecret();

    String encode(AuthInfo authInfo) throws Throwable;

    AuthInfo decode(String token) throws Throwable;

    /***
     * 刷新单点信息
     * @param token
     * @return
     * @throws Throwable
     */
    String refreshToken(String token) throws Throwable;

    String refreshToken(AuthInfo authInfo) throws Throwable;

    /***
     * 信息转换
     * @param authInfo
     * @return
     * @throws Throwable
     */
    User getUser(AuthInfo authInfo) throws Throwable;

    User getUserDetail(String token) throws Throwable;

    /***
     * 单点校验
     * @param authInfo
     * @return true:通过
     * @throws Throwable
     */
    boolean singleSignOnCheck(OauthAuthenticationToken authInfo) throws Throwable;

    /***
     * 登陆
     * @param user
     * @param pass
     * @param verificationCode 随便验证码
     * @return
     * @throws Throwable
     */
    UserDetail login(String user, String pass, String verificationCode) throws Throwable;

    /***
     * @param request
     * @param response
     * @param chain
     * @throws Throwable
     */
    void doLogout(HttpServletRequest request, HttpServletResponse response, String token, FilterChain chain) throws Throwable;

    /***
     * 登出处理
     * @param request
     * @param response
     * @throws Throwable
     */
    void logout(HttpServletRequest request, HttpServletResponse response, String token) throws Throwable;

    /***
     * 事件处理(轮询->support->execute())
     *
     * @param token
     * @param request
     * @param response
     * @return true:通过/false:中断
     */
    Authentication doAnalysis(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws Throwable;

    /***
     * token解析
     *
     * @param info
     * @param request
     * @param response
     * @return
     * @throws Throwable
     */
    Authentication analysisToken(TokenInfo info, HttpServletRequest request, HttpServletResponse response) throws Throwable;

    default void init() {
    }

}
