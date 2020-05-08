package com.cheuks.bin.original.oauth.web;

import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.oauth.model.AuthResource;
import com.cheuks.bin.original.oauth.security.OauthFilterInvocationSecurityMetadataSource;
import com.cheuks.bin.original.oauth.security.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("unused")
@Configuration
public class Init {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private OauthFilterInvocationSecurityMetadataSource oauthFilterInvocationSecurityMetadataSource;

    @Autowired
    private TokenManager tokenManager;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @PostConstruct
    private void init() {
        //忽略设置
        tokenManager.addIgnore("/test/next");

        //权限分配
        List<AuthResource> resources = new ArrayList<AuthResource>();
        resources.add(new AuthResource().setUrl("/1test/"));
        Map<RequestMappingInfo, HandlerMethod> map = this.handlerMapping.getHandlerMethods();
        Set<String> parentUrl = new HashSet<String>();
        Iterator<Entry<RequestMappingInfo, HandlerMethod>> iterator = map.entrySet().iterator();
        String auth;
        LinkedList<String> auths = new LinkedList<String>();
        while (iterator.hasNext()) {
            Map.Entry<RequestMappingInfo, HandlerMethod> entry = iterator.next();
            // 数据库过滤出权限:正则
            // urls.addAll(entry.getKey().getPatternsCondition().getPatterns());

            for (String url : entry.getKey().getPatternsCondition().getPatterns()) {
                parentUrl.add(StringUtil.getPaths(url, "/", true).getPath());
            }

            for (String url : entry.getKey().getPatternsCondition().getPatterns()) {
                auths.clear();
                for (AuthResource item : resources) {
                    url.equals(item.getUrl());
                    // System.err.println(antPathMatcher.match(item.getUrl(),
                    // url) + ":" + item.getUrl() + ":" + url);
                    if (url.equals(item.getUrl())) {
                        auths.add(item.getName());
                    }
                }
                oauthFilterInvocationSecurityMetadataSource.append(url, false, (auths.isEmpty() ? new String[]{TokenManager.ANONYMOUS} : auths.toArray(new String[0])));
                // antPathMatcher.match(item.getUrl(), url);
            }
            // oauthFilterInvocationSecurityMetadataSource.append(entry.getKey().getPatternsCondition().getPatterns(),
            // role)
            // System.out.println(entry.getKey().getName() + "\n" +
            // entry.getKey() + "\n" + entry.getValue());
        }
        for (String item : parentUrl) {
            System.out.println(item);
            oauthFilterInvocationSecurityMetadataSource.append(item, true, TokenManager.ANONYMOUS);
        }
        // System.err.println(Arrays.toString(urls.toArray()));


        System.err.println();
        // oauthFilterInvocationSecurityMetadataSource.append(url, role)
    }

}
