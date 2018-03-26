package com.cheuks.bin.original.anything.test.sql.parser.parser;

import java.util.LinkedList;
import java.util.List;

import com.cheuks.bin.original.anything.test.sql.parser.constant.DataBaseType;
import com.cheuks.bin.original.anything.test.sql.parser.constant.Keyword;
import com.cheuks.bin.original.anything.test.sql.parser.parser.mysql.SelectLexer;
import com.cheuks.bin.original.anything.test.sql.parser.parser.split.ColumnsParser;
import com.cheuks.bin.original.anything.test.sql.parser.parser.split.FromParser;
import com.cheuks.bin.original.anything.test.sql.parser.parser.split.WhereParser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SelectSQLParser implements SQLParser {

	private ColumnsParser columnsParser;
	private FromParser fromParser;
	private WhereParser whereParser;
	private Tokenizer tokenizer;
	private final Lexer lexer;
	private final DataBaseType dataBaseType;
	private LexerRule lexerRule = new LexerRule();

	private List<Tokenizer> tokenizers = new LinkedList<Tokenizer>();

	String testSql = "select * from a";

	public SelectSQLParser parser() throws Throwable {
		if (lexer.getLen() < 1) {
			throw new RuntimeException("SQL is empty.");
		}
		while (true) {
			if (null == (tokenizer = lexer.nextToken(lexerRule))) {
				break;
			}

			tokenizers.add(tokenizer);
			// System.out.println(tokenizer.getContent());
			switch (tokenizer.getKeyword()) {
			case Keyword.SELECT:
				tokenizers.add(lexer.checkdeAndIgnoreNextToken(Keyword.DISTINCT));
				tokenizers.addAll((columnsParser = new ColumnsParser(lexer, dataBaseType).parser()).tokenizers());
			case Keyword.FROM:
				tokenizers.addAll((fromParser = new FromParser(this.lexer, this.dataBaseType).parser()).tokenizers());
				continue;
			case Keyword.WHERE:
				tokenizers.addAll((whereParser = new WhereParser(this.lexer, this.dataBaseType).parser()).tokenizers());
				continue;
			case Keyword.HAVING:
				throw new RuntimeException("this version cant't support [having].");
			}
		}
		return this;
	}

	@Override
	public SQLParser parser(Tokenizer firstTokenizer) throws Throwable {
		if (null == firstTokenizer)
			return this;
		tokenizers().clear();
		tokenizers().add(firstTokenizer);
		// while (true) {
		// if (null == (tokenizer = lexer.nextToken())) {
		// break;
		// }
		// tokenizers.add(tokenizer);

		// 重复
		tokenizers.add(lexer.checkdeAndIgnoreNextToken(Keyword.DISTINCT));
		// 分解字段位全读
		tokenizers.addAll((columnsParser = new ColumnsParser(lexer, dataBaseType).parser()).tokenizers());
		// FROM位置
		tokenizers.addAll((fromParser = new FromParser(this.lexer, this.dataBaseType).parser()).tokenizers());

		tokenizers.addAll((whereParser = new WhereParser(this.lexer, this.dataBaseType).parser()).tokenizers());
		return this;
	}

	public static void main(String[] args) throws Throwable {
		SelectSQLParser selectSQLParser = new SelectSQLParser(new SelectLexer(
				"select (select a from sub_1) as '哇哈哈',(select b from sub_2) as 'function' from a".toUpperCase()),
				DataBaseType.MYSQL);
		selectSQLParser.parser();
		for (Tokenizer tokenizer : selectSQLParser.tokenizers()) {
			System.out.print(tokenizer.getContent());
		}
		// for (char c : "select (select a from x1) from a".toUpperCase().toCharArray())
		// {
		// System.out.println(c);
		// }
	}

	@Override
	public List<Tokenizer> tokenizers() {
		return this.tokenizers;
	}

}
