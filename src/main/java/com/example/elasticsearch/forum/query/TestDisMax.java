package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestDisMax {

	// 准备测试数据
	// POST /forum/article/_bulk
	// { "update": { "_id": "1"} }
	// { "doc" : {"content" : "i like to write best elasticsearch article"} }
	// { "update": { "_id": "2"} }
	// { "doc" : {"content" : "i think java is the best programming language"} }
	// { "update": { "_id": "3"} }
	// { "doc" : {"content" : "i am only an elasticsearch beginner"} }
	// { "update": { "_id": "4"} }
	// { "doc" : {"content" : "elasticsearch and hadoop are all very good
	// solution, i am a beginner"} }
	// { "update": { "_id": "5"} }
	// { "doc" : {"content" : "spark is best big data solution based on scala
	// ,an programming language similar to java"} }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestDisMax.sample1(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * dis_max策略意思就是搜索到的结果 应该是某一个field中匹配到了尽可能多的关键词 这样的docement应该被排在前面
	 * 而不是尽可能多的field匹配到了少数关键词
	 */
	private static void sample1(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.disMaxQuery().add(QueryBuilders.matchQuery("title", "java solution"))
						.add(QueryBuilders.matchQuery("content", "java solution")))
				.get();
		SearchHit[] hits = response.getHits().getHits();
		for (int i = 0; i < hits.length; i++) {
			float score = hits[i].getScore();
			String sourceAsString = hits[i].getSourceAsString();
			System.out.println("relevance score = " + score + " _source = " + sourceAsString);
		}
	}

}
