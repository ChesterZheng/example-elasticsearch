package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestDisMaxAndTieBreaker {

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
		System.out.println("sample1的结果");
		TestDisMaxAndTieBreaker.sample1(client);
		System.out.println("======================================================================");
		System.out.println("sample2的结果");
		TestDisMaxAndTieBreaker.sample2(client);
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

	/*
	 * tie_breaker参数的意义 在dis_max的基础上 将其他query的relevance score * tie_breaker
	 * 并与relevance score分数最高的那个query综合一起计算最终的relevance score
	 * tie_breaker取值范围在0-1之间
	 */
	private static void sample2(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.disMaxQuery().add(QueryBuilders.matchQuery("title", "java solution"))
						.add(QueryBuilders.matchQuery("content", "java solution")).tieBreaker(0.3f))
				.get();
		SearchHit[] hits = response.getHits().getHits();
		for (int i = 0; i < hits.length; i++) {
			float score = hits[i].getScore();
			String sourceAsString = hits[i].getSourceAsString();
			System.out.println("relevance score = " + score + " _source = " + sourceAsString);
		}
	}

}
