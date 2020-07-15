package com.summersky.gulishop.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summersky.common.utils.PageUtils;
import com.summersky.gulishop.ware.entity.WareSkuEntity;
import com.summersky.gulishop.ware.vo.HasSkuStockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 21:42:02
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    /**
     * 检查sku对应的库存
     * @param skuIds
     * @return
     */
    List<HasSkuStockVo> getHasSkuStock(List<Long> skuIds);
}

