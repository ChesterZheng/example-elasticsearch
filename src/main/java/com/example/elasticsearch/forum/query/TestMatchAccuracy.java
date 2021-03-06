package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es match operator
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月13日下午5:45:09
 * @Tags
 */
public class TestMatchAccuracy {

	// 准备测试数据
	// POST /forum/article/_bulk
	// { "update": { "_id": "1"} }
	// { "doc" : {"title" : "this is java and elasticsearch blog"} }
	// { "update": { "_id": "2"} }
	// { "doc" : {"title" : "this is java blog"} }
	// { "update": { "_id": "3"} }
	// { "doc" : {"title" : "this is elasticsearch blog"} }
	// { "update": { "_id": "4"} }
	// { "doc" : {"title" : "this is java, elasticsearch, hadoop blog"} }
	// { "update": { "_id": "5"} }
	// { "doc" : {"title" : "this is spark blog"} }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		System.out.println("sample1的结果");
		// 搜索标题中包含java或elasticsearch的blog
		TestMatchAccuracy.sample1(client);
		System.out.println("======================================================================");
		System.out.println("sample2的结果");
		// 搜索标题中包含java和elasticsearch的blog
		TestMatchAccuracy.sample2(client);
		System.out.println("======================================================================");
		System.out.println("sample3的结果");
		// 搜索java,elasticsearch,spark,hadoop 4个关键字中，至少包含其中3个的blog
		TestMatchAccuracy.sample3(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 相当于MySQL数据库查询语句 SELECT * FROM FORUM.ARTICLE WHERE TITLE LIKE '%java%' OR
	 * TITLE LIKE '%elasticsearch%' match query 在es底层其实转换成为bool+should
	 */
	public static void sample1(TransportClient client) throws Exception {
		/*
		 * term query是进行exact value搜索 match query是进行full text搜索
		 * 如果要检索的field是not_analyzed(keyword)类型 那么match query 相当于term query
		 */
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.matchQuery("title", "java elasticsearch")).get();
		ElasticSearchUtil.showQueryResults(response);
	}

	/*
	 * 相当于MySQL数据库查询语句 SELECT * FROM FORUM.ARTICLE WHERE TITLE LIKE
	 * '%java%elasticsearch%' match+operator在es底层其实转换成为bool+must
	 */
	public static void sample2(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.matchQuery("title", "java elasticsearch").operator(Operator.AND)).get();
		ElasticSearchUtil.showQueryResults(response);
	}

	/*
	 * match+minimumShouldMatch在es底层实际转换为bool+should+term+minimumShouldMatch
	 */
	public static void sample3(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(
						QueryBuilders.matchQuery("title", "java elasticsearch spark hadoop").minimumShouldMatch("75%"))
				.get();
		ElasticSearchUtil.showQueryResults(response);
	}
}
