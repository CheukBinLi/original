package com.cheuks.bin.original.anything.test.sql.parser;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableInfoManager {

	private static final Map<String, TableModel> TABLES = new ConcurrentSkipListMap<String, TableModel>();

	private static TableInfoManager INSTANCE;

	public static final TableInfoManager inatance() {
		if (null == INSTANCE) {
			synchronized (TableInfoManager.class) {
				if (null == INSTANCE) {
					return INSTANCE = new TableInfoManager();
				}
			}
		}
		return INSTANCE;
	}

	public TableModel geTableModel(String tableName) {
		return TABLES.get(tableName);
	}

	public void add(final TableModel tableModel) {
		TABLES.put(tableModel.getTableName(), tableModel);
	}
}
