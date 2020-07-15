package com.summersky.gulisop.search;

import com.alibaba.fastjson.JSON;
import com.summersky.gulisop.search.config.GulishopElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.naming.directory.SearchResult;
import java.io.IOException;

@SpringBootTest
class GulishopSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 检索
     * @throws IOException
     */
    @Test
    public void searchData() throws IOException {
        // 创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        // 指定索引
        searchRequest.indices("");
        // 指定DSL 检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 构造检索条件
        searchSourceBuilder.query(QueryBuilders.matchQuery("address","maill"));
        searchRequest.source(searchSourceBuilder);

        // 执行检索
        SearchResponse search = client.search(searchRequest, GulishopElasticSearchConfig.COMMON_OPTIONS);

        // 分析结果
        System.out.println(search.toString());
        // 获取到检索出的所有数据
        SearchHits hits = search.getHits();
    }

    /**
     * 测试存储数据到es
     */
    @Test
    public void indexData() throws IOException {
        // 指定哪个索引
        IndexRequest indexRequest = new IndexRequest("user");
        // 指定数据id,多次执行不会报错，只是版本会叠加
        indexRequest.id("1");
        // 存数据
        // indexRequest.source("userName","zhangsan","age","18","sex","男");
        User user = new User();
        user.setAge("18");
        user.setSex("男");
        user.setUserName("zhangsan");
        String string = JSON.toJSONString(user);
        // 要保存的数据，第一个参数是保存数据，第二是保存参数类型
        indexRequest.source(string, XContentType.JSON);
        // 执行保存操作
        IndexResponse index = client.index(indexRequest,GulishopElasticSearchConfig.COMMON_OPTIONS);
    }

    @Data
    class User{
        private String userName;
        private String age;
        private String sex;
    }

    @Test
    public void contextLoads() {
        System.out.println(client);
    }

}
