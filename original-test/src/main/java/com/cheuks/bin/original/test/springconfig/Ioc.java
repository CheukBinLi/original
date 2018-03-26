package com.cheuks.bin.original.test.springconfig;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ioc {

	@Value("${a}")
	private String a;
	@Value("${b}")
	private String b;
	@Value("${c}")
	private String c;

}
