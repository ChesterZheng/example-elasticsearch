package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es term filter
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月13日下午2:54:40
 * @Tags
 */
public class TestTermFilter {

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
		// 搜索articleID为XHDK-A-1293-#fJ3的帖子
		TestTermFilter.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 相当于MySQL数据库查询语句 SELECT * FROM FORUM.ARTICLE WHERE ARTICLE_ID =
	 * 'XHDK-A-1293-#fJ3'
	 */
	public static void sample(TransportClient client) throws Exception {
		/*
		 * term filter是根据exact value进行搜索【数字类型，布尔类型，日期类型天然支持】
		 * text/string类型需要在创建索引时指定not_analyzed(keyword)，才可以用term filter
		 * 如果field的mapping的type是text/string类型，则查询的时候需要使用field.keyword才可以保证不分词
		 * 如果field的mapping的type是keyword类型，则查询的时候就是不分词的
		 */
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				// .setQuery(QueryBuilders.termQuery("articleID","XHDK-A-1293-#fJ3")).get();
				.setQuery(QueryBuilders.termQuery("articleID.keyword", "XHDK-A-1293-#fJ3")).get();
		ElasticSearchUtil.showQueryResults(response);
	}

}
