package com.summersky.gulisop.search.controller;

import com.summersky.gulisop.search.service.ShopSearchService;
import com.summersky.gulisop.search.vo.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-21
 * @Time:21:14
 * @Description:
 */
@Controller
public class SearchController {

    @Autowired
    ShopSearchService shopSearchService;

    @GetMapping("/list.html")
    public String liastPage(SearchParam param){
        // 根据检索条件查询出结果
        Object result = shopSearchService.search(param);
        return "list";
    }
}
