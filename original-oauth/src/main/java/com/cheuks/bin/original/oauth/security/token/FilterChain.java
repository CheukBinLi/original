package com.cheuks.bin.original.oauth.security.token;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.cheuks.bin.original.oauth.security.token.handle.TokenHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
// @Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterChain {

	private List<TokenHandler> handlers;
	private int doFilterIndex = 0;
	private int doLououtIndex = 0;

	public Authentication doAnalysis(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		if (null == handlers || doFilterIndex >= handlers.size())
			return null;
		return handlers.get(doFilterIndex++).doAnalysis(request, response, this);
	}
	
	public void doLogout(HttpServletRequest request, HttpServletResponse response, String token) throws Throwable {
		if (null == handlers || doLououtIndex >= handlers.size())
			return;
		handlers.get(doLououtIndex++).doLogout(request, response, token, this);
	}

	public FilterChain setHandlers(List<TokenHandler> handlers) {
		this.handlers = handlers;
		doFilterIndex = 0;
		doLououtIndex = 0;
		return this;
	}

}
