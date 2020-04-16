package com.cheuks.bin.original.oauth.security;

import com.cheuks.bin.original.common.util.conver.CollectionUtil;
import com.cheuks.bin.original.common.util.conver.CollectionUtil.SetBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class AbstractFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private Map<String, Set<ConfigAttribute>> RESOURCES = new ConcurrentSkipListMap<String, Set<ConfigAttribute>>();
    private LinkedList<String> RESOURCES_NAME = new LinkedList<String>();
    private Map<String, Set<String>> RESOURCES_AUTH_GROUP = new ConcurrentSkipListMap<String, Set<String>>();


    public AbstractFilterInvocationSecurityMetadataSource append(String url, String... role) {
        SetBuilder builder = CollectionUtil.setBuilder(false);
        RESOURCES_NAME.add(url);
        Set<String> urls;
        for (String item : role) {
            builder.append(new OauthConfigAttribute(item));
            // 权限-地址表
            urls = RESOURCES_AUTH_GROUP.get(item);
            if (null == urls) {
                RESOURCES_AUTH_GROUP.put(item, urls = new HashSet<String>());
            }
            urls.add(url);
        }
        RESOURCES.put(url, builder.build());
        return this;
    }

    public AbstractFilterInvocationSecurityMetadataSource sort() {
        RESOURCES_NAME.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() < o2.length() ? 1 : o1.length() == o2.length() ? 0 : -1;
            }
        });
        return this;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        final String url = filterInvocation.getRequestUrl();
        // 分组管理
        // Authentication auth =
        // SecurityContextHolder.getContext().getAuthentication();
        // if (auth instanceof OauthAuthenticationToken) {
        // RESOURCES_AUTH_GROUP.get(((User)auth.getPrincipal()).getAuthorities())
        // }
        for (String item : RESOURCES_NAME) {
            if (antPathMatcher.match(item, url)) {
                System.err.println(item);
                return RESOURCES.get(item);
            }
        }
        throw new AccessDeniedException("not allow");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        List<ConfigAttribute> attributes = new LinkedList<>();
        attributes.add(new OauthConfigAttribute());
        return attributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
