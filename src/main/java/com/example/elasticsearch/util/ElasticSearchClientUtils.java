package com.example.elasticsearch.util;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ElasticSearchClientUtils {

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 建立连接
	 * @Date 2018年6月8日上午11:37:07
	 * @Tags  @return
	 * @Tags  @throws Exception
	 * @ReturnType TransportClient
	 */
	@SuppressWarnings("resource")
	public static TransportClient connectElasticSearchCluster()
			throws Exception {
		/**
		 * 1. java客户端的方式是以tcp协议在9300端口上进行通信 2. http客户端的方式是以http协议在9200端口上进行通信
		 */
		Settings settings = Settings.builder()
		// 设置集群名称
				.put("cluster.name", "bs-test-es").build();
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddresses(
						// 配置集群地址和端口
						new InetSocketTransportAddress(InetAddress
								.getByName("192.168.2.74"), 9300),
						new InetSocketTransportAddress(InetAddress
								.getByName("192.168.2.75"), 9300),
						new InetSocketTransportAddress(InetAddress
								.getByName("192.168.2.76"), 9300));

		System.out.println("ElasticsearchXPackClient 启动成功");

		return client;
	}
	
	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 关闭连接
	 * @Date 2018年6月8日上午11:36:56
	 * @Tags  @param client
	 * @Tags  @throws Exception
	 * @ReturnType void
	 */
	public static void close(TransportClient client) throws Exception {
		if (client != null) {
			client.close();
		}

	}
}
