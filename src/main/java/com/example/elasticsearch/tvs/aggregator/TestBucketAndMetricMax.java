package com.example.elasticsearch.tvs.aggregator;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.metrics.max.Max;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestBucketAndMetricMax {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestBucketAndMetricMax.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 相当于MySQL的查询语句 SELECT MAX(PRICE) FROM TVS.SALES GROUP BY COLOR
	 */
	public static void sample(TransportClient client) throws Exception {
		String groupByColorStr = "group_by_color";
		String maxPriceStr = "max_price";
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.addAggregation(AggregationBuilders.terms(groupByColorStr).field("color")
						.subAggregation(AggregationBuilders.max(maxPriceStr).field("price")))
				.get();
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		StringTerms groupByColor = (StringTerms) aggsMap.get(groupByColorStr);
		System.out.println(groupByColor.getName());
		List<Bucket> groupByColorBucketList = groupByColor.getBuckets();
		for (int m = 0; m < groupByColorBucketList.size(); m++) {
			Map<String, Aggregation> subAggsMap = groupByColorBucketList.get(m).getAggregations().asMap();
			Max maxPrice = (Max) subAggsMap.get(maxPriceStr);
			System.out.println(groupByColorBucketList.get(m).getKey() + "="
					+ groupByColorBucketList.get(m).getDocCount() + "=" + maxPrice.getValue());
		}
	}

}
