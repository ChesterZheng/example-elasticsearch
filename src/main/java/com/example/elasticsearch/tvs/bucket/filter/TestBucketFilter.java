package com.example.elasticsearch.tvs.bucket.filter;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestBucketFilter {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestBucketFilter.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 针对某一个aggs做filter过滤
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.setQuery(QueryBuilders.termQuery("brand", "长虹"))
				.addAggregation(AggregationBuilders
						.filter("recent_three_years", QueryBuilders.rangeQuery("sold_date").gte("now-3y"))
						.subAggregation(AggregationBuilders.avg("recent_three_years_avg_price").field("price")))
				.setSize(0).get();
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		InternalFilter internalFilter = (org.elasticsearch.search.aggregations.bucket.filter.InternalFilter) aggsMap
				.get("recent_three_years");
		System.out.println(internalFilter.getName() + "=" + internalFilter.getDocCount());
		Map<String, Aggregation> subAggsMap = internalFilter.getAggregations().asMap();
		Avg avg = (Avg) subAggsMap.get("recent_three_years_avg_price");
		System.out.println(avg.getName() + "=" + avg.getValue());
	}

}
