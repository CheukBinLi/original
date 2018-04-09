package com.cheuks.bin.spring.boot.data.jpa.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;

@Configurable
@Component
public class DbConfigration {

	@Value("${db.user}")
	String user;
	@Value("${db.password}")
	String password;
	@Value("${db.url}")
	String url;
	@Value("${db.driverClassName}")
	String driver;

	@Bean
	public DataSource dataSource() {
		// HikariDataSource dataSource = new HikariDataSource();
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		return dataSource;
	}

}
