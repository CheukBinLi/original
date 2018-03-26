package com.cheuks.bin.original.anything.test.sql.parser.parser;

import java.util.List;

import com.cheuks.bin.original.anything.test.sql.parser.constant.LayoutCharacters;

public interface SQLParser extends LayoutCharacters {

	SQLParser parser() throws Throwable;

	SQLParser parser(Tokenizer firstTokenizer) throws Throwable;

	List<Tokenizer> tokenizers();

}
