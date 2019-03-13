package com.example.elasticsearch.crud;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;

import com.example.elasticsearch.util.ElasticSearchClientUtils;

public class BulkAPI {

	@Test
	public void testBulk() throws Exception {
		TransportClient client = ElasticSearchClientUtils
				.connectElasticSearchCluster();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		bulkRequest.add(client.prepareIndex("twitter", "tweet", "10")
				.setSource(
						jsonBuilder().startObject().field("username", "Tom")
								.field("postDate", new Date())
								.field("message", "I'm Tom").endObject()));
		bulkRequest.add(client.prepareIndex("twitter", "tweet", "11")
				.setSource(
						jsonBuilder().startObject().field("username", "Jerry")
								.field("postDate", new Date())
								.field("message", "I'm Jerry").endObject()));
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			System.out.println("失败：" + bulkResponse.toString());
		} else {
			System.out.println("处理成功：" + bulkResponse.toString());
		}
	}
}
