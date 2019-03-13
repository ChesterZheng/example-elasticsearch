package com.example.elasticsearch.employee.query;

import java.net.InetAddress;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class EmployeeSearchApp {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Settings settings = Settings.builder().put("cluster.name", "Chester-Windows-ElasticSearch").build();
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		EmployeeSearchApp.prepareData(client);
		EmployeeSearchApp.executeSearch(client);
		client.close();
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 执行搜索操作
	 * @Date 2019年3月12日下午1:51:11
	 * @Tags @param client
	 * @Tags @throws Exception
	 * @ReturnType void
	 */
	private static void executeSearch(TransportClient client) throws Exception {
		SearchResponse searchResponse = client.prepareSearch("company").setTypes("employee")
				.setQuery(QueryBuilders.matchQuery("position", "technique"))
				.setPostFilter(QueryBuilders.rangeQuery("age").from(30).to(40)).setFrom(1).setSize(1).get();
		SearchHit[] hits = searchResponse.getHits().getHits();
		for (int i = 0; i < hits.length; i++) {
			System.out.println(hits[i].getSourceAsString());
		}
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 准备数据
	 * @Date 2019年3月12日下午1:45:20
	 * @Tags @param client
	 * @Tags @throws Exception
	 * @ReturnType void
	 */
	private static void prepareData(TransportClient client) throws Exception {
		client.prepareIndex("company", "employee", "1")
				.setSource(XContentFactory.jsonBuilder().startObject().field("name", "jack").field("age", 27)
						.field("position", "technique software").field("country", "China")
						.field("join_date", "2017-01-01").field("salary", 10000).endObject()).get();
		client.prepareIndex("company", "employee", "2")
				.setSource(XContentFactory.jsonBuilder().startObject().field("name", "mary").field("age", 35)
						.field("position", "technique software").field("country", "China")
						.field("join_date", "2017-01-01").field("salary", 12000).endObject()).get();
		client.prepareIndex("company", "employee", "3")
				.setSource(XContentFactory.jsonBuilder().startObject().field("name", "tom").field("age", 32)
						.field("position", "senior technique software").field("country", "China")
						.field("join_date", "2017-01-01").field("salary", 11000).endObject()).get();
		client.prepareIndex("company", "employee", "4")
				.setSource(XContentFactory.jsonBuilder().startObject().field("name", "jen").field("age", 25)
						.field("position", "junior finance").field("country", "USA").field("join_date", "2016-01-01")
						.field("salary", 7000).endObject()).get();
		client.prepareIndex("company", "employee", "5")
				.setSource(XContentFactory.jsonBuilder().startObject().field("name", "mike").field("age", 37)
						.field("position", "finance manager").field("country", "USA").field("join_date", "2017-01-01")
						.field("salary", 15000).endObject()).get();
	}

}
