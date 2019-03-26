package com.cheuks.bin.original.oauth.security.token;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.cheuks.bin.original.common.util.conver.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Deprecated
public class TokenFactory {

	Set<TokenType> types = new CopyOnWriteArraySet<TokenType>();

	@SuppressWarnings("unlikely-arg-type")
	public boolean hasToken(String tokenType) {
		return types.contains(tokenType);
	}

	public String getTokenType(String token, String splitChar) {
		splitChar = StringUtil.isEmpty(splitChar, ":");
		if (StringUtil.isBlank(token) || !token.contains(splitChar))
			return null;
		return StringUtil.isEmpty(token.substring(0, token.indexOf(splitChar)), null);
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TokenType {
		private String type;
		private String name;

		@Override
		public int hashCode() {
			return this.type.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TokenType) {
				return this.type.equals(((TokenType) obj).type);
			} else if (obj instanceof String) {
				return this.type.equals(obj);
			}
			throw new RuntimeException("not support this type");
		}

	}

}
