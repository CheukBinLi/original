package com.cheuks.bin.original.search;

import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class ElasticSearchHelpper {

	public QueryBuilder createQueryBuilder(final Map<String, Object> params, String... withOutParams) {
		String key;
		Object value;
		String prefix;
		String suffix;
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for (Entry<String, Object> en : params.entrySet()) {
			if (null == (value = en.getValue()))
				continue;
			else if (!(key = en.getKey()).contains("_")) {
				boolQueryBuilder.must(QueryBuilders.termQuery(key, value));
			} else {
				prefix = key.substring(0, key.indexOf("_"));
				suffix = key.substring(key.indexOf("_") + 1);
				if ("in".equals(prefix)) {
					boolQueryBuilder.must(QueryBuilders.termsQuery(suffix, value));
				} else if ("like".equals(prefix)) {
					boolQueryBuilder.must(QueryBuilders.termsQuery(suffix, value));
				} else if ("le".equals(prefix)) {
				} else if ("ge".equals(prefix)) {
				} else if ("notin".equals(prefix)) {
				} else if ("not".equals(prefix)) {
				} else if ("isnull".equals(prefix)) {
				} else if ("notnull".equals(prefix)) {
				}
			}
		}
		return null;
	}

}
