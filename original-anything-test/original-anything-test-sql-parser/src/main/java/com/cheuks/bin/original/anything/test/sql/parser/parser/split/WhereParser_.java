package com.cheuks.bin.original.anything.test.sql.parser.parser.split;

import java.io.Serializable;
import java.util.List;

import com.cheuks.bin.original.anything.test.sql.parser.parser.Lexer;
import com.cheuks.bin.original.anything.test.sql.parser.parser.SQLParser;
import com.cheuks.bin.original.anything.test.sql.parser.parser.Tokenizer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class WhereParser_ implements Serializable, SQLParser {

	private static final long serialVersionUID = -4659291206817198888L;

	private String params;

	private boolean hasShardingKey;

	private String shardingKey;

	private boolean hasGroupUpBy;

	private List<String> groupUpBy;

	private boolean hasLimit;

	private boolean hasSubSelect;

	private boolean hasHaving;

	private Lexer lexer;

	public WhereParser_ parser() throws Throwable {
		return this;
	}

	@Override
	public List<Tokenizer> tokenizers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLParser parser(Tokenizer firstTokenizer) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
