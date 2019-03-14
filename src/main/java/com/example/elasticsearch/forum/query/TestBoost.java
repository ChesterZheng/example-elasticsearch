package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es boost
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月14日下午1:52:56
 * @Tags
 */
public class TestBoost {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		// 搜索标题中包含java，同时，如果标题中包含hadoop或elasticsearch就优先被搜索出来
		// 如果一个帖子包含java hadoop，一个帖子包含java elasticsearch
		// 那么hadoop的帖子要比elasticsearch的帖子优先被搜索出来
		TestBoost.sample1(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 通过使用boost，来控制搜索结果的权重
	 * boost大的relevance score会更高
	 * boost默认为1
	 */
	public static void sample1(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", "java"))
						.should(QueryBuilders.matchQuery("title", "hadoop").boost(3))
						.should(QueryBuilders.matchQuery("title", "elasticsearch").boost(2)))
				.get();
		ElasticSearchUtil.showResults(response);
	}

}
