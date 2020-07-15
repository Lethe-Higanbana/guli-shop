package com.summersky.gulisop.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.summersky.common.to.es.SkuEsModel;
import com.summersky.gulisop.search.config.GulishopElasticSearchConfig;
import com.summersky.gulisop.search.constant.EsConstant;
import com.summersky.gulisop.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-15
 * @Time:15:49
 * @Description:
 */
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public Boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 保存到es
        // 1、创建索引，product，建立好映射关系


        // 2、保存数据到es
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            // 指定存到哪个索引
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            // 指定每条数据的ID
            indexRequest.id(skuEsModel.getSkuId().toString());
            // 需要传输的数据
            String s = JSON.toJSONString(skuEsModel);
            // 指定传输数据的类型
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GulishopElasticSearchConfig.COMMON_OPTIONS);

        // TODO 批量错误
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.info("商品上架完成:{}",collect);
        return b;
    }
}
