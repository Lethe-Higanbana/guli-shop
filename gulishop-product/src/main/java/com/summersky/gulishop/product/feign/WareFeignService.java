package com.summersky.gulishop.product.feign;

import com.summersky.common.to.HasSkuStockVo;
import com.summersky.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-15
 * @Time:14:30
 * @Description:
 */
@FeignClient("gulishop-ware")
public interface WareFeignService {
    /**
     * 商品上架时需要调用这个接口来查询sku规格是否有库存
     * @param skuIds
     * @return
     */
    @PostMapping("/ware/waresku/hasstock")
    R getHasSkuStock(@RequestBody List<Long> skuIds);
}
