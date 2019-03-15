package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction.Modifier;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es function score
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月15日下午4:16:42
 * @Tags
 */
public class TestFunctionScore {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestFunctionScore.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * 使用function_score自定义相关度分数算法
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(
						QueryBuilders
								.functionScoreQuery(
										QueryBuilders.multiMatchQuery("java spark",
												new String[] { "title", "content" }),
										ScoreFunctionBuilders.fieldValueFactorFunction("follower_num")
												.modifier(Modifier.LOG1P).factor(0.5f))
								.boostMode(CombineFunction.SUM).maxBoost(2))
				.get();
		ElasticSearchUtil.showResults(response);
	}

}
