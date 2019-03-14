package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es cross fields 策略
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月14日下午4:20:08
 * @Tags
 */
public class TestMultiMatchWithCrossFields {

	// 准备测试数据
	// POST /forum/article/_bulk
	// { "update": { "_id": "1"} }
	// { "doc" : {"author_first_name" : "Peter", "author_last_name" : "Smith"} }
	// { "update": { "_id": "2"} }
	// { "doc" : {"author_first_name" : "Smith", "author_last_name" :
	// "Williams"} }
	// { "update": { "_id": "3"} }
	// { "doc" : {"author_first_name" : "Jack", "author_last_name" : "Ma"} }
	// { "update": { "_id": "4"} }
	// { "doc" : {"author_first_name" : "Robbin", "author_last_name" : "Li"} }
	// { "update": { "_id": "5"} }
	// { "doc" : {"author_first_name" : "Tonny", "author_last_name" : "Peter
	// Smith"} }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestMultiMatchWithCrossFields.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * best fields策略,是将某一个field匹配尽可能多的搜索关键词, 并优先返回（排序在靠前） most fields策略,
	 * 是尽可能多返回更多field匹配到某一个搜索关键词, 并优先返回（排序在靠前） cross fields策略是可以跨field去匹配，比如人名,
	 * 地址 可能散落在多个field当中
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders
						.multiMatchQuery("Peter Smith", new String[] { "author_first_name", "author_last_name" })
						.operator(Operator.AND).type(Type.CROSS_FIELDS))
				.get();
		ElasticSearchUtil.showResults(response);
	}

}
