package com.cheuks.bin.original.oauth.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.cheuks.bin.original.common.util.conver.JsonMapper;
import com.cheuks.bin.original.common.util.web.ResultFactory;
import com.cheuks.bin.original.oauth.security.config.Constant;
import com.cheuks.bin.original.oauth.security.exceition.IgnoreException;
import com.cheuks.bin.original.oauth.security.token.TokenManager;
import com.cheuks.bin.original.oauth.util.ExceptionPrinterUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class OauthBasicAuthenticationFilter extends BasicAuthenticationFilter implements Constant {

	TokenManager tokenManager;

	@Autowired(required = false)
	ResultFactory resultFactory;

	public ResultFactory getResultFactory() {
		if (null == resultFactory) {
			synchronized (this) {
				resultFactory = new ResultFactory();
			}
		}
		return resultFactory;
	}

	public OauthBasicAuthenticationFilter setResultFactory(ResultFactory resultFactory) {
		this.resultFactory = resultFactory;
		return this;
	}

	public OauthBasicAuthenticationFilter(TokenManager tokenManager, AuthenticationManager authenticationManager) {
		super(authenticationManager);
		this.tokenManager = tokenManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

		Authentication auth;
		try {
			try {
				auth = getTokenManager().analysisToken(request, response);
				if (null == auth || (ANONYMOUS_USER_DETAIL != auth.getPrincipal() && !getTokenManager().singleSignOnCheck(auth))) {
					return;
				}
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (IgnoreException e) {
				//忽略放行
			}
			// 放行
			chain.doFilter(request, response);
		} catch (Throwable e) {
			try {
				ExceptionPrinterUtil.instance().writeString(response, JsonMapper.newInstance(true).writer(getResultFactory().create(e), null, true, false, true), null);
			} catch (Exception e1) {
				log.error(e.getMessage(), e);
			}
			return;
		}
	}

}
