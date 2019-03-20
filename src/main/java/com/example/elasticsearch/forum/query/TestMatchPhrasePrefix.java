package com.example.elasticsearch.forum.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import com.example.elasticsearch.util.ElasticSearchUtil;

/**
 * es match phrase prefix 搜索推荐
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月15日下午1:47:11
 * @Tags
 */
public class TestMatchPhrasePrefix {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestMatchPhrasePrefix.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * match_phrase_prefix的原理和phrase_match类似 唯一的区别就是把搜索中使用的最后一个term作为前缀去倒排索引中搜索
	 * slop最多可以移动多少位来匹配term max_expansions 按下面示例中所查询的term中的s开头的单词,
	 * 最多匹配多少个term就不再继续查询倒排索引
	 * match_phrase_prefix性能可能会很差
	 */
	public static void sample(TransportClient client) throws Exception {
		SearchResponse response = client.prepareSearch("forum").setTypes("article")
				.setQuery(QueryBuilders.matchPhrasePrefixQuery("title", "this is s").slop(10).maxExpansions(50)).get();
		ElasticSearchUtil.showQueryResults(response);
	}
}
