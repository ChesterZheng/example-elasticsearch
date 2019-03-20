package com.example.elasticsearch.tvs.aggregator;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestHistogram {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestHistogram.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * histogram：类似于terms，也是进行bucket分组操作，接收一个field，按照这个field的值的各个范围区间，
	 * 进行bucket分组操作
	 */
	public static void sample(TransportClient client) throws Exception {
		String groupByPriceStr = "group_by_price";
		String sumPriceStr = "sum_price";
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.addAggregation(AggregationBuilders.histogram(groupByPriceStr).field("price").interval(2000)
						.subAggregation(AggregationBuilders.sum(sumPriceStr).field("price"))).setSize(0)
				.get();
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		InternalHistogram groupByPriceHistogram = (InternalHistogram) aggsMap.get(groupByPriceStr);
		List<org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram.Bucket> groupByPriceBucketList = groupByPriceHistogram
				.getBuckets();
		for (int m = 0; m < groupByPriceBucketList.size(); m++) {
			Map<String, Aggregation> subAggsMap = groupByPriceBucketList.get(m).getAggregations().asMap();
			Sum sumPrice = (Sum) subAggsMap.get(sumPriceStr);
			System.out.println(groupByPriceBucketList.get(m).getKey() + "="
					+ groupByPriceBucketList.get(m).getDocCount() + "=" + sumPrice.getValue());
		}
	}

}
