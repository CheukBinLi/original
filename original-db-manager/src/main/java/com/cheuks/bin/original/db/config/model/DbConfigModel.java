package com.cheuks.bin.original.db.config.model;

import com.cheuks.bin.original.db.config.ContentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DbConfigModel implements ContentType.DbConfigType {

	private HibernateSessionFactoryModel hibernateSessionFactoryConfig;

	private DynamicRoutingDataSourceModel dynamicRoutingDataSourceConfig;

	private TransactionManagerModel transactionManagerConfig;
}
