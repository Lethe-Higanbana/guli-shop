package com.summersky.gulishop.order.dao;

import com.summersky.gulishop.order.entity.OrderOperateHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单操作历史记录
 * 
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 21:33:57
 */
@Mapper
public interface OrderOperateHistoryDao extends BaseMapper<OrderOperateHistoryEntity> {
	
}
