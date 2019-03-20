package com.example.elasticsearch.tvs.aggregator;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;

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
	 * 
	 */
	public static void sample(TransportClient client) throws Exception {
		
	}

}
