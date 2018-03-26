package com.cheuks.bin.original.anything.test.sql.parser.parser.split;

import java.util.LinkedList;
import java.util.List;

import com.cheuks.bin.original.anything.test.sql.parser.constant.DataBaseType;
import com.cheuks.bin.original.anything.test.sql.parser.constant.Keyword;
import com.cheuks.bin.original.anything.test.sql.parser.parser.Lexer;
import com.cheuks.bin.original.anything.test.sql.parser.parser.LexerRule;
import com.cheuks.bin.original.anything.test.sql.parser.parser.SQLParser;
import com.cheuks.bin.original.anything.test.sql.parser.parser.SelectSQLParser;
import com.cheuks.bin.original.anything.test.sql.parser.parser.Tokenizer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ColumnsParser implements SQLParser {

	private final Lexer lexer;
	private final DataBaseType dataBaseType;
	private Tokenizer tokenizer;
	private List<Tokenizer> tokenizers = new LinkedList<Tokenizer>();
	private LexerRule lexerRule = new LexerRule();

	public ColumnsParser parser() throws Throwable {

		boolean isBreak = false;
		while (true) {
			tokenizer = lexer.nextToken(lexerRule);
			if (tokenizer == null)
				break;
			System.err.println("columnsParser:" + tokenizer.getContent());
			switch (tokenizer.getKeyword()) {
			case Keyword.SELECT:
				// tokenizers.add(tokenizer);
				tokenizers.addAll(new SelectSQLParser(lexer, dataBaseType).parser(tokenizer).tokenizers());
				break;
			case Keyword.FROM:
				if (isBreak = lexerRule.getBracket() == 0)// 跳出
				{
					// lexerRule.rollback(lexer, tokenizer);
					lexer.reset(tokenizer.getStartPosition());
				}
				break;
			default:
				tokenizers.add(tokenizer);
				break;
			}
			if (isBreak)
				break;
		}
		return this;
	}

	@Override
	public List<Tokenizer> tokenizers() {
		// TODO Auto-generated method stub
		return this.tokenizers;
	}

	@Override
	public SQLParser parser(Tokenizer firstTokenizer) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
