package com.summersky.gulisop.search.service.impl;

import com.summersky.gulisop.search.config.GulishopElasticSearchConfig;
import com.summersky.gulisop.search.constant.EsConstant;
import com.summersky.gulisop.search.service.ShopSearchService;
import com.summersky.gulisop.search.vo.SearchParam;
import com.summersky.gulisop.search.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.IOException;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-21
 * @Time:22:05
 * @Description:
 */
@Service
public class ShopSearchServiceImpl implements ShopSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 去ES检索
     * @param searchParam  检索的所有参数
     * @return
     */
    @Override
    public SearchResult search(SearchParam searchParam) {

        // 动态构建出需要查询的DSL语句
        SearchResult result = null;

        // 准备检索请求
        SearchRequest searchRequest = buildSearchRequrest(searchParam);
        try {
            // 执行检索请求
            SearchResponse search = restHighLevelClient.search(searchRequest, GulishopElasticSearchConfig.COMMON_OPTIONS);
            // 分析响应数据并封装成我们需要的格式
            result = buildSearchResult(search);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 构建结果数据
     * @param search
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse search) {

        return null;
    }

    /**
     * 准备检索请求
     * @return
     */
    private SearchRequest buildSearchRequrest(SearchParam searchParam) {
        // 构建DSL语句
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 构建bool-query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // must:必须包含,模糊匹配
        if (!StringUtils.isEmpty(searchParam.getKeyword())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle",searchParam.getKeyword()));
        }
        // filter:过滤-按照三级分类Id来查
        if (searchParam.getCatalog3Id()!=null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId",searchParam.getCatalog3Id()));
        }
        // filter:过滤-按照品牌Id来查
        if (searchParam.getBrandId()!=null && searchParam.getBrandId().size()>0){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId",searchParam.getBrandId()));
        }
        // filter:过滤-按照所有指定属性来查
        if (searchParam.getAttrs()!=null && searchParam.getAttrs().size()>0){
            for (String attrs:searchParam.getAttrs()){
                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
                String[] s = attrs.split("_");
                // 检索的属性Id
                String attrId = s[0];
                // 这个属性的检索用的值
                String[] attrsValue = s[1].split(":");
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                nestedboolQuery.must(QueryBuilders.termsQuery("attrs.attrValue",attrsValue));
                // 每一个都要生成一个nested查询
                NestedQueryBuilder attrs1 = QueryBuilders.nestedQuery("attrs", nestedboolQuery, ScoreMode.None);
                boolQueryBuilder.filter(attrs1);
            }
        }
        // filter:过滤-按照是否有库存来查
        boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock",searchParam.getHasStock() == 1));
        // filter:过滤-按照价格区间来查
        if (!StringUtils.isEmpty(searchParam.getSkuPrice())){
            RangeQueryBuilder skuPrice = QueryBuilders.rangeQuery("skuPrice");
            String[] s = searchParam.getSkuPrice().split("_");
            if (s.length == 2){
                // 区间
                skuPrice.gte(s[0]).lte(s[1]);
            }else if (s.length == 1){
                if (searchParam.getSkuPrice().startsWith("_")){
                    skuPrice.lte(s[0]);
                }
                if (searchParam.getSkuPrice().endsWith("_")){
                    skuPrice.gte(s[0]);
                }
            }
            boolQueryBuilder.filter(skuPrice);
        }
        // 将所有条件封装
        searchSourceBuilder.query(boolQueryBuilder);

        // 排序
        if (!StringUtils.isEmpty(searchParam.getSort())){
            String sort = searchParam.getSort();
            String[] s = sort.split("_");
            SortOrder orders = s[1].equalsIgnoreCase("asc")? SortOrder.ASC:SortOrder.DESC;
            searchSourceBuilder.sort(s[0],orders);
        }
        // 分页
        searchSourceBuilder.from((searchParam.getPageNum()-1)* EsConstant.PRODUCT_PAGESIZE);
        searchSourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);
        // 高亮
        if (!StringUtils.isEmpty(searchParam.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSourceBuilder);
        return searchRequest;
    }
}
