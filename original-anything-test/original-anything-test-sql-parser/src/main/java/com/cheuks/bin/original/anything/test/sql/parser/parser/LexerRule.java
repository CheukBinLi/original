package com.cheuks.bin.original.anything.test.sql.parser.parser;

import com.cheuks.bin.original.anything.test.sql.parser.constant.LayoutCharacters;
import com.cheuks.bin.original.anything.test.sql.parser.parser.Lexer.EachItem;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LexerRule implements LayoutCharacters {
	private volatile int bracket;
	@Setter
	private EachItem eachItem = new EachItem() {

		@Override
		public boolean next(char[] fullText, char item, int current) {
			switch (item) {
			case '(':
				bracket++;
				return true;
			case ')':
				bracket--;
				return true;
			case ',':
				return true;
			case SPACE:
				return true;
			case EOI:
				return true;
			}

			char nextChar = charAt(fullText, current + 1);

			switch (nextChar) {
			case '(':
				bracket++;
				return true;
			case ')':
				bracket--;
				return true;
			case ',':
				return true;
			case SPACE:
				return true;
			case EOI:
				return true;
			}
			return false;
		}
	};

	public void rollback(Lexer lexer, Tokenizer tokenizer) {
		lexer.positionOffset(-1 * (tokenizer.getContent().length() + 1));
	}
}
