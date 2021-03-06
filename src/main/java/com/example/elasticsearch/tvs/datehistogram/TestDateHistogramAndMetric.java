package com.example.elasticsearch.tvs.datehistogram;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;

import com.example.elasticsearch.util.ElasticSearchUtil;

public class TestDateHistogramAndMetric {

	public static void main(String[] args) throws Exception {
		// 初始化
		TransportClient client = ElasticSearchUtil.init();
		TestDateHistogramAndMetric.sample(client);
		// 销毁
		ElasticSearchUtil.destory(client);
	}

	/*
	 * date histogram，按照我们指定的某个date类型的日期field，以及日期interval，按照一定的日期间隔，去划分bucket
	 * 然后会去扫描每个数据的date field，判断date落在哪个bucket中，就将其放入那个bucket
	 * min_doc_count：假设某个日期interval，2019-01-01~2019-01-31中，一条数据都没有，那么这个区间也是要返回的，
	 * 不然默认是会过滤掉这个区间的 extended_bounds，min，max：划分bucket的时候，会限定在这个起始日期，和截止日期内
	 */
	public static void sample(TransportClient client) throws Exception {
		String soldDate_dateHistogram = "date_histogram";
		String dateFormat = "yyyy-MM-dd";
		String minDate = "2017-01-01";
		String maxDate = "2017-12-31";
		SearchResponse response = client.prepareSearch("tvs").setTypes("sales")
				.addAggregation(AggregationBuilders.dateHistogram(soldDate_dateHistogram).field("sold_date")
						.dateHistogramInterval(DateHistogramInterval.QUARTER).format(dateFormat).minDocCount(0l)
						.extendedBounds(new ExtendedBounds(minDate, maxDate))
						.subAggregation(AggregationBuilders.terms("group_by_brand").field("brand")
								.subAggregation(AggregationBuilders.sum("sum_price").field("price")))
						.subAggregation(AggregationBuilders.sum("total_price").field("price")))
				.setSize(0).get();
		Map<String, Aggregation> aggsMap = response.getAggregations().asMap();
		InternalDateHistogram dateHistogram = (InternalDateHistogram) aggsMap.get(soldDate_dateHistogram);
		List<Bucket> soldDate_dateHistogramBucketList = dateHistogram.getBuckets();
		for (int i = 0; i < soldDate_dateHistogramBucketList.size(); i++) {
			Map<String, Aggregation> groupByBrandAggsMap = soldDate_dateHistogramBucketList.get(i).getAggregations()
					.asMap();
			StringTerms groupByBrand = (StringTerms) groupByBrandAggsMap.get("group_by_brand");
			List<org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket> bucketList = groupByBrand
					.getBuckets();
			for (int j = 0; j < bucketList.size(); j++) {
				System.out.println(bucketList.get(j).getKey() + "=" + bucketList.get(j).getDocCount());
				Map<String, Aggregation> subAggsMap = bucketList.get(j).getAggregations().asMap();
				Sum sumPrice = (Sum) subAggsMap.get("sum_price");
				System.out.println(sumPrice.getValue());
			}
			Sum sumPrice = (Sum) groupByBrandAggsMap.get("total_price");
			System.out.println("total_price = " + sumPrice.getValue());
		}
	}

}
