package com.example.elasticsearch.util;

import java.net.InetAddress;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试连接集群
 * Elasticsearch XPack Client 
 * @Author Chester_Zheng
 * @Date 2018年6月7日下午5:32:03
 * @Tags
 */
public class ElasticsearchXPackClient {

	protected TransportClient client;

	@Before
	@SuppressWarnings("resource")
	public void setUp() throws Exception {
		/**
		 * 如果es集群安装了x-pack插件则以此种方式连接集群 
		 * 1. java客户端的方式是以tcp协议在9300端口上进行通信 
		 * 2. http客户端的方式是以http协议在9200端口上进行通信
		 */
		Settings settings = Settings.builder()
				//设置用户名和密码
				.put("xpack.security.user", "elastic:changeme")
				//设置集群名称
				.put("cluster.name", "bs-test-es").build();
		client = new PreBuiltXPackTransportClient(settings)
				.addTransportAddresses(
						//配置集群地址和端口
						new InetSocketTransportAddress(InetAddress
								.getByName("192.168.2.74"), 9300),
						new InetSocketTransportAddress(InetAddress
								.getByName("192.168.2.75"), 9300),
						new InetSocketTransportAddress(InetAddress
								.getByName("192.168.2.76"), 9300));
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials("elastic", "changeme"));

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
