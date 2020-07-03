package com.summersky.gulishop.ware.dao;

import com.summersky.gulishop.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 21:42:02
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
