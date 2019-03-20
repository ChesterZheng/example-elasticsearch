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

public class TestBucketMultiMetrics {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestBucketMultiMetrics.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	public static void sample(TransportClient client) throws Exception {
		String groupByColorStr = "group_by_colors";
		String avgPriceStr = "avg_price";
		String groupByBrandStr = "group_by_brand";
		String avgBrandPriceStr = "avg_brand_price";
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.addAggregation(AggregationBuilders.terms(groupByColorStr).field("color")
						.subAggregation(AggregationBuilders.avg(avgPriceStr).field("price"))
						.subAggregation(AggregationBuilders.terms(groupByBrandStr).field("brand")
								.subAggregation(AggregationBuilders.avg(avgBrandPriceStr).field("price"))))
				.setSize(0).get();
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		StringTerms groupByColorStringTerms = (StringTerms) aggsMap.get(groupByColorStr);
		System.out.println(groupByColorStringTerms.getName());
		List<Bucket> groupByColorBucketList = groupByColorStringTerms.getBuckets();
		for (int m = 0; m < groupByColorBucketList.size(); m++) {
			Map<String, Aggregation> subAggregationMap = groupByColorBucketList.get(m).getAggregations().asMap();
			Avg avgPrice = (Avg) subAggregationMap.get(avgPriceStr);
			StringTerms groupByBrandStringTerm = (StringTerms) subAggregationMap.get(groupByBrandStr);
			System.out.println(groupByBrandStringTerm.getName());
			List<Bucket> groupByBrandBucketList = groupByBrandStringTerm.getBuckets();
			System.out.println(groupByColorBucketList.get(m).getKey() + "="
					+ groupByColorBucketList.get(m).getDocCount() + "=" + avgPrice.getValue());
			for (int n = 0; n < groupByBrandBucketList.size(); n++) {
				Map<String, Aggregation> subAggsMap = groupByBrandBucketList.get(n).getAggregations().asMap();
				Avg avgBrandPrice = (Avg) subAggsMap.get(avgBrandPriceStr);
				System.out.println(groupByBrandBucketList.get(n).getKey() + "="
						+ groupByBrandBucketList.get(n).getDocCount() + "=" + avgBrandPrice.getValue());
			}
			System.out.println("=========================");
		}
	}

}
