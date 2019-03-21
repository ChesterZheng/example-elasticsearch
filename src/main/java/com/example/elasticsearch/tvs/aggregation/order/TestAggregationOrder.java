package com.example.elasticsearch.tvs.aggregation.order;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestAggregationOrder {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestAggregationOrder.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 针对某一个aggs做filter过滤
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.addAggregation(AggregationBuilders.terms("group_by_color").field("color")
						.order(Terms.Order.aggregation("avg_price", true))
						.subAggregation(AggregationBuilders.avg("avg_price").field("price")))
				.get();
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		StringTerms groupByColorStringTerms = (StringTerms) aggsMap.get("group_by_color");
		List<Bucket> bucketList = groupByColorStringTerms.getBuckets();
		for (int i = 0; i < bucketList.size(); i++) {
			Map<String, Aggregation> subAggsMap = bucketList.get(i).getAggregations().asMap();
			Avg avgPrice = (Avg) subAggsMap.get("avg_price");
			System.out.println(
					bucketList.get(i).getKey() + "=" + bucketList.get(i).getDocCount() + "=" + avgPrice.getValue());
		}
	}

}
