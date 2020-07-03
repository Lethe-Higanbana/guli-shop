package com.summersky.gulishop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summersky.common.utils.PageUtils;
import com.summersky.gulishop.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 12:08:45
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

