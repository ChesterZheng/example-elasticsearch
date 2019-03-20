package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es terms fitler
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月13日下午5:14:24
 * @Tags
 */
public class TestTermsFilter {

	// 准备测试数据
	// POST /forum/article/_bulk
	// { "update": { "_id": "1"} }
	// { "doc" : {"tag" : ["java", "hadoop"]} }
	// { "update": { "_id": "2"} }
	// { "doc" : {"tag" : ["java"]} }
	// { "update": { "_id": "3"} }
	// { "doc" : {"tag" : ["hadoop"]} }
	// { "update": { "_id": "4"} }
	// { "doc" : {"tag" : ["java", "elasticsearch"]} }
	// POST /forum/article/_bulk
	// { "update": { "_id": "1"} }
	// { "doc" : {"tag_cnt" : 2} }
	// { "update": { "_id": "2"} }
	// { "doc" : {"tag_cnt" : 1} }
	// { "update": { "_id": "3"} }
	// { "doc" : {"tag_cnt" : 1} }
	// { "update": { "_id": "4"} }
	// { "doc" : {"tag_cnt" : 2} }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		System.out.println("sample1的结果");
		// 搜索articleID为KDKE-B-9947-#kL5或QQPX-R-3956-#aD8的帖子
		TestTermsFilter.sample1(client);
		System.out.println("======================================================================");
		System.out.println("sample2的结果");
		// 搜索tag仅仅包含java的帖子
		TestTermsFilter.sample2(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 相当于MySQL数据库查询语句 SELECT * FROM FORUM.ARTICLE WHERE ARTICLE_ID IN
	 * ('KDKE-B-9947-#kL5','QQPX-R-3956-#aD8')
	 */
	public static void sample1(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article").setQuery(
				QueryBuilders.termsQuery("articleID.keyword", new String[] { "KDKE-B-9947-#kL5", "QQPX-R-3956-#aD8" }))
				.get();
		ElasticSearchUtil.showQueryResults(response);
	}

	/*
	 * 相当于MySQL数据库查询语句 SELECT * FROM FORUM.ARTICLE WHERE TAG IN ('java') AND
	 * TAG_CNT = 1
	 */
	public static void sample2(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(
						QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("tag.keyword", new String[] { "java" }))
								.must(QueryBuilders.termQuery("tag_cnt", 1)))
				.get();
		ElasticSearchUtil.showQueryResults(response);
	}

}
