package com.cheuks.bin.original.session.sso;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 跨域头参数
 * 
 * @author Administrator
 *
 */
public class HeaderSessionFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (httpRequest.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(httpRequest.getMethod())) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.addHeader("Access-Control-Allow-Origin", "*");
			httpResponse.addHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,PUT,DELETE,HEAD");
			httpResponse.addHeader("Access-Control-Max-Age", "0");
			httpResponse.addHeader("Access-Control-Allow-Headers", "x-requested-with,x-auth-token,content-type");
			httpResponse.addHeader("Access-Control-Expose-Headers", "x-requested-with,x-auth-token,content-type");
			httpResponse.addHeader("Access-Control-Allow-Credentials", "true");
		} else {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {

	}

}
