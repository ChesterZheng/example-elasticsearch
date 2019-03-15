package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.rescore.RescoreBuilder;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * 使用match和近似匹配实现召回率和精准度的平衡
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月14日下午6:10:44
 * @Tags
 */
public class TestRecallAndPrecision {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		System.out.println("sample1的结果");
		TestRecallAndPrecision.sample1(client);
		System.out.println("======================================================================");
		System.out.println("sample2的结果");
		TestRecallAndPrecision.sample2(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * match phrase match proximity match区别 match ==> 只要匹配到一个term就可以返回, 扫描倒排索引
	 * phrase match ==> 首先扫描到所有term的docement list, 找到包含所有term的docement list,
	 * 然后对每个docement都计算term的position,是否符合指定的范围 proximity match(slop) ==>
	 * 需要进行复杂的计算, 来判断是否可以通过slop的移动, 匹配document
	 */
	public static void sample1(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", "java spark"))
						.minimumShouldMatch("50%")
						.should(QueryBuilders.matchPhraseQuery("title", "java spark").slop(50)))
				.get();
		ElasticSearchUtil.showResults(response);
	}

	/*
	 * 使用rescore优化搜索性能
	 */
	public static void sample2(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.matchQuery("title", "java spark").minimumShouldMatch("50%"))
				.setRescorer(
						RescoreBuilder.queryRescorer(QueryBuilders.matchPhraseQuery("title", "java spark").slop(50)),
						50)
				.get();
		ElasticSearchUtil.showResults(response);
	}

}
