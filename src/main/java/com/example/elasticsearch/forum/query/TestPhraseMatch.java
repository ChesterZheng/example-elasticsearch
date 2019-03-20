package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es match phrase
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月14日下午5:13:25
 * @Tags
 */
public class TestPhraseMatch {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestPhraseMatch.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 短语匹配原理 match_phrase在搜索时, 会将java spark拆分成java和spark
	 * 然后去docement中搜索，满足java的term position比spark的term position小1的将会被搜索出来
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.matchPhraseQuery("content", "java spark")).get();
		ElasticSearchUtil.showQueryResults(response);
	}

}
