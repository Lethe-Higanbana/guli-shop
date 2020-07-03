package com.summersky.gulishop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summersky.common.utils.PageUtils;
import com.summersky.gulishop.product.entity.ProductAttrValueEntity;

import java.util.Map;

/**
 * spu属性值
 *
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 12:08:45
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

