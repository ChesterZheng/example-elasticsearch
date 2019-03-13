package com.example.elasticsearch.crud;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.transport.TransportClient;

import com.example.elasticsearch.util.ElasticSearchClientUtils;

public class DeleteAPI {

	public void testDelete() throws Exception {
		TransportClient client = ElasticSearchClientUtils
				.connectElasticSearchCluster();
		DeleteResponse response = client.prepareDelete("test", "user", "1")
				.get();
		System.out.println(response.toString());
		System.out.println("删除成功！");
	}
}
