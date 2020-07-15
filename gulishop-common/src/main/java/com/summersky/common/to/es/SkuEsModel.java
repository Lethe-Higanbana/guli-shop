package com.summersky.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-15
 * @Time:10:04
 * @Description:
 */
@Data
public class SkuEsModel {

    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;

    private Boolean hasStock;

    private Long hasScore;

    private Long brandId;

    private Long catalogId;

    private String brandImg;

    private String catalogName;

    private String brandName;

    private List<Attrs> attrs;

    @Data
    public static class Attrs{

        private Long attrId;

        private String attrName;

        private String attrValue;
    }

}
