package com.example.elasticsearch.tvs.aggregator;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestBucket {

	// 准备测试数据
	// PUT /tvs
	// {
	// "mappings": {
	// "sales": {
	// "properties": {
	// "price": {
	// "type": "long"
	// },
	// "color": {
	// "type": "keyword"
	// },
	// "brand": {
	// "type": "keyword"
	// },
	// "sold_date": {
	// "type": "date"
	// }
	// }
	// }
	// }
	// }
	// POST /tvs/sales/_bulk
	// { "index": {}}
	// { "price" : 1000, "color" : "红色", "brand" : "长虹", "sold_date" :
	// "2016-10-28" }
	// { "index": {}}
	// { "price" : 2000, "color" : "红色", "brand" : "长虹", "sold_date" :
	// "2016-11-05" }
	// { "index": {}}
	// { "price" : 3000, "color" : "绿色", "brand" : "小米", "sold_date" :
	// "2016-05-18" }
	// { "index": {}}
	// { "price" : 1500, "color" : "蓝色", "brand" : "TCL", "sold_date" :
	// "2016-07-02" }
	// { "index": {}}
	// { "price" : 1200, "color" : "绿色", "brand" : "TCL", "sold_date" :
	// "2016-08-19" }
	// { "index": {}}
	// { "price" : 2000, "color" : "红色", "brand" : "长虹", "sold_date" :
	// "2016-11-05" }
	// { "index": {}}
	// { "price" : 8000, "color" : "红色", "brand" : "三星", "sold_date" :
	// "2017-01-01" }
	// { "index": {}}
	// { "price" : 2500, "color" : "蓝色", "brand" : "小米", "sold_date" :
	// "2017-02-12" }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestBucket.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	public static void sample(TransportClient client) throws Exception {
		String aggsName = "aggregation_popular_colors";
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.addAggregation(AggregationBuilders.terms(aggsName).field("color")).setSize(0).get();
		ElasticSearchUtil.showAggregationResults(response, aggsName);
	}

}
