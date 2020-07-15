package com.summersky.gulisop.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1、导入依赖
 * 2、编写配置，给容器注入es
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-14
 * @Time:15:52
 * @Description:
 */
@Configuration
public class GulishopElasticSearchConfig {

    /**
     * 创建客户端对象
     */
    @Bean
    public RestHighLevelClient esSearchClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        // es地址
                        new HttpHost("192.168.111.128",9200,"http")
                )
        );
        return client;
    }


    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        /*builder.addHeader("",""+TOKEN);
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory();*/
        COMMON_OPTIONS = builder.build();
    }

}
