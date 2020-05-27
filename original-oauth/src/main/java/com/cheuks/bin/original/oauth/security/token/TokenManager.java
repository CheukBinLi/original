package com.cheuks.bin.original.oauth.security.token;

import com.cheuks.bin.original.common.util.conver.CollectionUtil;
import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.oauth.model.AuthInfo;
import com.cheuks.bin.original.oauth.model.Role;
import com.cheuks.bin.original.oauth.model.User;
import com.cheuks.bin.original.oauth.model.UserDetail;
import com.cheuks.bin.original.oauth.security.OauthAuthenticationToken;
import com.cheuks.bin.original.oauth.security.config.Constant;
import com.cheuks.bin.original.oauth.security.exceition.IgnoreException;
import com.cheuks.bin.original.oauth.security.token.handle.TokenHandler;
import com.cheuks.bin.original.spring.plugin.util.design.factory.DefaultHandlerManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.Authentication;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Setter
@Getter
public class TokenManager extends DefaultHandlerManager<TokenHandler> implements Constant {

    /***
     * 分割符
     */
    protected String delimiter = ":";

    protected Set<String> ignore;

    protected LinkedList<TokenHandler> handlers = null;

    protected volatile TokenHandler head = null;

    protected TokenHandler handler = null;

    protected FilterChain filterChain;

    protected WebSecurity webSecurity;

    @Override
    public Class<TokenHandler> getSuperHandler() {
        return TokenHandler.class;
    }

    public TokenManager addIgnore(String... url) {
        if (null == url || url.length < 1)
            return this;
        if (null == ignore) {
            synchronized (this) {
                if (null == ignore)
                    ignore = new HashSet<String>();
            }
        }
        ignore.addAll(Arrays.asList(url));
        if (null != webSecurity)
            webSecurity.ignoring().antMatchers(url);
        return this;
    }

    public TokenManager removeIgnore(String... url) {
        if (null == url || url.length < 1)
            return this;
        if (null == ignore) {
            synchronized (this) {
                if (null == ignore)
                    ignore = new HashSet<String>();
            }
        }
        ignore.removeAll(Arrays.asList(url));
        return this;
    }

    /***
     *
     * @param tenant
     *            租户ID
     * @param source
     *            来源应用
     * @return
     */
	public static User buildGuestUserInfo(String tenant, String source) {
		return new User("0", "GUEST", ANONYMOUS, tenant, 0, Long.MAX_VALUE, source, GrantType.PASSWORD.name(), null, new Role().appendGranted(ANONYMOUS));
	}

    public static AuthInfo buildGuestAuthInfo(String tenant, String source) {
        return new AuthInfo().setId("0").setRole(CollectionUtil.setBuilder(false).append(ANONYMOUS).build());
    }

    User getUser(String token, String tenant, String source) throws Throwable {
        TokenInfo tokenInfo;
        if (StringUtil.isEmpty(token) || !token.contains(getDelimiter()) || null == (tokenInfo = analysisToken(token, getDelimiter())))
            return buildGuestUserInfo(tenant, source);
        return getHandler(tokenInfo.getType()).getUserDetail(token);
    }

    public boolean singleSignOnCheck(Authentication authentication) throws Throwable {
        if (!OauthAuthenticationToken.class.isAssignableFrom(authentication.getClass()))
            return false;
        OauthAuthenticationToken auth = (OauthAuthenticationToken) authentication;
        getHandler(auth.getUserDetail().getSource()).singleSignOnCheck(auth);
        return true;
    }

    public UserDetail Login(String user, String pass, String source, String verificationCode) throws Throwable {
        TokenHandler handler = StringUtil.isEmpty(source) ? getHandler(ANONYMOUS) : getHandler(source);
//        if (null == source)
//            throw new NullPointerException("the source can't be null.");
        if (null == handler) {
            throw new NullPointerException("can't found [" + source + "] handler.");
        }
        return handler.login(user, pass, verificationCode);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtil.isBlank(token)) {
            (null == filterChain ? filterChain = new FilterChain() : filterChain).setHandler(head).doLogout(request, response, token);
            return;
        }
        TokenInfo info = analysisToken(token, getDelimiter());
        getHandler(info.getType()).logout(request, response, info.getToken());
    }

    public Authentication analysisToken(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        if (ignore.contains(request.getServletPath()))
            throw IgnoreException.getDefaultIgnoreException();
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtil.isBlank(token)) {
            return (null == filterChain ? filterChain = new FilterChain() : filterChain).setHandler(head).doAnalysis(request, response);
        }
        TokenInfo info = analysisToken(token, getDelimiter());
        return getHandler(info.getType()).analysisToken(info, request, response);
    }

    protected HttpHeaders getHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, Collections.list(request.getHeaders(headerName)));
        }
        return headers;
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
        handlers = new LinkedList<TokenHandler>(POOL.values());
        handlers.sort(new Comparator<TokenHandler>() {
            @Override
            public int compare(TokenHandler o1, TokenHandler o2) {
                return o1.getOrder() > o2.getOrder() ? 0 : 1;
            }
        });
        if (CollectionUtil.isEmpty(handlers))
            return;
        TokenHandler temp = null;
        for (TokenHandler item : handlers) {
            if (null != temp)
                temp.setNext(item);
            temp = item;
        }
        this.head = handlers.get(0);
        this.head.setHead(true);

        temp.setNext(this.head);
        temp.setEnd(true);
    }

}
