package com.cheuks.bin.spring.boot.data.jpa;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@Configurable
/***
 * 打包war必须继承SpringBootServletInitializer
 * 
 * @author BIN
 *
 */
public class JpaApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(JpaApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(JpaApplication.class, args);
	}

}
