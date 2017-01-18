package com.cheuks.bin.original.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class LoginAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger LOG = LoggerFactory.getLogger(LoginAuthenticationSuccessHandler.class);

	private String defaultTargetUrl = "/login";
	private boolean forwardToDestination = false;

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		System.out.println("LoginAuthenticationSuccessHandler");

		// if (null == defaultTargetUrl || defaultTargetUrl.length() < 1) {
		// LOG.error("have no configure defaultTargetUrl");
		// throw new BeanInitializationException("You must configure defaultTargetUrl");
		// }
		//
		// LOG.info("登录验证成功：{}", request.getContextPath() + defaultTargetUrl);
		//
		// if (forwardToDestination) {
		// LOG.info("Login success,Forwarding to " + this.defaultTargetUrl);
		// request.getRequestDispatcher(defaultTargetUrl).forward(request, response);
		// } else {
		// LOG.info("Login success,Redirecting to " + this.defaultTargetUrl);
		// response.sendRedirect(request.getContextPath() + defaultTargetUrl);
		// }
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
