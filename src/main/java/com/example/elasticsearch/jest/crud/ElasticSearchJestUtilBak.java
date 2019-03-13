package com.example.elasticsearch.jest.crud;

import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.Update;
import io.searchbox.indices.ClearCache;
import io.searchbox.indices.CloseIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.Flush;
import io.searchbox.indices.Optimize;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ElasticSearchJestUtilBak {

	private static final Log log = LogFactory
			.getLog(ElasticSearchJestUtilBak.class.getName());

	private InitElasticSearchConfig elasticsearchConfig;

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 删除索引
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public JestResult deleteIndex(String index) {
		DeleteIndex deleteIndex = new DeleteIndex.Builder(index).build();
		JestResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(deleteIndex);
			log.info("deleteIndex == " + jestResult.getJsonString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 清空缓存
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public JestResult clearCache() {
		ClearCache closeIndex = new ClearCache.Builder().build();
		JestResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(closeIndex);
			log.info("clearCache == " + jestResult.getJsonString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 关闭索引
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public JestResult closeIndex(String index) {
		CloseIndex closeIndex = new CloseIndex.Builder(index).build();
		JestResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(closeIndex);
			log.info("closeIndex == " + jestResult.getJsonString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 自动优化索引
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public JestResult optimizeIndex() {
		Optimize optimize = new Optimize.Builder().build();
		JestResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(optimize);
			log.info("optimizeIndex == " + jestResult.getJsonString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 清空索引
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public JestResult flushIndex() {
		Flush flush = new Flush.Builder().build();
		JestResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(flush);
			log.info("flushIndex == " + jestResult.getJsonString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 更新文档
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public JestResult updateDocument(String script, String index, String type,
			String id) {
		Update update = new Update.Builder(script).index(index).type(type)
				.id(id).build();
		JestResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(update);
			log.info("updateDocument == " + jestResult.getJsonString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 删除文档
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public JestResult deleteDocument(String index, String type, String id) {
		Delete delete = new Delete.Builder(id).index(index).type(type).build();
		JestResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(delete);
			log.info("deleteDocument == " + jestResult.getJsonString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 查询出指定文档后删除
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public JestResult deleteDocumentByQuery(String index, String type,
			String params) {

		DeleteByQuery db = new DeleteByQuery.Builder(params).addIndex(index)
				.addType(type).build();

		JestResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(db);
			log.info("deleteDocument == " + jestResult.getJsonString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 获取文档
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	@SuppressWarnings("unchecked")
	public <T> JestResult getDocument(T object, String index, String type,
			String id) {
		Get get = new Get.Builder(index, id).type(type).build();
		JestResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(get);
			T o = (T) jestResult.getSourceAsObject(object.getClass());
			for (Method method : o.getClass().getMethods()) {
				log.info("getDocument == " + method.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult;

	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	@SuppressWarnings("unchecked")
	public <T> List<Hit<T, Void>> searchAll(String index, T o) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex(index).build();
		SearchResult jestResult = null;
		List<?> hits = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(search);
			System.out.println("本次查询共查到：" + jestResult.getTotal() + "个关键字！");
			log.info("本次查询共查到：" + jestResult.getTotal() + "个关键字！");
			hits = jestResult.getHits(o.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (List<Hit<T, Void>>) hits;
	}
	
	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	@SuppressWarnings("unchecked")
	public <T> List<Hit<T, Void>> createSearch(String keyWord, String type,
			T o, String... fields) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(keyWord));
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		for (String field : fields) {
			highlightBuilder.field(field);// 高亮field
		}
		highlightBuilder.preTags("<em>").postTags("</em>");// 高亮标签
		highlightBuilder.fragmentSize(200);// 高亮内容长度
		searchSourceBuilder.highlighter(highlightBuilder);
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex(type).build();
		SearchResult jestResult = null;
		List<?> hits = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(search);
			System.out.println("本次查询共查到：" + jestResult.getTotal() + "个结果！");

			hits = jestResult.getHits(o.getClass());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return (List<Hit<T, Void>>) hits;
	}
	
	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public <T> void bulkIndex(String index, String type, T o) {
		Bulk bulk = new Bulk.Builder().defaultIndex(index).defaultType(type)
				.addAction(Arrays.asList(new Index.Builder(o).build())).build();
		try {
			elasticsearchConfig.getClient().execute(bulk);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public <T> JestResult createIndex(T o, String index, String type) {
		Index index1 = new Index.Builder(o).index(index).type(type).build();
		JestResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(index1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult;
	}

	/**
	 * 
	 * @Author Chester_Zheng
	 * @Description 
	 * @Date 2018年6月12日上午10:05:03
	 * @Tags  @param index
	 * @Tags  @param type
	 * @Tags  @param o
	 * @ReturnType void
	 */
	public JsonObject searchEvent(String param) {
		JsonObject returnData = new JsonParser().parse(param).getAsJsonObject();
		Search search = new Search.Builder(returnData.toString())
				.addType("event").addIndex("pi").build();
		SearchResult jestResult = null;
		try {
			jestResult = elasticsearchConfig.getClient().execute(search);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jestResult.getJsonObject();
	}

}
