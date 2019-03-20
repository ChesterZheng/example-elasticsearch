package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestConstantScore {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestConstantScore.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 使用constant_score忽略搜索词在倒排索引中出现的频率,可提高搜索性能
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery("title", "java"))).get();
		ElasticSearchUtil.showQueryResults(response);
	}
}
