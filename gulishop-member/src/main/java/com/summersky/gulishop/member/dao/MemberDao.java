package com.summersky.gulishop.member.dao;

import com.summersky.gulishop.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 21:22:06
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
