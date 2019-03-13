package com.example.elasticsearch.util;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * ES工具类
 * 
 * @Author Chester_Zheng
 * @Date 2019年3月13日下午2:59:03
 * @Tags
 */
public class ElasticSearchUtil {

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 初始化
	 * @Date 2019年3月13日下午2:59:11
	 * @Tags @return
	 * @Tags @throws Exception
	 * @ReturnType TransportClient
	 */
	@SuppressWarnings("resource")
	public static TransportClient init() throws Exception {
		Settings settings = Settings.builder().put("cluster.name", "Chester-Windows-ElasticSearch").build();
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		return client;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 关闭
	 * @Date 2019年3月13日下午2:59:19
	 * @Tags @param client
	 * @Tags @throws Exception
	 * @ReturnType void
	 */
	public static void destory(TransportClient client) throws Exception {
		client.close();
	}

}
