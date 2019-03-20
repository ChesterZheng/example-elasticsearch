package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es bool with filter
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月13日下午4:43:21
 * @Tags
 */
public class TestBoolAndMultiFilter {

	// 准备四条测试数据
	// POST /forum/article/_bulk
	// { "index": { "_id": 1 }}
	// { "articleID" : "XHDK-A-1293-#fJ3", "userID" : 1, "hidden": false,
	// "postDate": "2017-01-01" }
	// { "index": { "_id": 2 }}
	// { "articleID" : "KDKE-B-9947-#kL5", "userID" : 1, "hidden": false,
	// "postDate": "2017-01-02" }
	// { "index": { "_id": 3 }}
	// { "articleID" : "JODL-X-1937-#pV7", "userID" : 2, "hidden": false,
	// "postDate": "2017-01-01" }
	// { "index": { "_id": 4 }}
	// { "articleID" : "QQPX-R-3956-#aD8", "userID" : 2, "hidden": true,
	// "postDate": "2017-01-02" }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		System.out.println("sample1的结果");
		// 搜索发帖日期为2017-01-01，或者帖子ID为XHDK-A-1293-#fJ3的帖子，同时要求帖子的发帖日期绝对不为2017-01-02
		TestBoolAndMultiFilter.sample1(client);
		System.out.println("======================================================================");
		System.out.println("sample2的结果");
		// 搜索帖子ID为XHDK-A-1293-#fJ3，或者是帖子ID为JODL-X-1937-#pV7而且发帖日期为2017-01-01的帖子
		TestBoolAndMultiFilter.sample2(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 相当于MySQL数据库查询语句 SELECT * FROM FORUM.ARTICLE WHERE (POST_DATE =
	 * '2017-01-01' OR ARTICLE_ID = 'XHDK-A-1293-#fJ3') AND POST_DATE !=
	 * '2017-01-02'
	 */
	public static void sample1(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("postDate", "2017-01-01"))
						.should(QueryBuilders.termQuery("articleID.keyword", "XHDK-A-1293-#fJ3"))
						.mustNot(QueryBuilders.termQuery("postDate", "2017-01-02")))
				.get();
		ElasticSearchUtil.showQueryResults(response);
	}

	/*
	 * 相当于MySQL数据库查询语句 SELECT * FROM FORUM.ARTICLE WHERE ARTICLE_ID =
	 * 'XHDK-A-1293-#fJ3' OR (ARTICLE_ID = 'JODL-X-1937-#pV7' AND POST_DATE =
	 * '2017-01-01')
	 * 
	 */
	public static void sample2(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery("articleID.keyword", "XHDK-A-1293-#fJ3"))
						.should(QueryBuilders.boolQuery()
								.must(QueryBuilders.termQuery("articleID.keyword", "JODL-X-1937-#pV7"))
								.must(QueryBuilders.termQuery("postDate", "2017-01-01"))))
				.get();
		ElasticSearchUtil.showQueryResults(response);
	}

}
