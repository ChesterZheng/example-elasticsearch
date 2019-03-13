package com.example.elasticsearch.employee.query;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class EmployeeAggsApp {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Settings settings = Settings.builder().put("cluster.name", "Chester-Windows-ElasticSearch").build();
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

		EmployeeAggsApp.executeSearch(client);
		client.close();
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 聚合分析
	 * @Date 2019年3月12日下午2:14:51
	 * @Tags @param client
	 * @Tags @throws Exception
	 * @ReturnType void
	 */
	private static void executeSearch(TransportClient client) throws Exception {
		SearchResponse searchResponse = client.prepareSearch("company").setTypes("employee")
				.addAggregation(AggregationBuilders.terms("group_by_country").field("country")
						.subAggregation(AggregationBuilders.dateHistogram("group_by_join_date").field("join_date")
								.dateHistogramInterval(DateHistogramInterval.YEAR)
								.subAggregation(AggregationBuilders.avg("avg_salary").field("salary"))))
				.execute().get();
		Map<String, Aggregation> aggregationMap = searchResponse.getAggregations().asMap();
		StringTerms groupByCountry = (StringTerms) aggregationMap.get("group_by_country");
		Iterator<Bucket> groupByCountryIterator = groupByCountry.getBuckets().iterator();
		while (groupByCountryIterator.hasNext()) {
			Bucket bucket = groupByCountryIterator.next();
			System.out.println(bucket.getKey() + "===" + bucket.getDocCount());
			Histogram groupByJoinDate = (Histogram) bucket.getAggregations().asMap().get("group_by_join_date");
			Iterator<? extends org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket> groupByJoinDateIterator = groupByJoinDate
					.getBuckets().iterator();
			while (groupByJoinDateIterator.hasNext()) {
				org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket groupByJoinDateBucket = groupByJoinDateIterator
						.next();
				System.out.println(groupByJoinDateBucket.getKey() + "===" + groupByJoinDateBucket.getDocCount());
				Avg avg = (Avg) groupByJoinDateBucket.getAggregations().asMap().get("avg_salary");
				System.out.println(avg.value());
			}
		}
	}

}
