package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestPositiveNegativeBoost {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestPositiveNegativeBoost.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 使用boosting来调整搜索词的relevance score; positive_query包含搜索词
	 * negative_query不排除搜索词, 但是relevance score会降低
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article").setQuery(QueryBuilders
				.boostingQuery(QueryBuilders.matchQuery("title", "java"), QueryBuilders.matchQuery("title", "spark"))
				.negativeBoost(0.5f)).get();
		ElasticSearchUtil.showResults(response);
	}

}
