package com.summersky.gulishop.coupon.dao;

import com.summersky.gulishop.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 21:11:31
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
