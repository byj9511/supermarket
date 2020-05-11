package com.byy.member.dao;

import com.byy.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-05-10 23:41:24
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
