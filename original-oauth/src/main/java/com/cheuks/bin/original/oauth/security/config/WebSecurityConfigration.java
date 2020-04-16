package com.cheuks.bin.original.oauth.security.config;

import com.cheuks.bin.original.cache.redis.JedisClusterFactory;
import com.cheuks.bin.original.cache.redis.JedisStandAloneFactory;
import com.cheuks.bin.original.common.cache.redis.RedisFactory;
import com.cheuks.bin.original.common.util.web.ResultFactory;
import com.cheuks.bin.original.oauth.security.OauthAccessDecisionManager;
import com.cheuks.bin.original.oauth.security.OauthAuthenticationProvider;
import com.cheuks.bin.original.oauth.security.OauthFilterInvocationSecurityMetadataSource;
import com.cheuks.bin.original.oauth.security.filter.LoginFilter;
import com.cheuks.bin.original.oauth.security.filter.OauthBasicAuthenticationFilter;
import com.cheuks.bin.original.oauth.security.token.TokenManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter
@Setter
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class WebSecurityConfigration extends WebSecurityConfigurerAdapter {

    @Value("${com.cheuks.bin.original.oauth.login.url:/user/login}")
    private String loginUrl;

    @Value("${com.cheuks.bin.original.oauth.login.page.url:/user/loginPage}")
    private String loginPageUrl;

    @Bean
    @Primary
    @ConditionalOnClass(value = {ResultFactory.class})
    public ResultFactory getResultFactory() {
        return new ResultFactory();
    }

    @Bean
    @Primary
    public OauthAccessDecisionManager getAccessDecisionManager() {
        return new OauthAccessDecisionManager();
    }

    @Bean
    @Primary
    public OauthFilterInvocationSecurityMetadataSource getFilterInvocationSecurityMetadataSource() {
        return new OauthFilterInvocationSecurityMetadataSource();
    }

    //	@Primary
    @Bean(initMethod = "init")
    @ConditionalOnMissingBean({TokenManager.class})
    TokenManager getTokenManager() {
        return new TokenManager();
    }

    @Bean
    @ConfigurationProperties(prefix = "redis.factory")
    @ConditionalOnMissingBean(value = {RedisFactory.class, JedisStandAloneFactory.class, JedisClusterFactory.class})
    RedisFactory getRedisFactory() {
//		return new JedisClusterFactory();
        return new JedisStandAloneFactory();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        getLoginPageUrl(),
                        "/*.ico"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        getFilterInvocationSecurityMetadataSource().append("/test/**", false, "ALL").append("/t/**", true, "ALL", "ANONYMOUS").append("/txxxxa/**/aax", false, "ALL", "ANONYMOUS").sort();

        http
                .addFilterBefore(new OauthBasicAuthenticationFilter(getTokenManager(), authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new LoginFilter(getLoginUrl(), authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(getAccessDecisionManager());
                        o.setSecurityMetadataSource(getFilterInvocationSecurityMetadataSource());
                        return o;
                    }
                })
//			.and()
//			.authorizeRequests()
//			.antMatchers("/test/**")
//			.permitAll()
////			.hasAnyRole("ROLE_GUEST")
//			.anyRequest()
//			.authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().loginPage(getLoginPageUrl())
                .loginProcessingUrl(getLoginUrl())
                .usernameParameter("userName")
                .passwordParameter("password")
                .permitAll()
                /*.failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest arg0, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        OutputStream writer = response.getOutputStream();
                        writer.write("登陆失败".getBytes("utf-8"));
                        writer.flush();
                        writer.close();
                    }
                }).successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse response, Authentication arg2) throws IOException, ServletException {
                        OutputStream writer = response.getOutputStream();
                        writer.write("登陆成功".getBytes());
                        writer.flush();
                        writer.close();
                    }
                })*/
                .and().logout().permitAll().and().csrf().disable().exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {

            @Override
            public void handle(HttpServletRequest arg0, HttpServletResponse response, AccessDeniedException arg2) throws IOException, ServletException {
//					OutputStream writer = response.getOutputStream();
//					writer.write("权限不足".getBytes());
//					writer.flush();
//					writer.close();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        })
        ;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new OauthAuthenticationProvider(getTokenManager().addIgnore(getLoginUrl())));
    }

}
