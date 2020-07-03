package com.summersky.gulishop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summersky.common.utils.PageUtils;
import com.summersky.gulishop.product.entity.CategoryBrandRelationEntity;

import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 12:08:45
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

