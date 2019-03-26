package com.cheuks.bin.original.oauth.security.token;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

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

@Setter
@Getter
public class TokenManager extends DefaultHandlerManager<TokenHandler> implements Constant {

	/***
	 * 分割符
	 */
	protected String delimiter = ":";

	protected Set<String> ignore;

	protected LinkedList<TokenHandler> handers = null;

	protected FilterChain filterChain;

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
		return new User("0", "GUEST", ANONYMOUS, tenant, 0, Long.MAX_VALUE, source, new Role().appendGranted(ANONYMOUS));
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
	
	public boolean singleSignOnCheck(Authentication authentication) throws Throwable{
		if (!OauthAuthenticationToken.class.isAssignableFrom(authentication.getClass()))
			return false;
		OauthAuthenticationToken auth = (OauthAuthenticationToken) authentication;
		getHandler(auth.getUserDetail().getSource()).singleSignOnCheck(auth);
		return true;
	}

	public UserDetail Login(String user, String pass, String source, String verificationCode) throws Throwable {
		if (null == source)
			throw new NullPointerException("the source can't be null.");
		TokenHandler handler = getHandler(source);
		if (null == handler) {
			throw new NullPointerException("can't found [" + source + "] handler.");
		}
		return handler.login(user, pass, verificationCode);
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		String token = request.getHeader(AUTHORIZATION);
		if (StringUtil.isBlank(token)) {
			(null == filterChain ? filterChain = new FilterChain() : filterChain).setHandlers(handers).doLogout(request, response, token);
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
			return (null == filterChain ? filterChain = new FilterChain() : filterChain).setHandlers(handers).doAnalysis(request, response);
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
		handers = new LinkedList<TokenHandler>(POOL.values());
		handers.sort(new Comparator<TokenHandler>() {
			@Override
			public int compare(TokenHandler o1, TokenHandler o2) {
				return o1.getOrder() > o2.getOrder() ? 0 : 1;
			}
		});
	}

}
