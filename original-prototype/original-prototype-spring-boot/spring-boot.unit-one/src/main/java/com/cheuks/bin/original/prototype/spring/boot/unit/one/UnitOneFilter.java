package com.cheuks.bin.original.prototype.spring.boot.unit.one;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class UnitOneFilter {

	@Bean
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new MyFilter());
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setName("unitOneFilter");
		return filterRegistrationBean;
	}

	public static class MyFilter implements Filter {

		@Override
		public void destroy() {

		}

		@Override
		public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
				throws IOException, ServletException {
			HttpServletRequest request = (HttpServletRequest) arg0;
			System.err.println("#################:" + request.getRequestURL());
			arg2.doFilter(arg0, arg1);
		}

		@Override
		public void init(FilterConfig arg0) throws ServletException {
			System.err.println("UnitOneFilter init");
		}

	}
}
