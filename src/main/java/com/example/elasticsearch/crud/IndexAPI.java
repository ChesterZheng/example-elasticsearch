package com.example.elasticsearch.crud;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Assert;
import org.junit.Test;

import com.example.elasticsearch.util.ElasticSearchClientUtils;

public class IndexAPI {

	/**
	 * 使用map来构造文档内容
	 * 
	 * @throws Exception
	 */
	@Test
	public void testForUseMap() throws Exception {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("username", "chester");
		json.put("postDate", "2018-06-08");
		json.put("message",
				"It's the first time to connect ElasticSearch cluster...");
		TransportClient client = ElasticSearchClientUtils
				.connectElasticSearchCluster();
		IndexResponse response = client
				.prepareIndex("test", "user", String.valueOf(1))
				.setSource(json).get();
		System.out.println(response.getResult());
		Assert.assertEquals("CREATED", response.getResult().name());
		System.out.println("testForUseMap twitter 创建成功");
		ElasticSearchClientUtils.close(client);
	}

	/**
	 * 使用elasticsearch官方提供的json构造器来构造文档内容
	 * 
	 * @throws Exception
	 */
	@Test
	public void testForUseXContentBuilder() throws Exception {
		XContentBuilder builder = XContentFactory
				.jsonBuilder()
				.startObject()
				.field("username", "chester")
				.field("postDate", new Date())
				.field("age", 10)
				.field("gender", "male")
				.field("message",
						"It's the first time to connect ElasticSearch cluster...")
				.endObject();
		ElasticSearchClientUtils.connectElasticSearchCluster()
				.prepareIndex("test", "user", "2").setSource(builder).get();

		System.out.println("Test For Use XContentBuilder 创建成功");
	}

}
