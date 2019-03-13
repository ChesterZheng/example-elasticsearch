package com.example.elasticsearch.util;

import java.net.InetAddress;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

/**
 * ElasticSearch工具类
 * 
 * @Author Chester_Zheng
 * @Date 2018年6月7日下午5:13:53
 * @Tags
 */
public class ElasticSearchXPackClientUtils {

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 连接ElasticSearch集群
	 * @Date 2018年6月7日下午5:12:58
	 * @Tags @return
	 * @Tags @throws Exception
	 * @ReturnType TransportClient
	 */
	@SuppressWarnings("resource")
	public static TransportClient connectElasticSearchCluster()
			throws Exception {
		/**
		 * 如果es集群安装了x-pack插件则以此种方式连接集群 
		 * 1. java客户端的方式是以tcp协议在9300端口上进行通信 
		 * 2.http客户端的方式是以http协议在9200端口上进行通信
		 */
		Settings settings = Settings.builder()
				// 设置用户名和密码
				.put("xpack.security.user", "elastic:changeme")
				//自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
				.put("client.transport.sniff", true)
				// 设置集群名称
				.put("cluster.name", "bs-test-es").build();
		TransportClient client = new PreBuiltXPackTransportClient(settings)
				.addTransportAddress(
						// 配置集群地址和端口
						new InetSocketTransportAddress(InetAddress
								.getByName("192.168.2.74"), 9300)
//						,
//						new InetSocketTransportAddress(InetAddress
//								.getByName("192.168.2.75"), 9300),
//						new InetSocketTransportAddress(InetAddress
//								.getByName("192.168.2.76"), 9300)
						);
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials("elastic", "changeme"));

		System.out.println("ElasticsearchXPackClient 启动成功");
		return client;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 断开ElasticSearch集群连接
	 * @Date 2018年6月7日下午5:12:49
	 * @Tags @param client
	 * @Tags @throws Exception
	 * @ReturnType void
	 */
	public static void close(TransportClient client) throws Exception {
		if (client != null) {
			client.close();
		}
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 打印返回结果
	 * @Date 2018年6月7日下午5:13:33
	 * @Tags @param searchResponse
	 * @ReturnType void
	 */
	public static void println(SearchResponse searchResponse) {
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		for (SearchHit searchHit : searchHits) {
			System.out.println(JSON.toJSONString(searchHit.getSource(),
					SerializerFeature.PrettyFormat));
		}
	}

}
