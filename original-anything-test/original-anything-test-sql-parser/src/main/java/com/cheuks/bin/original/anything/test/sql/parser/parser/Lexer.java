package com.cheuks.bin.original.anything.test.sql.parser.parser;

import com.cheuks.bin.original.anything.test.sql.parser.constant.LayoutCharacters;

import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class Lexer implements LayoutCharacters {

	private final char[] text;
	private final char defaultSeparator = SPACE;
	private final int len;
	private char current;
	private Tokenizer currentToken;
	private int offset;
	/***
	 * 默认分词规则
	 */
	private LexerRule defaultlexerRule = new LexerRule();
	// private EachItem defaultEachItem = new EachItem() {
	// @Override
	// public boolean next(final char[] fullText, final char item, final int
	// current) {
	// if (EOI == Lexer.this.current || SPACE == Lexer.this.current) {
	// return true;
	// }
	// return false;
	// }
	// };

	public Tokenizer nextToken(final LexerRule lexerRule) {
		if (offset >= len)
			return null;
		StringBuilder sb = new StringBuilder(32);
		String result;
		LexerRule rule = null == lexerRule ? defaultlexerRule : lexerRule;
		// 符号数，前后不相等：即当前是符号
		final int originalBracket = rule.getBracket();
		for (;;) {
			if (EOI != (this.current = charAt(offset)))
				sb.append(this.current);
			if (rule.getEachItem().next(this.text, this.current, offset++)) {
				break;
			}
		}
		currentToken = new Tokenizer(result = sb.toString(), result.trim(), originalBracket != rule.getBracket(),
				offset);
		return currentToken;
	}

	public void positionOffset(int position) {
		int newPosition = this.offset + position;
		if (newPosition < 0)
			newPosition = 0;
		else if (newPosition >= len) {
			newPosition = len - 1;
		}
		offset = newPosition;
		this.current = text[offset];
	}

	public Tokenizer nextToken() {
		return nextToken(null);
	}

	/***
	 * 忽略过滤
	 * 
	 * @param dictionary
	 */
	public Tokenizer checkdeAndIgnoreNextToken(@NonNull String... dictionary) {
		synchronized (this) {
			Tokenizer result;
			int offset = this.offset;
			char current = this.current;
			Tokenizer nextToken = this.currentToken;
			result = nextToken();
			String content = result.getContent().trim();
			if (EOI != this.current)
				for (String item : dictionary) {
					if (item.equals(content)) {
						this.currentToken = nextToken;
						return result;
					}
				}
			this.current = current;
			this.offset = offset;
			return new Tokenizer("", "", true, this.offset);
		}
	}

	protected char charAt(int index) {
		if (index >= len) {
			return EOI;
		}
		return text[index];
	}

	public Lexer(@NonNull String text) {
		super();
		this.text = text.toCharArray();
		this.len = this.text.length;
		// this.len = text.length();
		this.current = this.len > 0 ? text.charAt(0) : (char) EOI;
	}

	public boolean isEnd() {
		if (offset >= len) {
			return true;
		}
		return false;
	}

	public boolean isComment() {
		char current = charAt(offset);
		char next = charAt(offset + 1);
		return '/' == current && '*' == next || '*' == current && '/' == next || '-' == current && '-' == next;
	}

	public boolean isVariable() {
		return false;
	}

	public char nextChar() {
		return current = charAt(++offset);
	}

	public void reset() {
		current = charAt(offset = 0);
	}

	public void reset(int index) {
		current = charAt(offset = index > 0 && index < len ? index : 0);
	}

	/***
	 * 
	 * @Title: original-anything-test-sql-parser
	 * @Description: 字符迭代接口
	 * @Company:
	 * @Email: 20796698@qq.com
	 * @author cheuk.bin.li
	 * @date 2018年1月22日 下午5:09:52
	 *
	 */
	public static abstract class EachItem {

		/***
		 * * 迭代字符
		 * 
		 * @param fullText
		 *            原字字符串
		 * @param item
		 *            当前字符
		 * @param current
		 *            当前字符位置
		 * @return 返回:true 跳出循环 || false 继续迭代
		 */
		public abstract boolean next(final char[] fullText, final char item, final int current);

		protected char charAt(final char[] fullText, final int index) {
			if (index >= fullText.length) {
				return EOI;
			}
			return fullText[index];
		}
	}
}
