package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es regex search
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月15日下午12:13:56
 * @Tags
 */
public class TestRegexSearch {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestRegexSearch.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 正则搜索需要扫描所有的倒排索引, 性能很差
	 * 
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("my_index").setTypes("my_type")
				.setQuery(QueryBuilders.regexpQuery("title", "C[0-9].*45")).get();
		ElasticSearchUtil.showResults(response);
	}

}
