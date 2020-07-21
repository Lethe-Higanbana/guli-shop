package com.summersky.gulisop.search.vo;

import com.summersky.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-21
 * @Time:22:40
 * @Description:最终检索出来的结果
 */
@Data
public class SearchResult {
    /**
     * 查到的所有商品信息
     */
    private List<SkuEsModel> product;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 总页码
     */
    private Integer totalPages;
    /**
     * 当前检索结果涉及到的所有品牌
     */
    private List<BrandVo> brands;
    /**
     * 当前检索结果涉及到的所有属性
     */
    private List<AttrVo> attrs;
    /**
     * 当前检索结果涉及到的所有分类
     */
    private List<CatalogVo> catalogs;

    /**
     * 品牌VO
     */
    @Data
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    /**
     * 属性VO
     */
    @Data
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    /**
     * 分类VO
     */
    @Data
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }
}

