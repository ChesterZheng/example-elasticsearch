package com.example.elasticsearch.util;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * ES工具类
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月13日下午2:59:03
 * @Tags
 */
public class ElasticSearchUtil {

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 初始化
	 * @Date 2019年3月13日下午2:59:11
	 * @Tags @return
	 * @Tags @throws Exception
	 * @ReturnType TransportClient
	 */
	@SuppressWarnings("resource")
	public static TransportClient init() throws Exception {
		Settings settings = Settings.builder().put("cluster.name", "Chester-Windows-ElasticSearch").build();
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		return client;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 打印搜索结果
	 * @Date 2019年3月14日下午3:35:46
	 * @Tags @param response
	 * @Tags @throws Exception
	 * @ReturnType void
	 */
	public static void showQueryResults(SearchResponse response) throws Exception {
		SearchHit[] hits = response.getHits().getHits();
		for (int i = 0; i < hits.length; i++) {
			float score = hits[i].getScore();
			String sourceAsString = hits[i].getSourceAsString();
			System.out.println("relevance score = " + score + " _source = " + sourceAsString);
		}
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 打印聚合分析结果
	 * @Date 2019年3月20日下午12:06:16
	 * @Tags @param response
	 * @Tags @param aggsName
	 * @Tags @throws Exception
	 * @ReturnType void
	 */
	public static void showAggregationResults(SearchResponse response, String aggsName) throws Exception {
		Map<String, Aggregation> aggregationMap = response.getAggregations().asMap();
		StringTerms stringTerms = (StringTerms) aggregationMap.get(aggsName);
		String name = stringTerms.getName();
		System.out.println(name);
		List<Bucket> buckets = stringTerms.getBuckets();
		for (int i = 0; i < buckets.size(); i++) {
			System.out.println(buckets.get(i).getKey() + "=" + buckets.get(i).getDocCount());
		}
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 关闭
	 * @Date 2019年3月13日下午2:59:19
	 * @Tags @param client
	 * @Tags @throws Exception
	 * @ReturnType void
	 */
	public static void destory(TransportClient client) throws Exception {
		client.close();
	}

}
