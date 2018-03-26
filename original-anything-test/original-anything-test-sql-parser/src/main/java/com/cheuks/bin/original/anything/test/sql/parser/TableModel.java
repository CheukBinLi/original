package com.cheuks.bin.original.anything.test.sql.parser;

import java.io.Serializable;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TableModel implements Serializable {

	private static final long serialVersionUID = 4359209919203728235L;

	/***
	 * 表名
	 */
	private final String tableName;
	/***
	 * 分片键(主键ID)
	 */
	private String shardingKey;

	/***
	 * 字段
	 */
	private Set<String> columns;

	@Override
	public int hashCode() {
		return this.tableName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TableModel)
			return StringUtils.equals(this.tableName, ((TableModel) obj).tableName);
		throw new RuntimeErrorException(null, "the object can't convery TableModel type.");
	}

}
