package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es range filter
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月13日下午5:15:29
 * @Tags
 */
public class TestRangeFilter {

	// 准备测试数据
	// POST /forum/article/_bulk
	// { "update": { "_id": "1"} }
	// { "doc" : {"view_cnt" : 30} }
	// { "update": { "_id": "2"} }
	// { "doc" : {"view_cnt" : 50} }
	// { "update": { "_id": "3"} }
	// { "doc" : {"view_cnt" : 100} }
	// { "update": { "_id": "4"} }
	// { "doc" : {"view_cnt" : 80} }
	// POST /forum/article/_bulk
	// { "index": { "_id": 5 }}
	// { "articleID" : "DHJK-B-1395-#Ky5", "userID" : 3, "hidden": false,
	// "postDate": "2017-03-01", "tag": ["elasticsearch"], "tag_cnt": 1,
	// "view_cnt": 10 }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		System.out.println("sample1的结果");
		// 搜索浏览量在30~60之间的帖子
		TestRangeFilter.sample1(client);
		System.out.println("======================================================================");
		System.out.println("sample2的结果");
		// 搜索发帖日期在近一个月之内的帖子
		TestRangeFilter.sample2(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 相当于MySQL数据库查询语句 SELECT * FROM FORUM.ARTICLE WHERE VIEW_CNT > 30 AND
	 * VIEW_CNT < 60 大于使用 gt 小于使用 lt 大于等于 gte 小于等于 lte
	 */
	public static void sample1(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.rangeQuery("view_cnt").gt(30).lt(60)).get();
		SearchHit[] hits = response.getHits().getHits();
		for (int i = 0; i < hits.length; i++) {
			String sourceAsString = hits[i].getSourceAsString();
			System.out.println(sourceAsString);
		}
	}

	/*
	 * 相当于MySQL数据库查询语句 SELECT * FROM FORUM.ARTICLE WHERE POST_DATE =
	 * DATE_ADD(NOW(), INTERVAL -30 DAY)
	 * 
	 * 2019-03-13||-30d 和 now-30d 都可以
	 */
	public static void sample2(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.rangeQuery("postDate").gt("2019-03-13||-30d")).get();
		SearchHit[] hits = response.getHits().getHits();
		for (int i = 0; i < hits.length; i++) {
			String sourceAsString = hits[i].getSourceAsString();
			System.out.println(sourceAsString);
		}
	}

}
