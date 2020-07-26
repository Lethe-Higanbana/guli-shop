package com.summersky.gulisop.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-21
 * @Time:22:01
 * @Description:封装的检索条件
 */
@Data
public class SearchParam {
    /**
     * 页面传递的全文检索关键字
     */
    private String keyword;
    /**
     * 三级分类ID
     */
    private Long catalog3Id;
    /**
     * 排序条件
     */
    private String sort;
    /**
     * 是否有货
     */
    private Integer hasStock = 1;
    /**
     * 价格区间
     */
    private String skuPrice;
    /**
     * 品牌
     * 允许多选
     */
    private List<Long> brandId;
    /**
     * 按照属性筛选
     */
    private List<String> attrs;
    /**
     * 页码
     */
    private Integer pageNum = 1;
}
