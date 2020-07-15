package com.summersky.gulishop.product.feign;

import com.summersky.common.to.es.SkuEsModel;
import com.summersky.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-15
 * @Time:16:28
 * @Description:
 */
@FeignClient("gulishop-search")
public interface SearchFeignService {
    /**
     * 上架商品
     * @param skuEsModels
     * @return
     */
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
