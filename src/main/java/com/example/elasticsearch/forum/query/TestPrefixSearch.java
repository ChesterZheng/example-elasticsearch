package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es prefix search
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月15日上午11:53:00
 * @Tags
 */
public class TestPrefixSearch {

	// 准备测试数据
	// PUT my_index
	// {
	// "mappings": {
	// "my_type": {
	// "properties": {
	// "title":{
	// "type": "keyword"
	// }
	// }
	// }
	// }
	// }
	// PUT my_index/my_type/1
	// {
	// "title": "C3D0-KD345"
	// }
	// PUT my_index/my_type/2
	// {
	// "title": "C3K5-DFG65"
	// }
	// PUT my_index/my_type/3
	// {
	// "title": "C4I8-UI365"
	// }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestPrefixSearch.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 前缀搜索(不计算相关度分数) 前缀越短, 要处理的docement越多, 性能越差(尽可能使用长一些的前缀) 前缀搜索需要扫描所有的倒排索引,
	 * 性能很差
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("my_index").setTypes("my_type")
				.setQuery(QueryBuilders.prefixQuery("title", "C3")).get();
		ElasticSearchUtil.showQueryResults(response);
	}

}
