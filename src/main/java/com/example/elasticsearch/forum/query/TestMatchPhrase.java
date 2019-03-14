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
public class TestMatchPhrase {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestMatchPhrase.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 短语匹配
	 * @Date 2019年3月14日下午5:25:08
	 * @Tags  @param client
	 * @Tags  @throws Exception
	 * @ReturnType void
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.matchPhraseQuery("content", "java spark")).get();
		ElasticSearchUtil.showResults(response);
	}

}
