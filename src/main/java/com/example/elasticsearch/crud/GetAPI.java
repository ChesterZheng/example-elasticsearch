package com.example.elasticsearch.crud;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;

import com.example.elasticsearch.util.ElasticSearchClientUtils;
import com.example.elasticsearch.util.ElasticSearchXPackClientUtils;

public class GetAPI {

	@Test
	public void getIndex() throws Exception {
		/**
		 * 根据id查看文档
		 */
		TransportClient client = ElasticSearchClientUtils
				.connectElasticSearchCluster();
		GetResponse response = client.prepareGet("test", "user", "1").get();

		// GetResponse response = client.prepareGet("test", "user", "1")
		// .setOperationThreaded(false) //`true` 是在不同的线程里执行此次操作
		// .get();
		if (response.isExists()) {
			System.out.println("GetApi 有此文档：" + response.toString());
		} else {
			System.out.println("GetApi 没有此文档：" + response.toString());
		}
		ElasticSearchXPackClientUtils.close(client);
	}
}
