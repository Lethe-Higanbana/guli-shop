package com.summersky.gulisop.search.service;

import com.summersky.gulisop.search.vo.SearchParam;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-21
 * @Time:22:04
 * @Description:
 */
public interface ShopSearchService {
    /**
     * 根据参数检索出数据返回结果
     * @param searchParam  检索的所有参数
     * @return  返回的结果
     */
    Object search(SearchParam searchParam);
}
