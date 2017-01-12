package com.cheuks.bin.original.search;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Elasticsearch {

	public Client getClient() {
		try {
			// Builder builder = TransportClient.builder();
			// Settings settings = Settings.settingsBuilder().put("cluster.name", "yq-tTom").build();
			// TransportClient client = builder.settings(settings).build();
			// client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9200)));
			//
			// return client;

			TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9300)));

			return client;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void t1() {

		// TermsBuilder featuer = AggregationBuilders.terms("product_Feature_Name").field("productFeature.productFeatureName");
		// // TermsBuilder featuer = AggregationBuilders.terms("product_Feature_Name").field("productFeature.productFeatureId");
		//
		// TermsBuilder subFeature = AggregationBuilders.terms("sub_feature").field("productFeature.productFeatureCategoryId");
		//
		// featuer.subAggregation(subFeature);

		// 品牌
		TermsAggregationBuilder products = AggregationBuilders.terms("brand").field("productCategoryId");
		products.size(10000);
		// // 品牌数
		// TermsBuilder brand = AggregationBuilders.terms("category").field("productCategoryId");
		// products.subAggregation(brand);

		// 规格
		TermsAggregationBuilder feature = AggregationBuilders.terms("features").field("productFeature.productFeatureCategoryId");
		Script script = new Script("doc['productFeature.productFeatureId'].value");
		TermsAggregationBuilder sbuFeature = AggregationBuilders.terms("subList").field("productFeature.productFeatureId");
		feature.subAggregation(sbuFeature);

		// SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch("zurich_product").setTypes("product");
		SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch("zurich_product").setTypes("product").setSearchType(SearchType.DFS_QUERY_THEN_FETCH).addAggregation(products).addAggregation(feature);
		// SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch("zurich_product").setTypes("product").setSearchType(SearchType.COUNT).setQuery(new BoolQueryBuilder().must(QueryBuilders.termsQuery("productFeature.productFeatureCategoryId", "BIZ_FEATURE_FEATURE"))).addAggregation(feature);
		// SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch("zurich_product").setTypes("product").setSearchType(SearchType.COUNT).setQuery(new BoolQueryBuilder().must(QueryBuilders.termsQuery("productFeature.productFeatureCategoryId", "BIZ_FEATURE_COLOR", "BIZ_FEATURE_FEATURE", "BIZ_FEATURE_WEIGHT"))).addAggregation(feature);

		System.out.println(searchRequestBuilder.toString());

		SearchResponse response = searchRequestBuilder.execute().actionGet();

		System.out.println("回调:" + response.toString());

		System.out.println(response.getAggregations());

		// Object o = xxx(response.getAggregations());
		Object o = createNode(response.getAggregations(), "productFeatureId", "features", "subList");
		System.out.println(o);
		// StringTerms a = (StringTerms) response.getAggregations().get("features");
		// Iterator<Bucket> it = a.getBuckets().iterator();
		// Bucket tempBucket;
		// while (it.hasNext()) {
		// tempBucket = it.next();
		// StringTerms b = (StringTerms) tempBucket.getAggregations().get("subFeature");
		// Iterator<Bucket> itb = b.getBuckets().iterator();
		// Bucket tempBucket2;
		// System.err.println(tempBucket.getKey());
		// while (itb.hasNext()) {
		// System.err.println("---" + itb.next().getKey());
		// }
		// }

		// for (SearchHit hit1 : response.getHits()) {
		// Map<String, Object> source1 = hit1.getSource();
		// if (!source1.isEmpty()) {
		// for (Iterator<Map.Entry<String, Object>> it = source1.entrySet().iterator(); it.hasNext();) {
		// Map.Entry<String, Object> entry = it.next();
		// System.out.println(entry.getKey() + "=======" + entry.getValue());
		// }
		// }
		// }

	}

	public List<Map> xxx(Aggregations aggregations) {
		Map<String, Aggregation> map = aggregations.getAsMap();
		Iterator<Entry<String, Aggregation>> it = map.entrySet().iterator();
		Aggregation value;
		Entry<String, Aggregation> temp;
		List<Map> result = new ArrayList<Map>();
		List<Map> subList;
		Map node;
		Bucket bucket;
		Iterator<Bucket> buckets;
		while (it.hasNext()) {
			temp = it.next();
			node = new HashMap();
			node.put(temp.getKey(), temp.getKey());
			value = temp.getValue();
			if (null == value)
				node.put("subList", null);
			else {
				buckets = ((StringTerms) value).getBuckets().iterator();
				subList = new ArrayList<Map>();
				while (buckets.hasNext()) {
					subList.addAll(xxx(buckets.next().getAggregations()));
				}
				node.put("subList", subList);
			}
			result.add(node);
		}
		return result;
	}

	public List<Map> createNode(Aggregations aggregations, String idField, String aggregationName, String subNodeName) {
		// def List<Map> createNode(Aggregations aggregations, String idField, String aggregationName, String subNodeName) {
		//// Bucket bucket
		StringTerms stringTerms = (StringTerms) aggregations.get(aggregationName);
		if (null == stringTerms)
			return null;
		Iterator<Bucket> it = stringTerms.getBuckets().iterator();
		Bucket tempBucket;
		List<Map> result = new ArrayList<Map>();
		Map node;
		while (it.hasNext()) {
			tempBucket = it.next();
			node = new HashMap();
			node.put(idField, tempBucket.getKey());
			Aggregation subList = tempBucket.getAggregations().get(subNodeName);
			if (null != subList) {
				node.put("subList", createNode(tempBucket.getAggregations(), idField, subNodeName, subNodeName));
			}
			result.add(node);
		}
		return result;

	}

	public static void main(String[] args) throws Throwable {
		new Elasticsearch().create();
	}

	private Map listMapConvertToMap(List<Map> data, String fireIdField, String idFiead, String subListName) {
		Map<String, Map> result = new HashMap<String, Map>();
		Map temp;
		Object value = null;
		Iterator<Map> it = data.iterator();
		while (it.hasNext()) {
			temp = it.next();
			// List<Map>
			value = temp.get(subListName);
			if (null != value) {
				// 最后加
				temp.put(subListName, listMapConvertToMap((List<Map>) value, null, idFiead, subListName));
			}
			result.put(temp.get(null == fireIdField ? idFiead : fireIdField).toString(), temp);
		}
		return result;
	}

	public List<Map> createNode(Map<String, Map> allNode, List<Map> existsNode, String allNodeIdField, String existsNodeIdField, String subListName) {
		List<Map> result = new ArrayList<Map>();
		Iterator<Map> it = existsNode.iterator();
		Map next;
		Object subList;
		Map node;
		while (it.hasNext()) {
			next = it.next();
			node = new HashMap();
			node.putAll(allNode.get(next.get(existsNodeIdField).toString()));
			if (null == (subList = next.get(subListName))) {
				continue;
			} else {
				node.put(subListName, createNode(allNode.get(next.get(existsNodeIdField).toString()), (List<Map>) subList, allNodeIdField, existsNodeIdField, subListName));
			}
			result.add(node);
		}
		return result;
	}

	public List<Map> iteratorCollection(Map<String, Map> allNode, List<Map> existNode, String idField) {
		List<Map> result = new ArrayList<Map>();
		Iterator<Map> it = existNode.iterator();
		String id;
		Map node;
		while (it.hasNext()) {
			id = it.next().get(idField).toString();
			if (allNode.containsKey(id)) {
				node = new HashMap();
				it.remove();
				// 节点
				node.putAll(allNode.get(id));
				node.put("subList", iteratorCollection((Map<String, Map>) node.get("subList"), existNode, idField));
				result.add(node);
			}
		}
		return result;
	}

	@Test
	public void create() throws Throwable {

		XContentBuilder builder = new ElasticSearchHelpper().buildTemplate(EsModel.class, 5);
		System.out.println(builder.string());

		Client client = getClient();
		// client.prepareIndex("com", EsModel.class.getSimpleName()).execute().actionGet();

		PutMappingRequest mapping = Requests.putMappingRequest("com").type(EsModel.class.getSimpleName()).source(builder);

		try {
			client.admin().indices().prepareCreate("com").execute().actionGet();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		System.out.println(client.admin().indices().putMapping(mapping).actionGet());
	}

	@Test
	public void inster() throws JsonProcessingException {
		Client client = getClient();
		ObjectMapper mapper = new ObjectMapper();
		EsModel es = new EsModel();
		MMX wahaha = new MMX();
		List<MMX> list = new ArrayList<MMX>();
		list.add(wahaha);
		wahaha.setIdx(1).setNamex("哇哈哈").setRemarkx("好嗨森");
		es.setCount(11).setId(1).setName("叼嗱升").setSearchWord("小红帽").setWahaha(list);
		IndexRequestBuilder index = client.prepareIndex("com", "EsModel", "1");
		index.setSource(mapper.writeValueAsBytes(es));
		index.execute().actionGet();
	}

	@Test
	public void inster2() throws JsonProcessingException {
		Client client = getClient();
		ObjectMapper mapper = new ObjectMapper();
		EsModel es = new EsModel();
		MMX wahaha = new MMX();
		List<MMX> list = new ArrayList<MMX>();
		list.add(wahaha);
		wahaha.setIdx(1).setNamex("哇哈哈").setRemarkx("MBA-CC1");
		es.setCount(11).setId(2).setName("小黄帽").setSearchWord("小黄帽").setWahaha(list);
		IndexRequestBuilder index = client.prepareIndex("com", "EsModel", "2");
		index.setSource(mapper.writeValueAsBytes(es));
		index.execute().actionGet();
	}

	@Test
	public void inster3() throws JsonProcessingException {
		Client client = getClient();
		ObjectMapper mapper = new ObjectMapper();
		EsModel es = new EsModel();
		MMX wahaha = new MMX();
		List<MMX> list = new ArrayList<MMX>();
		list.add(wahaha);
		wahaha.setIdx(1).setNamex("哇哈哈").setRemarkx("MMX-0083");
		es.setCount(11).setId(3).setName("小黄帽").setSearchWord("小黄帽").setWahaha(list);
		IndexRequestBuilder index = client.prepareIndex("com", "EsModel", "3");
		index.setSource(mapper.writeValueAsBytes(es));
		index.execute().actionGet();
	}

	@Test
	public void filderT() {
		Client client = getClient();
		SearchRequestBuilder search = client.prepareSearch("");
		BoolQueryBuilder bool = new BoolQueryBuilder();
		// bool.must(QueryBuilders.filteredQuery(QueryBuilders.matchQuery("a", "1"), QueryBuilders.queryFilter(QueryBuilders.termQuery("b", "2"))));
		// bool.must(QueryBuilders.filteredQuery(QueryBuilders.matchQuery("a", "1"), QueryBuilders.fi);
		System.out.println(bool);
	}

	// @Test
	public void deleteClusterAllIndex() throws InterruptedException, ExecutionException {
		Client client = getClient();
		ClusterStateResponse response = client.admin().cluster().prepareState().execute().get();
		// 获取所有索引
		String[] indexs = response.getState().getMetaData().getConcreteAllIndices();
		for (String index : indexs) {
			System.out.println(index + " delete");//
			// 清空所有索引。

			DeleteIndexResponse deleteIndexResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
			System.out.println(deleteIndexResponse.toString());

		}
	}

	// @Test
	public void deleteAllIndex() throws InterruptedException, ExecutionException {
		Client client = getClient();
		IndicesStatsResponse response = client.admin().indices().prepareStats(null).execute().get();
		System.out.println(response.toString());
		// 获取所有索引
		// String[] indexs = response.getSuccessfulShards()
		// for (String index : indexs) {
		// System.out.println(index + " delete");//
		// // 清空所有索引。
		//
		// DeleteIndexResponse deleteIndexResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
		// System.out.println(deleteIndexResponse.getHeaders());
		//
		// }
		System.out.println((Integer) 129 == (Integer) 129);
	}

	@Test
	public void select() throws Throwable {
		Client client = getClient();
		ElasticSearchHelpper helper = new ElasticSearchHelpper();
		BoolQueryBuilder query = new BoolQueryBuilder();
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("com");
		searchRequestBuilder.setTypes("EsModel");
		query.must(QueryBuilders.termQuery("searchWord", "小黄帽"));
		searchRequestBuilder.setQuery(query);
		System.out.println(query);
		// searchRequestBuilder.setQuery(helper.condition(query, ConditionType.EQUALS, "searchWord", "黄帽", "小蓝", "小紫", "小红"));
		// searchRequestBuilder.setQuery(helper.condition(query, ConditionType.NOT_EQUALS, "searchWord", "小黄", "小蓝", "小紫"));
		// searchRequestBuilder.setQuery(helper.condition(query, ConditionType.LIKE, "searchWord", "小", "小蓝", "小紫"));
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<h2>");
		highlightBuilder.postTags("</h2>");
		highlightBuilder.field("title");
		searchRequestBuilder.highlighter(highlightBuilder);

		Object response = searchRequestBuilder.execute().get();
		System.err.println(response);
	}

	@Test
	public void select2() throws Throwable {
		Client client = getClient();
		ElasticSearchHelpper helper = new ElasticSearchHelpper();
		BoolQueryBuilder query = new BoolQueryBuilder();
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("com");

		// searchRequestBuilder.setQuery(helper.condition(query, ConditionType.EQUALS, "searchWord", "小黄", "小蓝", "小紫"));
		QueryStringQueryBuilder qb = QueryBuilders.queryStringQuery("小黄帽");
		qb.field("searchWord");
		qb.analyzer("ik_max_word");
		searchRequestBuilder.setQuery(qb);
		System.out.println(qb);
		// searchRequestBuilder.setQuery(helper.condition(query, ConditionType.NOT_EQUALS, "searchWord", "小黄", "小蓝", "小紫"));
		// searchRequestBuilder.setQuery(helper.condition(query, ConditionType.EQUALS, "searchWord", "小红帽"));
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<h2>");
		highlightBuilder.postTags("</h2>");
		highlightBuilder.field("searchWord");
		searchRequestBuilder.highlighter(highlightBuilder);
		Object response = searchRequestBuilder.execute().get();
		// QueryBuilders.queryStringQuery(queryString)
		System.err.println(response);

	}

}
