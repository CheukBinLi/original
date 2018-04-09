package com.cheuks.bin.original.prototype.spring.boot.unit.one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
public class UnitOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnitOneApplication.class, args);
	}
}
