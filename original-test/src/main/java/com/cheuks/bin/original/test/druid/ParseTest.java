package com.cheuks.bin.original.test.druid;

import java.util.List;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcUtils;

public class ParseTest {

	public static void main(String[] args) {
		String sql = "select m,(select x from b where b.x=a.id) from aa where a='100';";
		SQLStatementParser sqlStatementParser = SQLParserUtils.createSQLStatementParser(sql, JdbcUtils.MYSQL);
		List<SQLStatement> list = sqlStatementParser.parseStatementList();

		for (SQLStatement each : list) {

			SQLSelect sqlselect = ((SQLSelectStatement) each).getSelect();
			System.err.println(sqlselect + " || ");
			SQLSelectQueryBlock selectQueryBlock = (SQLSelectQueryBlock) sqlselect.getQuery();
			System.err.println(selectQueryBlock.getFrom());
			System.err.println(selectQueryBlock.getWhere());

			List<SQLSelectItem> xxxx = selectQueryBlock.getSelectList();
			for (SQLSelectItem item : xxxx) {
				System.out.println(item.toString()+"  m ba  ");
			}

//			System.err.println(selectQueryBlock.get);
			// System.err.println(each.toString() + " || ");

		}
	}

}
