package com.example.elasticsearch.tvs.aggregation.filter;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestAggregationFilter {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestAggregationFilter.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.setQuery(QueryBuilders.rangeQuery("price").gte(1200))
				.addAggregation(AggregationBuilders.avg("avg_price").field("price")).setSize(0).get();
		ElasticSearchUtil.showQueryResults(response);
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		Avg avgPrice = (Avg) aggsMap.get("avg_price");
		System.out.println(avgPrice.getName() + "=" + avgPrice.getValue());
	}
	
}
