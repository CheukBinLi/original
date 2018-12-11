package com.cheuks.bin.original.oauth.model;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Granted implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	private String name;
	private String url;

	@Override
	public String getAuthority() {
		return this.url;
	}

	public Granted setName(String name) {
		this.name = name;
		return this;
	}

	public Granted setUrl(String url) {
		this.url = url;
		return this;
	}

}
