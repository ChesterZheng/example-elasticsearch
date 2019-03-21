package com.example.elasticsearch.tvs.aggregation.percentiles;

import java.util.Iterator;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentile;
import org.elasticsearch.search.aggregations.metrics.percentiles.tdigest.InternalTDigestPercentiles;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestPercentiles {

	// 准备测试数据
	// POST /website/logs/_bulk
	// { "index": {}}
	// { "latency" : 105, "province" : "江苏", "timestamp" : "2016-10-28" }
	// { "index": {}}
	// { "latency" : 83, "province" : "江苏", "timestamp" : "2016-10-29" }
	// { "index": {}}
	// { "latency" : 92, "province" : "江苏", "timestamp" : "2016-10-29" }
	// { "index": {}}
	// { "latency" : 112, "province" : "江苏", "timestamp" : "2016-10-28" }
	// { "index": {}}
	// { "latency" : 68, "province" : "江苏", "timestamp" : "2016-10-28" }
	// { "index": {}}
	// { "latency" : 76, "province" : "江苏", "timestamp" : "2016-10-29" }
	// { "index": {}}
	// { "latency" : 101, "province" : "新疆", "timestamp" : "2016-10-28" }
	// { "index": {}}
	// { "latency" : 275, "province" : "新疆", "timestamp" : "2016-10-29" }
	// { "index": {}}
	// { "latency" : 166, "province" : "新疆", "timestamp" : "2016-10-29" }
	// { "index": {}}
	// { "latency" : 654, "province" : "新疆", "timestamp" : "2016-10-28" }
	// { "index": {}}
	// { "latency" : 389, "province" : "新疆", "timestamp" : "2016-10-28" }
	// { "index": {}}
	// { "latency" : 302, "province" : "新疆", "timestamp" : "2016-10-29" }

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestPercentiles.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("website").setTypes("logs")
				.addAggregation(AggregationBuilders.percentiles("latency_percentiles").field("latency")
						.percentiles(new double[] { 50d, 95d, 99d })
						.subAggregation(AggregationBuilders.avg("latency_avg").field("latency")))
				.get();
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		InternalTDigestPercentiles percentiles = (InternalTDigestPercentiles) aggsMap.get("latency_percentiles");
		System.out.println(percentiles.getName());
		Iterator<Percentile> iterator = percentiles.iterator();
		while (iterator.hasNext()) {
			Percentile percentile = iterator.next();
			System.out.println(percentile.getPercent() + "=" + percentile.getValue());
		}
	}
}
