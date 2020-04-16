package com.cheuks.bin.original.oauth.security;

import com.cheuks.bin.original.common.util.web.ResultFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OauthAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @SuppressWarnings("unused")
    private ResultFactory resultFactory;

    public OauthAuthenticationEntryPoint(ResultFactory resultFactory) {
        super();
        this.resultFactory = resultFactory;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.err.println("当访问权限验证失败是");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
