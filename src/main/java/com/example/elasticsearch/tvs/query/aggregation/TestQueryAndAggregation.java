package com.example.elasticsearch.tvs.query.aggregation;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestQueryAndAggregation {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestQueryAndAggregation.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * aggs的scope 任何的聚合, 都必须在搜索出来的结果数据中执行, 搜索结果就是聚合分析操作的scope 相当于MySQL查询语句
	 * SELECT COUNT(1) FROM TVS.SALES WHERE BRAND = '长虹' GROUP BY COLOR
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.setQuery(QueryBuilders.matchQuery("brand", "长虹"))
				.addAggregation(AggregationBuilders.terms("group_by_color").field("color")).get();
		ElasticSearchUtil.showQueryResults(response);
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		StringTerms groupByColorStringTerms = (StringTerms) aggsMap.get("group_by_color");
		System.out.println(groupByColorStringTerms.getName());
		List<Bucket> bucketList = groupByColorStringTerms.getBuckets();
		for (int i = 0; i < bucketList.size(); i++) {
			System.out.println(bucketList.get(i).getKey() + "=" + bucketList.get(i).getDocCount());
		}
	}
}
