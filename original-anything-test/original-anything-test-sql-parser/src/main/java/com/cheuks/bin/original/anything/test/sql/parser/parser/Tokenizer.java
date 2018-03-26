package com.cheuks.bin.original.anything.test.sql.parser.parser;

import com.cheuks.bin.original.anything.test.sql.parser.constant.TokenType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tokenizer {

	private String content;
	private String keyword;
	private TokenType tokenType = TokenType.COMMONLY;
	private int startPosition;
	private int endPosition;
	private boolean isBracket;

	public Tokenizer(String content, String keyword, boolean isBracket, int endPosition) {
		super();
		this.content = content;
		this.keyword = keyword;
		this.startPosition = endPosition - content.length();
		this.endPosition = endPosition;
		this.isBracket = isBracket;
	}
}
