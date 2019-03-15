package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es fuzzy模糊搜索
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月15日下午4:30:20
 * @Tags
 */
public class TestFuzzy {

	// 准备测试数据
	// POST /my_index/my_type/_bulk
	// { "index": { "_id": 1 }}
	// { "text": "Surprise me!"}
	// { "index": { "_id": 2 }}
	// { "text": "That was surprising."}
	// { "index": { "_id": 3 }}
	// { "text": "I wasn't surprised."}

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestFuzzy.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 使用fuzzy模糊查询会自动尝试将搜索词进行纠错, 然后再去倒排索引中查询
	 * fuzziness, 指定你的搜索词最多可以纠正几个字母, 默认值是2
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("my_index").setTypes("my_type")
				.setQuery(QueryBuilders.fuzzyQuery("text", "surprize").fuzziness(Fuzziness.TWO)).get();
		ElasticSearchUtil.showResults(response);
	}

}
