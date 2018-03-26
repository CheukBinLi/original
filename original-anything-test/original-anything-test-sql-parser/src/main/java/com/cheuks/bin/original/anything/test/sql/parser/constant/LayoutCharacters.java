package com.cheuks.bin.original.anything.test.sql.parser.constant;

/***
 * 
 * @Title: original-anything-test-sql-parser
 * @Description: 符号常量
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2018年1月11日 下午5:11:53
 *
 */
public interface LayoutCharacters {

	/**
	 * Tabulator character.TAB
	 */
	final byte TAB = 0x8;

	/**
	 * Line feed character.换行
	 */
	final byte LF = 0xA;

	/**
	 * Carriage return character.回车
	 */
	final byte CR = 0xD;

	/**
	 * End of input character. Used as a sentinel to denote the character one beyond the last defined character in a source file.
	 */
	final byte EOI = 0x1A;

	final byte SPACE = 0x20;

	final byte DOT = 0x2e;

}
