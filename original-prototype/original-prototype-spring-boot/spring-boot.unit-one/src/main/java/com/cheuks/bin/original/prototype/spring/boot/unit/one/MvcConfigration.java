package com.cheuks.bin.original.prototype.spring.boot.unit.one;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 * 
 * @author BIN
 * @see 是用来全局定制 化 Spring Boot 的 MVC 特性
 */
@Component
@Configurable
public class MvcConfigration implements WebMvcConfigurer {

	@Override
	/***
	 * 日期格式化
	 */
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
		WebMvcConfigurer.super.addFormatters(registry);
	}

	@Override
	/***
	 * 拦截器
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
				System.out.println(request.getRequestURL());
				return HandlerInterceptor.super.preHandle(request, response, handler);
			}

		});
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	@Override
	/***
	 * 跨域
	 */
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
		WebMvcConfigurer.super.addCorsMappings(registry);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/file/**").addResourceLocations("file:g:/psp/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}

}
