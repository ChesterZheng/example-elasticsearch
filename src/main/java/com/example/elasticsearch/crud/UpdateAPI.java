package com.example.elasticsearch.crud;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;

import com.example.elasticsearch.util.ElasticSearchClientUtils;

public class UpdateAPI {

	@Test
	public void testUpdate() throws Exception {
		TransportClient client = ElasticSearchClientUtils
				.connectElasticSearchCluster();
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("twitter");
		updateRequest.type("tweet");
		updateRequest.id("2");
		updateRequest.doc(jsonBuilder().startObject()
				.field("user", "http://quanke.name").endObject());
		UpdateResponse updateResponse = client.update(updateRequest).get();
		System.out.println(updateResponse.toString());
	}
}
