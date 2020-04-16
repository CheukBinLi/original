package com.cheuks.bin.original.oauth.security.filter;

import com.cheuks.bin.original.common.util.conver.JsonMapper;
import com.cheuks.bin.original.common.util.conver.ObjectFill;
import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.common.util.web.ResultFactory;
import com.cheuks.bin.original.oauth.model.User;
import com.cheuks.bin.original.oauth.model.UserDetail;
import com.cheuks.bin.original.oauth.security.OauthAuthenticationToken;
import com.cheuks.bin.original.oauth.util.ExceptionPrinterUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    @Autowired(required = false)
    ObjectMapper objectMapper;

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

    public LoginFilter setResultFactory(ResultFactory resultFactory) {
        this.resultFactory = resultFactory;
        return this;
    }

    public ObjectMapper getObjectMapper() {
        if (null == objectMapper) {
            synchronized (this) {
                objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            }
        }
        return objectMapper;
    }

    public LoginFilter setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public LoginFilter(String loginUrl, AuthenticationManager authenticationManager) {
        super();
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(loginUrl, "POST"));
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user;
            String contentType;
            if ((contentType = StringUtil.isEmpty(request.getContentType(), StringUtil.EMPTY)).contains("json") || contentType.contains("JSON")) {
                user = getObjectMapper().readValue(request.getInputStream(), User.class);
            } else {
                user = new User();
                ObjectFill.fillObject(user, request.getParameterMap());
            }
            if (StringUtil.isEmpty(user.getUserName()))
                throw new UsernameNotFoundException("userName can't be null");
            if (StringUtil.isEmpty(user.getPassword()))
                throw new UsernameNotFoundException("password can't be null");
            if (StringUtil.isEmpty(user.getSource()))
                throw new UsernameNotFoundException("source can't be null");
            return authenticationManager.authenticate(new OauthAuthenticationToken(new UserDetail(user, "123"), response));
        } catch (Exception e) {
            getResultFactory().create(e);
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try {
            UserDetail detail = (UserDetail) authResult.getPrincipal();
            ExceptionPrinterUtil.instance().writeString(response, JsonMapper.newInstance(true).writer(detail.getOutput(), null, false, true, true, true, detail.getAddition()), null);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.err.println("login unsuccess.");
        failed.printStackTrace();
        try {
            ExceptionPrinterUtil.instance().writeString(response, JsonMapper.newInstance(true).writer(getResultFactory().create(failed), null, true, false, true), null);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
