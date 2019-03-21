package com.example.elasticsearch.tvs.query.aggregation;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.global.InternalGlobal;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestGlobalBucket {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestGlobalBucket.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * global bucket就是将所有数据纳入聚合的scope, 而不管之前的query
	 * 例如：在一次收缩中获取单个商品平均价格和全部商品平均价格
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.setQuery(QueryBuilders.termQuery("brand", "长虹"))
				.addAggregation(AggregationBuilders.avg("single_brand_avg_price").field("price"))
				.addAggregation(AggregationBuilders.global("all")
						.subAggregation(AggregationBuilders.avg("all_brand_avg_price").field("price")))
				.get();
		ElasticSearchUtil.showQueryResults(response);
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		Avg singleBrandAvgPrice = (Avg) aggsMap.get("single_brand_avg_price");
		System.out.println(singleBrandAvgPrice.getName() + "=" + singleBrandAvgPrice.getValue());
		InternalGlobal global  = (InternalGlobal) aggsMap.get("all");
		Map<String, Aggregation> globalAggsMap = global.getAggregations().asMap();
		Avg allBrandAvgPrice = (Avg) globalAggsMap.get("all_brand_avg_price");
		System.out.println(allBrandAvgPrice.getName() + "=" + allBrandAvgPrice.getValue());
	}

}
