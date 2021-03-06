package com.byy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byy.model.entities.AttrEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品属性
 * 
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {
	
}
