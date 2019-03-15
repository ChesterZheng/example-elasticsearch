package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es ngram提升搜索推荐功能性能
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月15日下午2:34:26
 * @Tags
 */
public class TestNgram {

	// 准备测试数据
	// PUT my_index
	// {
	// "settings": {
	// "analysis": {
	// "filter": {
	// "autocomplete_filter":{
	// "type": "edge_ngram",
	// "min_gram": 1,
	// "max_gram": 20
	// }
	// },
	// "analyzer": {
	// "autocomplete":{
	// "type": "custom",
	// "tokenizer": "standard",
	// "filter":[
	// "lowercase",
	// "autocomplete_filter"
	// ]
	// }
	// }
	// }
	// }
	// }
	// PUT my_index/_mapping/my_type
	// {
	// "properties": {
	// "title":{
	// "type": "string",
	// "analyzer": "autocomplete",
	// "search_analyzer": "standard"
	// }
	// }
	// }
	// PUT my_index/my_type/1
	// {
	// "title": "hello world"
	// }
	// PUT my_index/my_type/2
	// {
	// "title": "hello we"
	// }
	// PUT my_index/my_type/3
	// {
	// "title": "hello win"
	// }
	// PUT my_index/my_type/4
	// {
	// "title": "hello dog"
	// }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestNgram.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 推荐使用match_phrase和ngram去做搜索推荐功能 如果只用match的话, 是全文检索, 按下面的示例来讲,
	 * 包含hello的也会被搜索出来 而如果用match_phrase, 是要求每个term都要出现,并且term之间的postition相差1
	 * 
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("my_index").setTypes("my_type")
				.setQuery(QueryBuilders.matchPhraseQuery("title", "hello w")).get();
		ElasticSearchUtil.showResults(response);
	}

}
