package com.cheuks.bin.original.anything.test.sql.parser.parser.split;

import java.util.LinkedList;
import java.util.List;

import com.cheuks.bin.original.anything.test.sql.parser.TableInfoManager;
import com.cheuks.bin.original.anything.test.sql.parser.constant.DataBaseType;
import com.cheuks.bin.original.anything.test.sql.parser.parser.Lexer;
import com.cheuks.bin.original.anything.test.sql.parser.parser.SQLParser;
import com.cheuks.bin.original.anything.test.sql.parser.parser.Tokenizer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WhereParser implements SQLParser {

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
	 *  SELECT * FROM 
	 *  	tables[0]_shardingkey a 
	 *  		left join tables[1]_shardingkey b 
	 *  	ON a.id=b.pid 
	 *  		left join tables[2]_shardingkey c 
	 *  	ON b.id=c.pid
	 *  	WHERE 1=1
	 *  UNION
	 *   SELECT * FROM 
	 *  	tables[0]_shardingkey a 
	 *  		left join tables[1]_shardingkey b 
	 *  	ON a.id=b.pid 
	 *  		left join tables[2]_shardingkey c 
	 *  	ON b.id=c.pid
	 *  	WHERE 1=1
	 * </pre>
	 */

	private final Lexer lexer;

	private final DataBaseType dataBaseType;

	private List<Tokenizer> tokenizers = new LinkedList<Tokenizer>();

	private List<String> tables;
	private String shardingKey;
	/***
	 * 特征值参数位置
	 */
	private Integer shardingKeyIndex;
	private boolean isHaving;
	/***
	 * 获取分片表特征值
	 */
	private TableInfoManager tableInfoManager;

	public WhereParser parser() throws Throwable {
		return this;
	}

	@Override
	public List<Tokenizer> tokenizers() {
		// TODO Auto-generated method stub
		return this.tokenizers;
	}

	@Override
	public WhereParser parser(Tokenizer firstTokenizer) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
