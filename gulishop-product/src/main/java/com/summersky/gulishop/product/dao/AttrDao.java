package com.summersky.gulishop.product.dao;

import com.summersky.gulishop.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 12:08:45
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {
    /**
     * 在所有属性里面过滤出es的检索属性
     * alt+enter快速生成@param
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrs(@Param("attrIds") List<Long> attrIds);
}
