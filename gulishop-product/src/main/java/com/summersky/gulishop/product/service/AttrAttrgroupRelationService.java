package com.summersky.gulishop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summersky.common.utils.PageUtils;
import com.summersky.gulishop.product.entity.AttrAttrgroupRelationEntity;

import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 12:08:45
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

