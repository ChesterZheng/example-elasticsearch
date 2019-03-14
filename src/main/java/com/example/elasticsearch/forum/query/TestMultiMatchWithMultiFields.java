package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es most fields 策略
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月14日下午3:43:44
 * @Tags
 */
public class TestMultiMatchWithMultiFields {

	// 准备测试数据
	// POST /forum/article/_bulk
	// { "update": { "_id": "1"} }
	// { "doc" : {"sub_title" : "learning more courses"} }
	// { "update": { "_id": "2"} }
	// { "doc" : {"sub_title" : "learned a lot of course"} }
	// { "update": { "_id": "3"} }
	// { "doc" : {"sub_title" : "we have a lot of fun"} }
	// { "update": { "_id": "4"} }
	// { "doc" : {"sub_title" : "both of them are good"} }
	// { "update": { "_id": "5"} }
	// { "doc" : {"sub_title" : "haha, hello world"} }

	/*
	 * best fields策略,是将某一个field匹配尽可能多的搜索关键词, 并优先返回（排序在靠前） most fields策略,
	 * 是尽可能多返回更多field匹配到某一个搜索关键词, 并优先返回（排序在靠前）
	 */
	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		System.out.println("sample1的结果");
		TestMultiMatchWithMultiFields.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(
						QueryBuilders.multiMatchQuery("learning courses", new String[] { "sub_title", "sub_title.std" })
								.type(Type.MOST_FIELDS))
				.get();
		ElasticSearchUtil.showResults(response);
	}
}
