package com.example.elasticsearch.tvs.datehistogram;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket;
import org.elasticsearch.search.aggregations.metrics.cardinality.InternalCardinality;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestDateHistogramAndCartinality {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestDateHistogramAndCartinality.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * cardinality去重, 相当于MySQL中的count(distinct xxx)
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.addAggregation(AggregationBuilders.dateHistogram("group_by_sold_date").field("sold_date")
						.dateHistogramInterval(DateHistogramInterval.MONTH).format("yyyy-MM-dd").minDocCount(0l)
						.subAggregation(AggregationBuilders.cardinality("distinct_brand").field("brand")))
				.get();
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		InternalDateHistogram dateHistogram = (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram) aggsMap
				.get("group_by_sold_date");
		List<Bucket> bucketList = dateHistogram.getBuckets();
		for (int i = 0; i < bucketList.size(); i++) {
			Map<String, Aggregation> subAggsMap = bucketList.get(i).getAggregations().asMap();
			InternalCardinality cardinality = (org.elasticsearch.search.aggregations.metrics.cardinality.InternalCardinality) subAggsMap
					.get("distinct_brand");
			System.out.println(bucketList.get(i).getKey() + "=" + bucketList.get(i).getDocCount() + "="
					+ cardinality.getName() + "=" + cardinality.getValue());
		}
	}

}
