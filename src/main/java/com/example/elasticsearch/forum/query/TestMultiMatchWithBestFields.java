package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es multi match
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月14日下午3:29:52
 * @Tags
 */
public class TestMultiMatchWithBestFields {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestMultiMatchWithBestFields.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * best fields策略,是将某一个field匹配尽可能多的搜索关键词 title^2标识title这个field的权重是基础权重的两倍
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.multiMatchQuery("java solution", new String[] { "title^2", "content" })
						.type(Type.BEST_FIELDS).tieBreaker(0.3f).minimumShouldMatch("50%"))
				.get();
		ElasticSearchUtil.showQueryResults(response);
	}
}
