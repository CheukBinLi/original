package com.cheuks.bin.original.test.elasticsearch;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.client.transport.TransportClient.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class T1 {

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

	@Test
	public void createIndex() throws JsonProcessingException {
		Client client = getClient();
		TestModel tm;
		ObjectMapper mapper = new ObjectMapper();
		for (int i = 0; i < 30; i++) {
			tm = new TestModel();
			System.err.println(tm.getId());
			IndexResponse indexResponse = client.prepareIndex("index", "TTXX", Integer.toString(tm.getId())).setSource(mapper.writeValueAsString(tm)).execute().actionGet();
			// System.err.println(indexResponse.isCreated());
		}
		tm = new TestModel().setName("小苹果");
		IndexResponse indexResponse = client.prepareIndex("index", "TTXX", Integer.toString(tm.getId())).setSource(mapper.writeValueAsBytes(tm)).get();
	}

	@Test
	public void createIndex2() throws IOException {
		Client client = getClient();
		IndexResponse response = client.prepareIndex("comment_index", "comment_ugc", "comment_123674").setSource(XContentFactory.jsonBuilder().startObject().field("author", "569874").endObject().field("author_name", "riching").field("mark", 232).startObject().field("body", "北京不错，但是人太多了").field("store","yes").endObject().field("valid", true).endObject()).setTTL(8000).execute().actionGet();

		System.out.println(response.getId());
	}

	@Test
	public void clear() {
		Client client = getClient();
		client.admin().indices().prepareDelete("index").execute().actionGet();
	}

	@Test
	public void getById() {
		Client client = getClient();
		GetResponse response = client.prepareGet("index", "TTXX", "11").get();
		System.err.println(response.getSourceAsString());
	}

	@Test
	public void getByName() {
		String str = "苹果";
		Client client = getClient();
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("index");
		searchRequestBuilder.setTypes("TTXX");
		// searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.must(QueryBuilders.wildcardQuery("name", str));
		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

		for (SearchHit hit1 : searchResponse.getHits()) {
			Map<String, Object> source1 = hit1.getSource();
			if (!source1.isEmpty()) {
				for (Iterator<Map.Entry<String, Object>> it = source1.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, Object> entry = it.next();
					System.out.println(entry.getKey() + "=======" + entry.getValue());
				}
			}
		}

		// System.err.println(searchResponse);
		// com.fasterxml.jackson.core.base.GeneratorBase.getOutputContext()
	}

	public static void main(String[] args) {
		new T1().getByName();
	}
}
