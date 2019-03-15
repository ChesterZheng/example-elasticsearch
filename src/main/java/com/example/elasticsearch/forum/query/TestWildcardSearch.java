package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es wildcard search
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月15日下午12:12:47
 * @Tags
 */
public class TestWildcardSearch {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestWildcardSearch.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 通配符搜索必须扫描所有的倒排索引, 性能很差
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("my_index").setTypes("my_type")
				.setQuery(QueryBuilders.wildcardQuery("title", "C?D?-*5")).get();
		ElasticSearchUtil.showResults(response);
	}

}
