package com.cheuks.bin.original.search;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.cheuks.bin.original.annotation.IndexField;
import com.cheuks.bin.original.reflect.Reflection;
import com.cheuks.bin.original.reflect.Reflection.FieldList;

public class ElasticSearchHelpper {

	public static enum ConditionType {
		IN, EQUALS, NOT_IN, NOT_EQUALS, LIKE, IS_NULL, NOT_NULL
	}

	public Client getClient() {
		try {
			// Builder builder = TransportClient.builder();
			// TransportClient client = builder.build();
			// client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9300)));
			// return client;

			TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// private String defaultIkName = "ik";
	// private String defaultIkName = "ik_max_word";

	/***
	 * 搜索生成器，方向有误，等修改
	 * 
	 * @param params
	 * @param withOutParams
	 * @return
	 */
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

	/***
	 * 建立索引
	 * 
	 * @param entity
	 *            模版对象
	 * @param shards
	 *            分块数量
	 * @return
	 * @throws Throwable
	 */
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
				if (null != index) {
					builder.field("store", index.store());
					builder.field("index", index.index());
					builder.field("search_analyzer", index.searchAnalyzer());
					builder.field("term_vector", index.termVector());
					builder.field("include_in_all", index.INCLUDE_IN_ALL_false);
					// if (list.getField().getType().equals(String.class)) {
					builder.field(index.analyzerFieldName(), index.analyzer());
					// builder.field("analyzer", getDefaultIkName());
					// builder.field("searchAnalyzer", getDefaultIkName());
					// }
				}
				builder.field("type", Reflection.newInstance().getElasticsearchMappingType(list.getField().getType()));
				builder.endObject();
			}
		}
	}

	/***
	 * 
	 * @param boolQueryBuilder
	 * @param conditionType
	 *            Note LIKE is to slow。
	 * @param name
	 * @param values
	 * @return
	 * @throws Throwable
	 */
	public QueryBuilder condition(final BoolQueryBuilder boolQueryBuilder, ConditionType conditionType, String name, Object... values) throws Throwable {
		switch (conditionType) {
		case IN: {
			boolQueryBuilder.must(QueryBuilders.termsQuery(name, values));
			break;
		}
		case EQUALS: {
			boolQueryBuilder.must(QueryBuilders.termsQuery(name, values));
			break;
		}
		case IS_NULL: {
			boolQueryBuilder.must(QueryBuilders.termQuery(name, null));
			break;
		}
		case NOT_NULL: {
			boolQueryBuilder.mustNot(QueryBuilders.termQuery(name, null));
			break;
		}
		case LIKE: {
			boolQueryBuilder.must(QueryBuilders.wildcardQuery(name, "*" + values[0].toString() + "*"));
			break;
		}
		case NOT_EQUALS: {
			boolQueryBuilder.mustNot(QueryBuilders.termsQuery(name, values));
			break;
		}
		case NOT_IN: {
			boolQueryBuilder.mustNot(QueryBuilders.termsQuery(name, values));
			break;
		}
		}
		return boolQueryBuilder;
	}
}
