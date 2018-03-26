package com.cheuks.bin.original.anything.test.sql.parser.parser.split;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cheuks.bin.original.anything.test.sql.parser.constant.DataBaseType;
import com.cheuks.bin.original.anything.test.sql.parser.constant.Keyword;
import com.cheuks.bin.original.anything.test.sql.parser.constant.TokenType;
import com.cheuks.bin.original.anything.test.sql.parser.parser.Lexer;
import com.cheuks.bin.original.anything.test.sql.parser.parser.LexerRule;
import com.cheuks.bin.original.anything.test.sql.parser.parser.SQLParser;
import com.cheuks.bin.original.anything.test.sql.parser.parser.SelectSQLParser;
import com.cheuks.bin.original.anything.test.sql.parser.parser.Tokenizer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FromParser implements SQLParser {

	/***
	 * <pre>
	 *  SELECT * FROM 
	 *  	order a 
	 *  		left join user b 
	 *  	ON a.id=b.pid 
	 *  		left join item c 
	 *  	ON b.id=c.pid
	 *  	WHERE 1=1;
	 * </pre>
	 * 
	 * <pre>
	 * FROM
	 * 	   	tables[0]_shardingkey a 
	 *  		left join tables[1]_shardingkey b 
	 *  	ON a.id=b.pid 
	 *  		left join tables[2]_shardingkey c 
	 *  	ON b.id=c.pid
	 * </pre>
	 * 
	 ***/

	private final Lexer lexer;

	/***
	 * 数据库类型
	 */
	private final DataBaseType dataBaseType;

	private LinkedList<Tokenizer> tokenizers = new LinkedList<Tokenizer>();
	private Tokenizer tokenizer;
	/***
	 * 相关表
	 */
	private List<String> tables;
	/***
	 * FROM %S A LEFT JOIN %S B ON A.ID=B.PID LEFT JOIN %S C ON B.ID=C.PID
	 */
	private String fromStringFormat;

	private LexerRule lexerRule = new LexerRule();

	/***
	 * 支持递归select
	 */
	public FromParser parser() throws Throwable {
		tokenizers.add(lexer.nextToken(lexerRule));
		boolean isBreak = false;
		// from 后 table /(select
		isBreak = true;

		do {
			tokenizer = lexer.nextToken(lexerRule);
			if (!StringUtils.isEmpty(tokenizer.getContent().trim())) {// 性能问题
				if (!"(".equals(tokenizer.getContent())) {
					tokenizer.getContent();
					tokenizer.setContent("#" + tokenizer.getContent());
					tokenizer.setTokenType(TokenType.TABLE);
					isBreak = false;
				}
				// ,
				// left/right/join

			}
			// previousToken = tokenizer;
			tokenizers.add(tokenizer);
		} while (isBreak);
		Tokenizer previousToken;
		while (true) {
			previousToken = tokenizer;
			tokenizer = lexer.nextToken(lexerRule);
			if (tokenizer == null)
				break;

			if (lexerRule.getBracket() < 0) {// 过滤
				lexer.reset(tokenizer.getStartPosition());
				break;
			}

			switch (tokenizer.getKeyword()) {
			case Keyword.SELECT:
				tokenizers.addAll(new SelectSQLParser(lexer, dataBaseType).parser(tokenizer).tokenizers());
				break;
			case Keyword.WHERE:
				tokenizers.addAll(new WhereParser(this.lexer, this.dataBaseType).parser(tokenizer).tokenizers());
				break;
			case Keyword.FROM:
				if (isBreak = lexerRule.getBracket() == 0)// 跳出
				{
					tokenizers.add(tokenizer);
				}
				break;
			case Keyword.JOIN:
				// next is table name / select
				break;
			case Keyword.LEFT:
				break;
			case Keyword.RIGHT:
				break;
			case Keyword.ON:
				break;
			case Keyword.AND:
				break;
			case Keyword.OR:
				break;
			default:
				tokenizers.add(tokenizer);
				break;
			}
			if (isBreak)
				break;
			System.err.println("FromParser:" + tokenizer.getContent());
		}
		return this;
	}

	@Override
	public List<Tokenizer> tokenizers() {
		return this.tokenizers;
	}

	@Override
	public SQLParser parser(Tokenizer firstTokenizer) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
