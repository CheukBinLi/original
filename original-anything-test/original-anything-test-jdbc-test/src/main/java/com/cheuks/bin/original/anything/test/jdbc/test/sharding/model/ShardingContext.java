package com.cheuks.bin.original.anything.test.jdbc.test.sharding.model;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShardingContext {

	private Map<String, DataSource> dataSources = new ConcurrentSkipListMap<String, DataSource>();

	private Map<String, TableRule> tableRules = new ConcurrentSkipListMap<String, TableRule>();
	// private Map<String, DataSource> dataSources;
	//
	// private Map<String, TableRule> tableRules;

}
