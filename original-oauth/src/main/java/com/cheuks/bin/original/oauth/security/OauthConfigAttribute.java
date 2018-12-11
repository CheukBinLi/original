package com.cheuks.bin.original.oauth.security;

import org.springframework.security.access.ConfigAttribute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OauthConfigAttribute implements ConfigAttribute {

	private static final long serialVersionUID = -3952987215456121562L;

	private String attribute;

	@Override
	public String getAttribute() {
		return this.attribute;
	}

}
