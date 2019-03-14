package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es slop
 * @Author Chester_Zheng
 * @Date 2019年3月14日下午5:38:36
 * @Tags
 */
public class TestProximityMatchWithSlop {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestProximityMatchWithSlop.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * slop参数含义
	 * term最多可以移动几次去匹配document
	 * term之间的距离越近, relevance score越高
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.matchPhraseQuery("content", "java best").slop(15)).get();
		ElasticSearchUtil.showResults(response);
	}

}
