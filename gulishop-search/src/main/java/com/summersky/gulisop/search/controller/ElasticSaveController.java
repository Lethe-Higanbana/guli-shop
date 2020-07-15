package com.summersky.gulisop.search.controller;

import com.summersky.common.exception.BizCodeEnume;
import com.summersky.common.to.es.SkuEsModel;
import com.summersky.common.utils.R;
import com.summersky.gulisop.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-15
 * @Time:15:39
 * @Description:
 */
@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {

    @Autowired
    private ProductSaveService productSaveService;

    /**
     * 上架商品
     * @param skuEsModels
     * @return
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b = false;
        try {
           b = productSaveService.productStatusUp(skuEsModels);
        } catch (Exception e) {
            log.error("商品上架错误",e);
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (!b){
            return R.ok();
        }else {
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }

    }
}
