package com.example.elasticsearch.crud;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.transport.TransportClient;

import com.example.elasticsearch.util.ElasticSearchClientUtils;

public class MutliGetAPI {

	public void testMultiGet() throws Exception {
		TransportClient client = ElasticSearchClientUtils
				.connectElasticSearchCluster();
		MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("test", "user", "1") //一个id的方式
                .add("test", "user", "2", "3", "4") //多个id的方式
                .add("shop", "book", "java")  //可以从另外一个索引获取
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) { //迭代返回值
            GetResponse response = itemResponse.getResponse();
            if (response != null && response.isExists()) {      //判断是否存在
                String json = response.getSourceAsString(); //_source 字段
                System.out.println("_source 字段:" + json);
            }
        }
    }
}
