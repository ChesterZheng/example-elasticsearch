package com.example.elasticsearch.util;

import java.net.InetAddress;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ElasticSearchClient {

	protected TransportClient client;

	@Before
	@SuppressWarnings("resource")
	public void setUp() throws Exception {
		/**
		 * 1. java客户端的方式是以tcp协议在9300端口上进行通信 
		 * 2. http客户端的方式是以http协议在9200端口上进行通信
		 */
		Settings settings = Settings.builder()
				//设置集群名称
				.put("cluster.name", "bs-test-es").build();
		client = new PreBuiltTransportClient(settings)
				.addTransportAddresses(
						//配置集群地址和端口
						new InetSocketTransportAddress(InetAddress
								.getByName("192.168.2.74"), 9300),
						new InetSocketTransportAddress(InetAddress
								.getByName("192.168.2.75"), 9300),
						new InetSocketTransportAddress(InetAddress
								.getByName("192.168.2.76"), 9300));
		System.out.println("ElasticsearchXPackClient 启动成功");
	}

	@Test
	public void testClientConnection() throws Exception {

		System.out.println("--------------------------");
	}

	@After
	public void tearDown() throws Exception {
		if (client != null) {
			client.close();
		}
	}

	protected void println(SearchResponse searchResponse) {
		ElasticSearchXPackClientUtils.println(searchResponse);
	}
}
