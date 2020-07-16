package com.summersky.gulishop.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-16
 * @Time:10:06
 * @Description:二级分类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {

    /**
     * 1级父分类id
     */
    private String catalog1Id;
    /**
     * 3级子分类
     */
    private List<Catelog3Vo> catalog3List;

    private String id;

    private String name;

    /**
     * 三级分类
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3Vo{
        /**
         * 2级父分类id
         */
        private String catalog2Id;

        private String id;

        private String name;
    }
}
