package com.cheuks.bin.original.search;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.client.transport.TransportClient.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.cheuks.bin.original.annotation.IndexField;
import com.cheuks.bin.original.reflect.Reflection;
import com.cheuks.bin.original.reflect.Reflection.FieldList;

public class ElasticSearchHelpper {

	public Client getClient() {
		try {
			Builder builder = TransportClient.builder();
			TransportClient client = builder.build();
			client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9300)));
			return client;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String defaultIkName = "ik";
	// private String defaultIkName = "ik_max_word";

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

	public XContentBuilder buildTemplate(Class<?> entity, int shards) throws Throwable {
		XContentBuilder builder = XContentFactory.jsonBuilder();
		List<FieldList> fieldList = Reflection.newInstance().getSettingFieldListList(entity, false);
		builder.startObject().startObject(entity.getSimpleName()).startObject("properties");
		fieldListNode(builder, fieldList);
		builder.endObject().endObject().endObject();
		return builder;
	}

	private void fieldListNode(final XContentBuilder builder, final List<FieldList> fieldList) throws IOException {
		for (FieldList list : fieldList) {
			if (!list.isBasicType()) {
				builder.startObject(list.getField().getName()).startObject("properties");
				// 遍历
				fieldListNode(builder, list.getSubFieldList());
				builder.endObject().endObject();
			} else {
				IndexField index = list.getField().getAnnotation(IndexField.class);
				builder.startObject(list.getField().getName());
				if (null == index) {
					builder.field("store", "yes");
					builder.field("index", "not_analyzed");
					if (list.getField().getType().equals(String.class)) {
						builder.field("analyzer", getDefaultIkName());
						// builder.field("searchAnalyzer", getDefaultIkName());
					}
				} else {
					builder.field("store", index.store());
					builder.field("index", index.index());
					if (list.getField().getType().equals(String.class)) {
						builder.field("analyzer", index.analyzer());
						// builder.field("searchAnalyzer", index.analyzer());
					}
				}
				builder.field("type", Reflection.newInstance().getPackageType(list.getField().getType()));
				builder.endObject();
			}
		}
	}

	public String getDefaultIkName() {
		return defaultIkName;
	}

	public ElasticSearchHelpper setDefaultIkName(String defaultIkName) {
		this.defaultIkName = defaultIkName;
		return this;
	}

}
