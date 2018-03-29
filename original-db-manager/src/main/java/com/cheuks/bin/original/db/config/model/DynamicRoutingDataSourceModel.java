package com.cheuks.bin.original.db.config.model;

import java.io.Serializable;
import java.util.List;

import com.cheuks.bin.original.db.config.ContentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DynamicRoutingDataSourceModel implements ContentType.DynamicRoutingDataSourceConfigType, ContentType.FilterModelConfigType, ContentType.DataSourceConfigType, Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String ref;
	private String clazz;

	private FilterModel handler;
	private List<DataSourceModel> datasources;

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class FilterModel implements Serializable {

		private static final long serialVersionUID = 1L;

		private String id;

		private String expression;

	}

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DataSourceModel implements Serializable {

		private static final long serialVersionUID = 1L;

		private String pattern;
		private String datasource;
	}

}
