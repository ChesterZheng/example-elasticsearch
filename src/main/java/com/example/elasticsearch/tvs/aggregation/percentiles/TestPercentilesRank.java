package com.example.elasticsearch.tvs.aggregation.percentiles;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentile;
import org.elasticsearch.search.aggregations.metrics.percentiles.tdigest.InternalTDigestPercentileRanks;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es percentiles rank
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月21日下午5:36:32
 * @Tags
 */
public class TestPercentilesRank {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestPercentilesRank.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * percentileRank使用的是TDigest算法, 用很多节点来执行百分比的计算, 近似估计且会有误差, 节点越多结果越精准
	 * compression限制节点数量, 最多是2000个node去计算【默认是100】 节点越多占用内存越多, 结果越精准, 但性能越差
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("website").setTypes("logs")
				.addAggregation(AggregationBuilders.terms("zones").field("province").subAggregation(AggregationBuilders
						.percentileRanks("load_times").field("latency").values(new double[] { 200, 1000 })))
				.get();
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		StringTerms zonesStringTerms = (StringTerms) aggsMap.get("zones");
		List<Bucket> bucketList = zonesStringTerms.getBuckets();
		for (int i = 0; i < bucketList.size(); i++) {
			Map<String, Aggregation> subAggsMap = bucketList.get(i).getAggregations().asMap();
			System.out.println(bucketList.get(i).getKey() + "=" + bucketList.get(i).getDocCount());
			InternalTDigestPercentileRanks percentileRanks = (InternalTDigestPercentileRanks) subAggsMap
					.get("load_times");
			Iterator<Percentile> iterator = percentileRanks.iterator();
			Percentile percentile = null;
			while (iterator.hasNext()) {
				percentile = iterator.next();
				System.out.println(percentile.getValue() + "=" + percentile.getPercent());
			}
		}
	}

}
