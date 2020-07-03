package com.summersky.gulishop.product.dao;

import com.summersky.gulishop.product.entity.SkuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * sku信息
 * 
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 12:08:45
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {
	
}
