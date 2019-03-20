package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es ik 中文分词器
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月15日下午5:29:58
 * @Tags
 */
public class TestIKAnalyzer {

	// 准备测试数据
	// PUT /ik_index
	// {
	// "mappings": {
	// "ik_type": {
	// "properties": {
	// "text": {
	// "type": "text",
	// "analyzer": "ik_max_word"
	// }
	// }
	// }
	// }
	// }
	// POST /ik_index/ik_type/_bulk
	// { "index": { "_id": "1"} }
	// { "text": "男子偷上万元发红包求交女友 被抓获时仍然单身" }
	// { "index": { "_id": "2"} }
	// { "text": "16岁少女为结婚“变”22岁 7年后想离婚被法院拒绝" }
	// { "index": { "_id": "3"} }
	// { "text": "深圳女孩骑车逆行撞奔驰 遭索赔被吓哭(图)" }
	// { "index": { "_id": "4"} }
	// { "text": "女人对护肤品比对男票好？网友神怼" }
	// { "index": { "_id": "5"} }
	// { "text": "为什么国内的街道招牌用的都是红黄配？" }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestIKAnalyzer.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("ik_index").setTypes("ik_type")
				.setQuery(QueryBuilders.matchQuery("text", "16岁少女结婚好还是单身好")).get();
		ElasticSearchUtil.showQueryResults(response);
	}
}
