package com.example.elasticsearch.tvs.aggregator;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestBucketAndMetricAvg {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestBucketAndMetricAvg.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 相当于MySQL的查询语句 SELECT AVG(PRICE) FROM TVS.SALES GROUP BY COLOR
	 */
	public static void sample(TransportClient client) throws Exception {
		String groupBy = "group_by_popular_colors";
		String avgPrice = "avg_price";
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales").addAggregation(AggregationBuilders
				.terms(groupBy).field("color").subAggregation(AggregationBuilders.avg(avgPrice).field("price")))
				.setSize(0).get();
		Map<String, Aggregation> aggregationMap = response.getAggregations().asMap();
		StringTerms stringTerms = (StringTerms) aggregationMap.get(groupBy);
		String groupByName = stringTerms.getName();
		System.out.println(groupByName);
		List<Bucket> buckets = stringTerms.getBuckets();
		for (int i = 0; i < buckets.size(); i++) {
			Map<String, Aggregation> subAggregationMap = buckets.get(i).getAggregations().asMap();
			Avg avg = (Avg) subAggregationMap.get(avgPrice);
			System.out.println(buckets.get(i).getKey() + "=" + buckets.get(i).getDocCount() + "=" + avg.getValue());
		}
	}

}
